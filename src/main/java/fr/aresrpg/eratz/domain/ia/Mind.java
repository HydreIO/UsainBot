package fr.aresrpg.eratz.domain.ia;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.functional.consumer.Consumer;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.connection.Connector;
import fr.aresrpg.eratz.domain.ia.harvest.Harvesting;
import fr.aresrpg.eratz.domain.ia.navigation.Navigator;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

import java.util.concurrent.*;

/**
 * 
 * @since
 */
public class Mind extends Info {

	private Consumer<Interrupt> state;
	private boolean park = false;
	private ScheduledFuture<?> sch;

	public Mind(BotPerso perso) {
		super(perso);
		resetState();
	}

	public void publishState(Consumer<Interrupt> state, long timeout, TimeUnit unit) {
		LOGGER.debug("publish state");
		this.state = state;
		sch = Executors.SCHEDULED.schedule(() -> accept(Interrupt.TIMEOUT), timeout, unit);
	}

	/**
	 * @return the state
	 */
	public Consumer<Interrupt> getState() {
		return state;
	}

	public synchronized void accept(Interrupt interrupt) {
		LOGGER.debug("Try " + interrupt);
		if (park) {
			LOGGER.debug("Refuse " + interrupt);
			return;
		}
		parkMind();
		if (sch != null) sch.cancel(true);
		LOGGER.debug("Accepting " + interrupt);
		CompletableFuture.completedFuture(interrupt).thenAcceptAsync(Threads.threadContextSwitch("mind->accepting", state::accept), Executors.FIXED).thenRun(this::unparkMind);
	}

	private void parkMind() {
		park = true;
	}

	private void unparkMind() {
		park = false;
	}

	public void resetState() {
		LOGGER.debug("Reset state");
		state = i -> LOGGER.debug("Not handled " + i);
	}

	/**
	 * Return a CompletableFuture that is completed when the perso reach the destination map
	 * 
	 * @param destination
	 *            the destination
	 * @return the CompletableFuture
	 */
	public CompletableFuture<Navigator> moveToMap(BotMap destination) {
		LOGGER.debug("Move to map " + destination.getMap().getInfos());
		Navigator navigator = new Navigator(getPerso(), destination);
		if (getPerso().getPerso().getMap().getMapId() == destination.getMapId()) {
			LOGGER.debug("mind -> already on destination -> return");
			return CompletableFuture.completedFuture(navigator);
		}
		return CompletableFuture.completedFuture(navigator).thenApplyAsync(Threads.threadContextSwitch("mind->navigate", Navigator::compilePath), Executors.FIXED)
				.thenCompose(getPerso().getNavRunner()::runNavigation);
	}

	public CompletableFuture<Harvesting> harvest(Interractable... ressources) {
		LOGGER.debug("Mind -> harvest");
		return CompletableFuture.completedFuture(new Harvesting(getPerso(), ressources)).thenComposeAsync(Threads.threadContextSwitch("mind->harvest", getPerso().getHarRunner()::runHarvest),
				Executors.FIXED);
	}

	public CompletableFuture<Connector> connect(long time, TimeUnit unit) {
		return CompletableFuture.completedFuture(new Connector(getPerso(), time, unit)).thenComposeAsync(Threads.threadContextSwitch("mind->connect", getPerso().getConRunner()::runConnection),
				Executors.FIXED);
	}

	public static enum MindState {
		MOVEMENT,
		LOGIN,
		HARVEST,
	}

	@Override
	public void shutdown() {

	}

}
