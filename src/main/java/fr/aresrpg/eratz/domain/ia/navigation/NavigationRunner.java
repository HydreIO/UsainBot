package fr.aresrpg.eratz.domain.ia.navigation;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

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
		LOGGER.success("RUN NAV finished=" + navigator.isFinished());
		if (navigator.isFinished()) return CompletableFuture.completedFuture(null);
		CompletableFuture<CompletableFuture<?>> actions = new CompletableFuture<>();
		getPerso().getMind().publishState(MindState.MOVEMENT, interrupt -> {
			switch (interrupt) {
				case DISCONNECT:
					actions.complete(
							CompletableFuture.<Connector>completedFuture(new Connector(getPerso(), Randoms.nextBetween(BotConfig.RECONNECT_MIN, BotConfig.RECONNECT_MAX), TimeUnit.MILLISECONDS))
									.thenCompose(getPerso().getConRunner()::runConnection)
									.thenApply(cf -> navigator)
									.thenCompose(this::runNavigation).join());
					break;
				case FIGHT_JOIN: // TODO
					break;
				case FULL_POD:
					actions.complete(CompletableFuture.<Navigator>completedFuture(navigator).thenCompose(nav -> {
						getPerso().getUtilities().destroyHeaviestRessource();
						return runNavigation(navigator);
					}).join());
					break;
				case OUT_OF_PATH:
					actions.complete(CompletableFuture.<Navigator>completedFuture(navigator).thenApply(Navigator::compilePath).thenCompose(this::runNavigation).join());
					break;
				case MOVED:
					actions.complete(CompletableFuture.<Navigator>completedFuture(navigator).thenApply(Navigator::notifyMoved).thenCompose(this::runNavigation).join());
					break;
			}
			getPerso().getMind().getStates().remove(MindState.MOVEMENT);
		});
		navigator.runToNext();
		return actions;
	}

}
