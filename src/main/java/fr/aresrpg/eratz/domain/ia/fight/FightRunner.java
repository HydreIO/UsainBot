package fr.aresrpg.eratz.domain.ia.fight;

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
public class FightRunner extends Runner {

	private boolean running;
	CompletableFuture<?> turn = null;

	/**
	 * @param perso
	 */
	public FightRunner(BotPerso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {

	}

	/**
	 * @param running
	 *            the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	public CompletableFuture<Fighting> runFight(Fighting fighting) {
		LOGGER.debug("run fight=====");
		CompletableFuture<CompletableFuture<Fighting>> promise = new CompletableFuture<>();
		getPerso().getMind().publishState(interrupt -> {
			LOGGER.debug("state fight " + interrupt);
			switch (interrupt) {
				case MOVED:
					setRunning(false);
					if (turn != null) turn.cancel(true);
					getPerso().getMind().resetState();
					fighting.notifyFightEnd();
					LOGGER.debug("Fight terminé on complete");
					promise.complete(CompletableFuture.completedFuture(fighting));
					break;
				case STATS:
					fighting.notifyStatsReceive();
					break;
				case PA:
					fighting.notifyPaReceive();
					break;
				case PM:
					fighting.notifyPmReceive();
					break;
				case TURN_START:
					getPerso().getMind().resetState();
					promise.complete(CompletableFuture.completedFuture(fighting).thenComposeAsync(Threads.threadContextSwitch("turn-fighting", this::runFight), Executors.FIXED));
					break;
				case DISCONNECT:
					getPerso().getMind().resetState();
					promise.complete(getPerso().getMind().connect(Randoms.nextBetween(BotConfig.RECONNECT_MIN, BotConfig.RECONNECT_MAX), TimeUnit.MILLISECONDS)
							.thenApplyAsync(Threads.threadContextSwitch("connect->fight", c -> fighting), Executors.FIXED).thenCompose(this::runFight));
					break;
				default:
					return; // avoid reset if non handled
			}
		}); // no timeout
		if (running) turn = CompletableFuture.runAsync(fighting::playTurn, Executors.FIXED);
		else {
			Threads.uSleep(1, TimeUnit.SECONDS);
			getPerso().getPerso().beReady(true);
			getPerso().refreshMap();
			running = true;
		}
		LOGGER.debug("Turn played");
		return promise.thenCompose(Function.identity());
	}

}
