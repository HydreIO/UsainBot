package fr.aresrpg.eratz.domain.ai;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.path.zone.HarvestZone;

import java.util.concurrent.*;

/**
 * 
 * @since
 */
public class BehaviorLayer extends Info implements Layer {

	/**
	 * @param perso
	 */
	public BehaviorLayer(BotPerso perso) {
		super(perso);
	}

	public CompletableFuture<Void> harvestZone(HarvestZone zone) {
		return down().searchNextMap(zone)
				.thenComposeAsync(down()::joinMap)
				.thenComposeAsync(a -> down().harvestMap(zone))
				.thenApply(i -> true)
				.handleAsync((a, t) -> {
					if (t != null) {
						t.printStackTrace();
						if (t instanceof CancellationException || (t instanceof CompletionException && ((CompletionException) t).getCause() instanceof CancellationException)) return false;
						Threads.uSleep(Randoms.nextBetween(2, 4), TimeUnit.SECONDS);
						getPerso().cancelInvits();
					}
					getPerso().getUtilities().setNextMapId(-1);
					LOGGER.severe("handlinnnng !!!");
					return true;
				})
				.thenComposeAsync(a -> {
					if (a) return harvestZone(zone);
					CompletableFuture ftr = new CompletableFuture<>();
					ftr.completeExceptionally(new CancellationException());
					return ftr;
				});
	}

	@Override
	public void shutdown() {
	}

	@Override
	public Layers up() {
		return getPerso().getLayers();
	}

	@Override
	public ActionLayer down() {
		return getPerso().getLayers().actionLayer;
	}

}
