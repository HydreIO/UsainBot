package fr.aresrpg.eratz.domain.ia.navigation;

import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.Mind.MindState;
import fr.aresrpg.eratz.domain.ia.connection.Connector;
import fr.aresrpg.eratz.domain.util.BotConfig;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class NavigationRunner extends Info {

	public NavigationRunner(BotPerso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {
	}

	public CompletableFuture<CompletableFuture<?>> runNavigation(Navigator navigator) {
		if (navigator.isFinished()) return CompletableFuture.completedFuture(null);
		CompletableFuture<CompletableFuture<CompletableFuture<?>>> actions = new CompletableFuture<>();
		getPerso().getMind().publishState(MindState.MOVEMENT, interrupt -> {
			switch (interrupt) {
				case DISCONNECT:
					actions.complete(
							CompletableFuture.<Connector>completedFuture(new Connector(getPerso(), Randoms.nextBetween(BotConfig.RECONNECT_MIN, BotConfig.RECONNECT_MAX), TimeUnit.MILLISECONDS))
									.thenCompose(getPerso().getConRunner()::runConnection)
									.thenApply(cf -> navigator)
									.thenCompose(this::runNavigation));
					break;
				case FIGHT_JOIN: // TODO mais flemme
					break;
				case FULL_POD:
					actions.complete(CompletableFuture.<Navigator>completedFuture(navigator).thenCompose(nav -> {
						getPerso().getUtilities().destroyHeaviestRessource();
						return runNavigation(navigator);
					}));
					break;
				case OUT_OF_PATH:
					actions.complete(CompletableFuture.<Navigator>completedFuture(navigator).thenCompose(nav -> {
						nav.compilePath();
						return runNavigation(navigator);
					}));
					break;
				case MOVED:
					actions.complete(CompletableFuture.<Navigator>completedFuture(navigator).thenCompose(n -> {
						n.notifyMoved();
						return runNavigation(n);
					}));
					break;
			}
			getPerso().getMind().getStates().remove(MindState.MOVEMENT);
		});
		navigator.runToNext();
		return actions.join();
	}

}
