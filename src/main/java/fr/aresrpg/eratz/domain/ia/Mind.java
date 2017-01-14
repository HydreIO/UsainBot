package fr.aresrpg.eratz.domain.ia;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.functional.consumer.Consumer;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.harvest.Harvesting;
import fr.aresrpg.eratz.domain.ia.navigation.Navigator;
import fr.aresrpg.eratz.domain.ia.path.Paths;

import java.util.concurrent.CompletableFuture;

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
		if (getPerso().getPerso().getMap().getMapId() == destination.getMapId()) return CompletableFuture.completedFuture(new Navigator(getPerso(), destination));
		CompletableFuture<Navigator> promise = new CompletableFuture<>();
		getPerso().getNavRunner().runNavigation(new Navigator(getPerso(), destination).compilePath(), promise);
		return promise;
	}

	public CompletableFuture<Harvesting> harvest(Paths path) {
		CompletableFuture<Harvesting> promise = new CompletableFuture<>();
		getPerso().getHarRunner().startHarvest(path, promise);
		return promise;
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
