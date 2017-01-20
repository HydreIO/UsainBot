package fr.aresrpg.eratz.domain.ia.waiter;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.structures.item.Interractable;
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
public class WaitRunner extends Runner {

	/**
	 * @param perso
	 */
	public WaitRunner(BotPerso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {

	}

	public CompletableFuture<Interractable[]> waitFor(Interractable... interractables) {
		LOGGER.debug("run waitFor");
		CompletableFuture<CompletableFuture<Interractable[]>> promise = new CompletableFuture<>();
		getPerso().getMind().publishState(interrupt -> {
			LOGGER.debug("state waiting " + interrupt);
			switch (interrupt) {
				case FIGHT_JOIN:
					return;
				case RESSOURCE_SPAWN:
					if (!getPerso().getPerso().getMap().hasSpawnedRessources(interractables)) return;
					getPerso().getMind().resetState();
					promise.complete(CompletableFuture.completedFuture(interractables));
					return;
				case MOVED:
				case FULL_POD:
					getPerso().getMind().resetState();
					promise.complete(onFullPod().thenCompose(i -> CompletableFuture.completedFuture(interractables)));
					return;
				case DISCONNECT:
					getPerso().getMind().resetState();
					promise.complete(getPerso().getMind().connect(Randoms.nextBetween(BotConfig.RECONNECT_MIN, BotConfig.RECONNECT_MAX), TimeUnit.MILLISECONDS)
							.thenApplyAsync(Threads.threadContextSwitch("connect->harvest", c -> interractables), Executors.FIXED).thenCompose(this::waitFor));
					return;
				default:
					return;
			}
		});
		LOGGER.debug("wating WIP");
		return promise.thenCompose(Function.identity());
	}

}
