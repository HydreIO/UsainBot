package fr.aresrpg.eratz.domain.ia;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.functional.consumer.Consumer;
import fr.aresrpg.dofus.structures.game.FightSpawn;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.connection.Connector;
import fr.aresrpg.eratz.domain.ia.fight.Fighting;
import fr.aresrpg.eratz.domain.ia.harvest.Harvesting;
import fr.aresrpg.eratz.domain.ia.navigation.Navigator;
import fr.aresrpg.eratz.domain.util.functionnal.FutureHandler;
import fr.aresrpg.tofumanchou.domain.data.entity.mob.MobGroup;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * 
 * @since
 */
public class Mind extends Info {

	private Consumer<Interrupt> state;
	private boolean park;

	public Mind(BotPerso perso) {
		super(perso);
		resetState();
	}

	public void publishState(Consumer<Interrupt> state) {
		LOGGER.debug("publish state");
		this.state = state;
	}

	public void handleState(Interrupt interrupt, boolean forced) {
		if (!forced && park) {
			LOGGER.debug("Refusing " + interrupt);
			return;
		}
		LOGGER.debug("Accepting " + interrupt);
		park();
		CompletableFuture.completedFuture(interrupt)
				.thenAcceptAsync(Threads.threadContextSwitch("mind->accepting", state::accept), Executors.FIXED)
				.thenRun(this::unpark)
				.handle(FutureHandler.handleEx());
	}

	public void park() {
		this.park = true;
	}

	public void unpark() {
		this.park = false;
	}

	public void resetState() {
		LOGGER.debug("Reset state and cancel timeout");
		state = i -> LOGGER.debug("Not handled " + i);
	}

	/**
	 * Return a CompletableFuture that is completed when the perso reach the destination map
	 * 
	 * @param destination
	 *            the destination
	 * @return the CompletableFuture
	 */
	public CompletableFuture<?> moveToMap(BotMap destination) {
		LOGGER.debug("Move to map " + destination.getMap().getInfos());
		Navigator navigator = new Navigator(getPerso(), destination);
		if (getPerso().getPerso().getMap().getMapId() == destination.getMapId()) {
			LOGGER.debug("mind -> already on destination -> return");
			return CompletableFuture.completedFuture(navigator);
		}
		return CompletableFuture.completedFuture(navigator).thenApplyAsync(Threads.threadContextSwitch("mind->navigate", Navigator::compilePath), Executors.FIXED)
				.thenCompose(getPerso().getNavRunner()::runNavigation);
	}

	public CompletableFuture<?> harvest(boolean playerJob, Interractable... ressources) {
		LOGGER.debug("Mind -> harvest");
		return CompletableFuture.completedFuture(new Harvesting(getPerso(), playerJob, ressources))
				.thenComposeAsync(Threads.threadContextSwitch("mind->harvest", getPerso().getHarRunner()::runHarvest), Executors.FIXED);
	}

	public CompletableFuture<?> connect(long time, TimeUnit unit) {
		LOGGER.debug("Mind -> connect");
		return CompletableFuture.completedFuture(new Connector(getPerso(), time, unit)).thenComposeAsync(Threads.threadContextSwitch("mind->connect", getPerso().getConRunner()::runConnection),
				Executors.FIXED);
	}

	public CompletableFuture<?> fight() {
		LOGGER.debug("Mind -> fight");
		return CompletableFuture.completedFuture(new Fighting(getPerso()))
				.thenComposeAsync(getPerso().getFiRunner()::runFight, Executors.FIXED);
	}

	public CompletableFuture<?> waitSpawn(Interractable... ress) {
		LOGGER.debug("Mind -> wait");
		return CompletableFuture.completedFuture(ress).thenComposeAsync(getPerso().getWaRunner()::waitFor, Executors.FIXED);
	}

	public CompletableFuture<?> waitMobSpawn(Predicate<MobGroup> valid) {
		LOGGER.debug("Mind -> waitmob");
		return CompletableFuture.completedFuture(valid).thenComposeAsync(getPerso().getWaRunner()::waitFor, Executors.FIXED);
	}

	public CompletableFuture<?> waitFightSpawn(Predicate<FightSpawn> valid) {
		LOGGER.debug("Mind -> waitfight");
		return CompletableFuture.completedFuture(valid).thenComposeAsync(getPerso().getWaRunner()::waitForFight, Executors.FIXED);
	}

	@Override
	public void shutdown() {

	}

}
