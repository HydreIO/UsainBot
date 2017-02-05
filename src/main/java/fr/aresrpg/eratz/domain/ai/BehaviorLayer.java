package fr.aresrpg.eratz.domain.ai;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.path.zone.HarvestZone;
import fr.aresrpg.eratz.domain.util.UtilFunc;
import fr.aresrpg.eratz.domain.util.functionnal.Handling;

import java.util.Arrays;
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
					if (getPerso().getUtilities().isFullPod()) {
						getPerso().getUtilities().destroyHeaviestRessource();
						return null;
					}
					LOGGER.severe("handlinnnng !!!");
					return true;
				})
				.thenComposeAsync(a -> {
					if (a == null) return bankDeposit().thenComposeAsync(v -> harvestZone(zone));
					if (a) return harvestZone(zone);
					CompletableFuture ftr = new CompletableFuture<>();
					ftr.completeExceptionally(new CancellationException());
					return ftr;
				});
	}

	public CompletableFuture<Void> bankDeposit() {
		LOGGER.debug("Bank deposit");
		return down().joinBank()
				.thenComposeAsync(v -> down().down().openBank())
				.thenComposeAsync(v -> CompletableFuture.runAsync(getPerso().getUtilities()::depositBank));
	}

	public CompletableFuture<Void> testing() {
		return down().joinBank().thenComposeAsync(i -> down().down().waitTime(1, TimeUnit.SECONDS)).thenComposeAsync(v -> down().down().openBank()).thenComposeAsync(
				i -> CompletableFuture.runAsync(() -> Arrays.stream(UtilFunc.retrieveWoodStacks(getPerso())).forEach(mi -> down().down().retrieveItem(mi.getItemUid(), mi.getAmount()))))
				.handle(Handling.handleEx());
	}

	public CompletableFuture<Void> tofuSmash() {
		Threads.uSleep(250);
		return down().playAndWinTofuSmash()
				.handleAsync((a, t) -> {
					if (t != null) t.printStackTrace();
					if (t instanceof CancellationException || (t instanceof CompletionException && ((CompletionException) t).getCause() instanceof CancellationException)) return false;
					LOGGER.severe("handlinnnng !!!");
					return true;
				})
				.thenComposeAsync(a -> {
					LOGGER.debug("a = " + a);
					if (a) return testing();
					CompletableFuture ftr = new CompletableFuture<>();
					ftr.completeExceptionally(new CancellationException());
					return ftr;
				});
	}

	/*
	 * return down().joinBank()
	 * .thenComposeAsync(m -> down().down().waitTime(2, TimeUnit.SECONDS))
	 * .thenComposeAsync(m -> down().down().openBank())
	 * .thenComposeAsync(a -> down().down().moveKamas(50))
	 * .thenRun(getPerso().getPerso()::leaveExchange);
	 */

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
