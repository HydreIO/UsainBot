package fr.aresrpg.eratz.domain.ia.fight;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.util.BotConfig;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 
 * @since
 */
public class FightRunner extends Info {

	/**
	 * @param perso
	 */
	public FightRunner(BotPerso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {

	}

	public CompletableFuture<Fighting> runFight(Fighting fighting) {
		LOGGER.debug("run fight");
		CompletableFuture<CompletableFuture<Fighting>> promise = new CompletableFuture<>();
		getPerso().getMind().publishState(interrupt -> {
			LOGGER.debug("state fight " + interrupt + " thd:" + Thread.currentThread().getName());
			switch (interrupt) {
				case MOVED:
					getPerso().getMind().resetState();
					promise.complete(CompletableFuture.completedFuture(fighting));
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
		LOGGER.debug("play fight turn");
		fighting.playTurn();
		LOGGER.debug("Turn played");
		return promise.thenCompose(Function.identity());
	}

}
