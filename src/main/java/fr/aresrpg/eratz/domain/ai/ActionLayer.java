package fr.aresrpg.eratz.domain.ai;

import static fr.aresrpg.commons.domain.condition.match.Matcher.def;
import static fr.aresrpg.commons.domain.condition.match.Matcher.when;
import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.condition.Option;
import fr.aresrpg.commons.domain.condition.match.Matcher;
import fr.aresrpg.commons.domain.util.Consumers;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.HarvestUtilities.HarvestTarget;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.fight.behavior.FightBehavior;
import fr.aresrpg.eratz.domain.ia.path.zone.HarvestZone;
import fr.aresrpg.eratz.domain.ia.path.zone.Zone;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger;
import fr.aresrpg.tofumanchou.domain.data.enums.PotionType;
import fr.aresrpg.tofumanchou.domain.data.enums.Smiley;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * 
 * @since
 */
public class ActionLayer extends Info implements Layer {

	/**
	 * @param perso
	 */
	public ActionLayer(BotPerso perso) {
		super(perso);
	}

	public CompletableFuture<Void> harvestMap(HarvestZone zone) {
		LOGGER.debug("harvest map !");
		Option<HarvestTarget> mayAvailable = getPerso().getHarvestUtilities().getAvailableRessource(zone.isPlayerJob(), zone.getRessources());
		CompletableFuture<CompletableFuture<Void>> promise = new CompletableFuture();
		mayAvailable
				.ifAbsent(() -> {
					LOGGER.debug("sortie du harvesting !");
					promise.complete(CompletableFuture.completedFuture(null));
				})
				.ifPresent(available -> promise.complete(down().move(available.cellToGo, true, true)
						.thenRunAsync(() -> Consumers.execute(getPerso().getPerso()::sendSmiley, Smiley.getRandomTrollSmiley(), Randoms.nextBool()))
						.thenComposeAsync(i -> down().harvest(available.cellToHarvest, available.skill).thenApply(h -> true).exceptionally(t -> {
							if (t instanceof CancellationException || (t instanceof CompletionException && ((CompletionException) t).getCause() instanceof CancellationException)) return false;
							LOGGER.error("Unable to harvest <> recover::ok");
							return true;
						}))
						.thenComposeAsync(bool -> {
							if (bool) return harvestMap(zone);
							CompletableFuture ftr = new CompletableFuture();
							ftr.completeExceptionally(new CancellationException());
							return ftr;
						})));
		return promise.thenComposeAsync(Function.identity());
	}

	/**
	 * Attempt to join the given map
	 * 
	 * @param map
	 *            the destination
	 * @return an empty CompletableFuture
	 */
	public CompletableFuture<Void> joinMap(BotMap map) {
		LOGGER.debug("joinmap");
		getPerso().getUtilities().setNextMapId(-1);
		return CompletableFuture.completedFuture(getPerso().getMapUtilities().searchPath(map))
				.thenComposeAsync(this::loopMap)
				.thenComposeAsync(Matcher.<Boolean, CompletableFuture<Void>>matcher(
						when(b -> {
							LOGGER.debug("value = " + b);
							return b;
						}, CompletableFuture.completedFuture(null)),
						def(b -> {
							LOGGER.debug("def value = " + b);
							return joinMap(map);
						}))::match);
	}

	/**
	 * Attempt to take all triggers
	 * 
	 * @param path
	 *            the path
	 * @return inside a CompletableFuture :: true if the destination seem reached, false if the player is out of the path
	 */
	private CompletableFuture<Boolean> loopMap(Queue<TeleporterTrigger> path) {
		Threads.uSleep(1000);
		LOGGER.debug("loopmap");
		if (path.isEmpty()) {
			LOGGER.debug("empty return true !");
			return CompletableFuture.completedFuture(true);
		} else if (!getPerso().getUtilities().isOnPath()) return CompletableFuture.completedFuture(false);
		return CompletableFuture.supplyAsync(path.peek().getDest()::getMapId)
				.thenComposeAsync(i -> {
					getPerso().getUtilities().setNextMapId(i);
					return CompletableFuture.completedFuture(null);
				})
				.thenComposeAsync(i -> CompletableFuture.completedFuture(path.poll()))
				.thenComposeAsync(Matcher.<TeleporterTrigger, CompletableFuture<Boolean>>matcher(
						when(Objects::isNull, CompletableFuture.completedFuture(null)),
						when(TeleporterTrigger::isZaap,
								trg -> CompletableFuture.completedFuture(trg.getCellId())
										.thenComposeAsync(i -> down().move(getPerso().getUtilities().getRandomAccessibleCellNextTo(i, Pathfinding::getNeighbors), true, false).thenApplyAsync(v -> i))
										.thenComposeAsync(down()::openZaap)
										.thenComposeAsync(a -> down().useZaap(trg.getDest().getMapId()))
										.thenApplyAsync(t -> true)),
						when(TeleporterTrigger::isZaapi,
								trg -> CompletableFuture.completedFuture(trg.getCellId())
										.thenComposeAsync(i -> down().move(getPerso().getUtilities().getRandomAccessibleCellNextTo(i, Pathfinding::getNeighbors), true, false).thenApplyAsync(v -> i))
										.thenComposeAsync(down()::openZaapi)
										.thenComposeAsync(a -> down().useZaapi(trg.getDest().getMapId()))
										.thenApplyAsync(t -> true)),
						when(TeleporterTrigger::isTeleporter, trg -> down().listenMapJoinResponse().thenCombineAsync(down().move(trg.getCellId(), true, true), (a, b) -> a).thenApplyAsync(t -> true)),
						when(TeleporterTrigger::isPopoAstrub, trg -> CompletableFuture.completedFuture(PotionType.RAPPEL).thenComposeAsync(down()::usePopo).thenApplyAsync(t -> true)),
						when(TeleporterTrigger::isPopoBonta, trg -> CompletableFuture.completedFuture(PotionType.BONTA).thenComposeAsync(down()::usePopo).thenApplyAsync(t -> true)),
						when(TeleporterTrigger::isPopoBrak, trg -> CompletableFuture.completedFuture(PotionType.BRAKMAR).thenComposeAsync(down()::usePopo).thenApplyAsync(t -> true)),
						def(CompletableFuture.completedFuture(false)))::match)
				.thenComposeAsync(Matcher.<Boolean, CompletableFuture<Boolean>>matcher(
						when(Objects::isNull, CompletableFuture.completedFuture(true)),
						when(Boolean::booleanValue, a -> loopMap(path)),
						def(CompletableFuture::completedFuture))::match);
	}

	public CompletableFuture<Void> playFightTurn(FightBehavior behavior) {
		return null;
	}

	public CompletableFuture<BotMap> searchNextMap(Zone zone) {
		return CompletableFuture.completedFuture(MapsManager.getMap(zone.getNextMap()));
	}

	@Override
	public void shutdown() {
	}

	@Override
	public BehaviorLayer up() {
		return getPerso().getLayers().behaviorLayer;
	}

	@Override
	public BasicLayer down() {
		return getPerso().getLayers().basicLayer;
	}

}
