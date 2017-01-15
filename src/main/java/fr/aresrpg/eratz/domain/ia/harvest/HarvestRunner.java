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

	private boolean fullPod;

	/**
	 * @param perso
	 */
	public HarvestRunner(BotPerso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {

	}

	void resetStateMachine() {
		fullPod = false;
	}

	public CompletableFuture<Harvesting> runHarvest(Harvesting harvesting) {
		if (harvesting.isMapHarvested()) {
			LOGGER.debug("Harvest completed ");
			return CompletableFuture.completedFuture(harvesting);
		}
		LOGGER.debug("run harvest");
		CompletableFuture<CompletableFuture<Harvesting>> promise = new CompletableFuture<>();
		getPerso().getMind().publishState(interrupt -> {
			LOGGER.debug("state " + interrupt);
			switch (interrupt) {
				case FIGHT_JOIN:
					break;
				case OVER_POD:
				case FULL_POD:
					if (fullPod) return;
					fullPod = true;
					promise.complete(onFullPod().thenRun(this::resetStateMachine).thenCompose(i -> CompletableFuture.completedFuture(harvesting)));
					break;
				case ACTION_STOP:
				case RESSOURCE_STEAL:
				case RESSOURCE_HARVESTED:
					String name = interrupt == Interrupt.ACTION_STOP ? "actionstop->harvest" : interrupt == Interrupt.RESSOURCE_STEAL ? "steal->harvest" : "harvested->harvest";
					getPerso().getUtilities().setCurrentHarvest(-1);
					promise.complete(CompletableFuture.completedFuture(harvesting).thenComposeAsync(Threads.threadContextSwitch(name, this::runHarvest), Executors.FIXED));
					break;
				case DISCONNECT:
					promise.complete(getPerso().getMind().connect(Randoms.nextBetween(BotConfig.RECONNECT_MIN, BotConfig.RECONNECT_MAX), TimeUnit.MILLISECONDS)
							.thenApplyAsync(Threads.threadContextSwitch("connect->harvest", c -> harvesting), Executors.FIXED).thenCompose(this::runHarvest));
					break;
				default:
					return; // avoid reset if non handled
			}
			getPerso().getMind().resetState();
		});
		boolean harvest = harvesting.harvest();
		LOGGER.debug(harvest + " finish run harvest !!!!!!!!!!!!");
		return promise.thenCompose(Function.identity());
	}

}
