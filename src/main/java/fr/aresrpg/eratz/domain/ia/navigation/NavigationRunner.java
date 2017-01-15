package fr.aresrpg.eratz.domain.ia.navigation;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.Interrupt;
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

	private boolean fullPod;

	public NavigationRunner(BotPerso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {
	}

	void resetStateMachine() {
		fullPod = false;
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
					break;
				case FIGHT_JOIN: // TODO
					return;
				case OVER_POD:
				case FULL_POD:
					if (fullPod) return;
					getPerso().getMind().resetState();
					fullPod = true;
					promise.complete(onFullPod().thenRun(this::resetStateMachine).thenCompose(i -> CompletableFuture.completedFuture(navigator)));
					break;
				case ACTION_STOP: // on laisse le notify moved pour recalculer un path
					if (getPerso().getUtilities().getPodsPercent() > 95) getPerso().getUtilities().destroyHeaviestRessource();
					else {
						//getPerso().getMind().resetState();
						getPerso().getPerso().disconnect();
						break;
					}
				case TIMEOUT:
				case MOVED:
					getPerso().getMind().resetState();
					String name = interrupt == Interrupt.ACTION_STOP ? "actionstop->navigate" : "moved->navigate";
					navigator.notifyMoved();
					if (navigator.isFinished()) {
						LOGGER.debug("navigation completed !");
						navigator.resetPersoPath();
						// complete async pour éviter que la state soit pas reset quand la suite des actions s'éffectue (probleme rencontré)
						promise.complete(CompletableFuture.completedFuture(navigator));
					} else promise.complete(CompletableFuture.completedFuture(navigator).thenComposeAsync(Threads.threadContextSwitch(name, this::runNavigation), Executors.FIXED));
					break;
				default:
					return; // avoid reset if non handled
			}
		}, 10, TimeUnit.SECONDS);
		if (!getPerso().getUtilities().isOnPath()) {
			LOGGER.debug("NOT ON PATH RECALCULING");
			navigator.compilePath();
		}
		navigator.runToNext();
		LOGGER.debug("runned to next ok !");
		return promise.thenCompose(Function.identity());
	}

}
