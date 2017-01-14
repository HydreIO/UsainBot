package fr.aresrpg.eratz.domain.ia.harvest;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

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
public class HarvestRunner extends Info {

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
		if (harvesting.isMapHarvested()) return CompletableFuture.completedFuture(harvesting);
		LOGGER.debug("run harvest");
		CompletableFuture<CompletableFuture<Harvesting>> promise = new CompletableFuture<>();
		getPerso().getMind().publishState(interrupt -> {
			LOGGER.debug("state " + interrupt);
			switch (interrupt) {
				case FIGHT_JOIN:
					break;
				case RESSOURCE_STEAL:
				case RESSOURCE_HARVESTED:
					getPerso().getUtilities().setCurrentHarvest(-1);
					promise.complete(CompletableFuture.completedFuture(harvesting).thenComposeAsync(this::runHarvest, Executors.FIXED));
					break;
				case DISCONNECT:
					promise.complete(getPerso().getMind().connect(Randoms.nextBetween(BotConfig.RECONNECT_MIN, BotConfig.RECONNECT_MAX), TimeUnit.MILLISECONDS)
							.thenApplyAsync(c -> harvesting, Executors.FIXED).thenCompose(this::runHarvest));
					break;
				default:
					return; // avoid reset if non handled
			}
			getPerso().getMind().resetState();
		});
		harvesting.harvest();
		LOGGER.debug("finish run harvest !!!!!!!!!!!!");
		return promise.thenCompose(Function.identity());
	}

}
