package fr.aresrpg.eratz.domain.ia;

import fr.aresrpg.commons.domain.functional.consumer.Consumer;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.connection.Connector;
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

	public void forEachState(Consumer<Consumer<Interrupt>> actions) {
		states.values().stream().forEach(actions::accept);
	}

	public CompletableFuture<CompletableFuture<?>> moveToMap(BotMap destination) {
		if (getPerso().getPerso().getMap().getMapId() == destination.getMapId()) return CompletableFuture.completedFuture(null);
		Navigator navigator = new Navigator(getPerso(), destination);
		return CompletableFuture.<Navigator>completedFuture(navigator).thenApply(Navigator::compilePath).thenCompose(getPerso().getNavRunner()::runNavigation);
	}

	public CompletableFuture<CompletableFuture<?>> connect(long time, TimeUnit unit) {
		Connector con = new Connector(getPerso(), time, unit);
		return CompletableFuture.<Connector>completedFuture(con).thenCompose(getPerso().getConRunner()::runConnection);
	}

	public static enum MindState {
		MOVEMENT,
		LOGIN,
	}

	@Override
	public void shutdown() {

	}

}
