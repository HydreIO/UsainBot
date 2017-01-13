package fr.aresrpg.eratz.domain.ia;

import fr.aresrpg.commons.domain.functional.consumer.Consumer;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.navigation.Navigator;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

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

	public void forEachState(Consumer<Consumer<Interrupt>> actions) {
		states.values().stream().forEach(actions::accept);
	}

	public CompletableFuture<CompletableFuture<Navigator>> moveToMap(BotMap destination) {
		CompletableFuture<CompletableFuture<Navigator>> promise = CompletableFuture.completedFuture(CompletableFuture.completedFuture(new Navigator(getPerso(), destination)));
		if (getPerso().getPerso().getMap().getMapId() != destination.getMapId())
			promise.thenAcceptAsync(c -> c.thenApply(Navigator::compilePath).thenCompose(getPerso().getNavRunner()::runNavigation), Executors.FIXED);
		return promise;
	}

	public static enum MindState {
		MOVEMENT,
		LOGIN,
	}

	@Override
	public void shutdown() {

	}

}
