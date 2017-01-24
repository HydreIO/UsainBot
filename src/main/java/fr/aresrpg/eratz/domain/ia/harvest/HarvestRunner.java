package fr.aresrpg.eratz.domain.ia.harvest;

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
public class HarvestRunner extends Runner {

	/**
	 * @param perso
	 */
	public HarvestRunner(BotPerso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {

	}

	public CompletableFuture<Harvesting> runHarvest(Harvesting harvesting) {
		LOGGER.debug("run harvest");
		CompletableFuture<CompletableFuture<Harvesting>> promise = new CompletableFuture<>();
		if (!harvesting.harvest()) {
			LOGGER.debug("Harvest completed ");
			return CompletableFuture.completedFuture(harvesting);
		}
		getPerso().getMind().publishState(interrupt -> {
			LOGGER.debug("state " + interrupt);
			switch (interrupt) {
				case FIGHT_JOIN:
					getPerso().getMind().resetState();
					promise.complete(getPerso().getMind().fight()
							.thenApplyAsync(Threads.threadContextSwitch("connect->harvest", c -> harvesting), Executors.FIXED).thenCompose(this::runHarvest));
					return;
				case FULL_POD:
					getPerso().getMind().resetState();
					promise.complete(onFullPod().thenCompose(i -> CompletableFuture.completedFuture(harvesting)));
					return;
				case ACTION_STOP: // des fois la frame s'update cash (donc bug du serv) et le serv envoie une action error (dofus jte chie dessus)
					getPerso().getMind().resetState();
					Executors.FIXED.execute(() -> recoverAfterActionError(promise));
					return;
				case MOVED:
				case RESSOURCE_STEAL:
				case RESSOURCE_HARVESTED:
					getPerso().getMind().resetState();
					String name = interrupt == Interrupt.MOVED ? "moved->harvest" : interrupt == Interrupt.RESSOURCE_STEAL ? "steal->harvest" : "harvested->harvest";
					getPerso().getUtilities().setCurrentHarvest(-1);
					Threads.uSleep(1, TimeUnit.SECONDS); // wait au cas ou join fight
					if (getPerso().isInFight()) promise.complete(getPerso().getMind().fight()
							.thenApplyAsync(Threads.threadContextSwitch("fight->harvest", c -> harvesting), Executors.FIXED).thenCompose(this::runHarvest));
					else
						promise.complete(CompletableFuture.completedFuture(harvesting).thenComposeAsync(Threads.threadContextSwitch(name, this::runHarvest), Executors.FIXED));
					return;
				case DISCONNECT:
					getPerso().getMind().resetState();
					promise.complete(getPerso().getMind().connect(Randoms.nextBetween(BotConfig.RECONNECT_MIN, BotConfig.RECONNECT_MAX), TimeUnit.MILLISECONDS)
							.thenApplyAsync(Threads.threadContextSwitch("connect->harvest", c -> harvesting), Executors.FIXED).thenCompose(this::runHarvest));
					return;
				default:
					return;
			}
		});
		LOGGER.debug("Harvest WIP");
		return promise.thenCompose(Function.identity());
	}

}
