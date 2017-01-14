package fr.aresrpg.eratz.domain.ia;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.functional.consumer.Consumer;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.connection.Connector;
import fr.aresrpg.eratz.domain.ia.harvest.Harvesting;
import fr.aresrpg.eratz.domain.ia.navigation.Navigator;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class Mind extends Info {

	private Consumer<Interrupt> state = i -> {};

	public Mind(BotPerso perso) {
		super(perso);
	}

	public void publishState(Consumer<Interrupt> state) {
		CompletableFuture.completedFuture(state).thenAcceptAsync(this::setState, Executors.FIXED);
	}

	public void setState(Consumer<Interrupt> state) {
		this.state = state;
	}

	/**
	 * @return the state
	 */
	public Consumer<Interrupt> getState() {
		return state;
	}

	public void accept(Interrupt interrupt) {
		state.accept(interrupt);
	}

	public void resetState() {
		state = i -> {};
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
		if (getPerso().getPerso().getMap().getMapId() == destination.getMapId()) return CompletableFuture.completedFuture(navigator);
		return CompletableFuture.completedFuture(navigator).thenApplyAsync(Navigator::compilePath, Executors.FIXED).thenCompose(getPerso().getNavRunner()::runNavigation);
	}

	public CompletableFuture<Harvesting> harvest(Interractable... ressources) {
		return CompletableFuture.completedFuture(new Harvesting(getPerso(), ressources)).thenComposeAsync(getPerso().getHarRunner()::runHarvest, Executors.FIXED);
	}

	public CompletableFuture<Connector> connect(long time, TimeUnit unit) {
		return CompletableFuture.completedFuture(new Connector(getPerso(), time, unit)).thenComposeAsync(getPerso().getConRunner()::runConnection, Executors.FIXED);
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
