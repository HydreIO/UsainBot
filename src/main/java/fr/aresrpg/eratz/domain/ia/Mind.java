package fr.aresrpg.eratz.domain.ia;

import fr.aresrpg.commons.domain.functional.consumer.Consumer;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.navigation.Navigator;

import java.util.concurrent.*;

/**
 * 
 * @since
 */
public class Mind extends Info {

	private ConcurrentMap<MindState, Consumer<Interrupt>> states = new ConcurrentHashMap<>();

	public Mind(BotPerso perso) {
		super(perso);
	}

	public void publishState(MindState type, Consumer<Interrupt> state) {
		states.put(type, state);
	}

	/**
	 * @return the states
	 */
	public ConcurrentMap<MindState, Consumer<Interrupt>> getStates() {
		return states;
	}

	public void accept(Interrupt interrupt) {
		states.values().stream().forEach(c -> c.accept(interrupt));
	}

	/**
	 * Return a CompletableFuture that is completed when the perso reach the destination map
	 * 
	 * @param destination
	 *            the destination
	 * @return the CompletableFuture
	 */
	public CompletableFuture<Navigator> moveToMap(BotMap destination) {
		if (getPerso().getPerso().getMap().getMapId() == destination.getMapId()) return CompletableFuture.completedFuture(new Navigator(getPerso(), destination));
		CompletableFuture<Navigator> promise = new CompletableFuture<>();
		getPerso().getNavRunner().runNavigation(new Navigator(getPerso(), destination).compilePath(), promise);
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
