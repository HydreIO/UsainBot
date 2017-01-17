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
					return;
				case FULL_POD:
					getPerso().getMind().resetState();
					promise.complete(onFullPod().thenCompose(i -> CompletableFuture.completedFuture(harvesting)));
					return;
				case ACTION_STOP:
					System.exit(1);
					return;
				case RESSOURCE_STEAL:
				case RESSOURCE_HARVESTED:
					getPerso().getMind().resetState();
					String name = interrupt == Interrupt.ACTION_STOP ? "actionstop->harvest" : interrupt == Interrupt.RESSOURCE_STEAL ? "steal->harvest" : "harvested->harvest";
					getPerso().getUtilities().setCurrentHarvest(-1);
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
