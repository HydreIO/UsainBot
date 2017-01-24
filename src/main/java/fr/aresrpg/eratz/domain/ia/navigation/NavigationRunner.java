package fr.aresrpg.eratz.domain.ia.navigation;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.Runner;
import fr.aresrpg.eratz.domain.util.BotConfig;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 
 * @since
 */
public class NavigationRunner extends Runner {

	public NavigationRunner(BotPerso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {
	}

	public CompletableFuture<Navigator> runNavigation(Navigator navigator) {
		LOGGER.success("run navigation");
		CompletableFuture<CompletableFuture<Navigator>> promise = new CompletableFuture<>();
		getPerso().getMind().publishState(interrupt -> {
			LOGGER.debug("state move " + interrupt);
			switch (interrupt) {
				case DISCONNECT:
					getPerso().getMind().resetState();
					promise.complete(getPerso().getMind().connect(Randoms.nextBetween(BotConfig.RECONNECT_MIN, BotConfig.RECONNECT_MAX), TimeUnit.MILLISECONDS)
							.thenApplyAsync(Threads.threadContextSwitch("connect->navigate", c -> navigator), Executors.FIXED).thenCompose(this::runNavigation));
					return;
				case FIGHT_JOIN:
					getPerso().getMind().resetState();
					promise.complete(getPerso().getMind().fight()
							.thenApplyAsync(Threads.threadContextSwitch("connect->harvest", c -> navigator), Executors.FIXED).thenCompose(this::runNavigation));
					return;
				case FULL_POD:
					getPerso().getMind().resetState();
					promise.complete(onFullPod().thenCompose(i -> CompletableFuture.completedFuture(navigator)));
					return;
				case ACTION_STOP:
					getPerso().getMind().resetState();
					Executors.FIXED.execute(() -> recoverAfterActionError(promise));
					return;
				case MOVED:
					getPerso().getMind().resetState();
					navigator.notifyMoved();
					if (navigator.isFinished()) {
						LOGGER.debug("navigation completed !");
						navigator.resetPersoPath();
						promise.complete(CompletableFuture.completedFuture(navigator));
					} else promise.complete(CompletableFuture.completedFuture(navigator).thenComposeAsync(Threads.threadContextSwitch("moved->navigate", this::runNavigation), Executors.FIXED));
					return;
				default:
					return; // avoid reset if non handled
			}
		});
		if (!getPerso().getUtilities().isOnPath()) {
			LOGGER.debug("NOT ON PATH RECALCULING");
			navigator.compilePath();
		}
		navigator.runToNext();
		LOGGER.debug("runToNext() called !");
		return promise.thenCompose(Function.identity());
	}

}
