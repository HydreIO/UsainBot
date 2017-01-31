package fr.aresrpg.eratz.domain.ai;

import static fr.aresrpg.commons.domain.condition.match.Matcher.def;
import static fr.aresrpg.commons.domain.condition.match.Matcher.when;
import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.condition.Option;
import fr.aresrpg.commons.domain.condition.match.Matcher;
import fr.aresrpg.commons.domain.util.Consumers;
import fr.aresrpg.commons.domain.util.Randoms;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
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
				.ifAbsent(() -> promise.complete(CompletableFuture.completedFuture(null)))
				.ifPresent(available -> promise.complete(down().move(available.cellToGo, true, true)
						.thenRunAsync(() -> Consumers.execute(getPerso().getPerso()::sendSmiley, Smiley.getRandomTrollSmiley(), Randoms.nextBool()))
						.thenComposeAsync(i -> down().harvest(available.cellToHarvest, available.skill).exceptionally(t -> {
							LOGGER.error("Unable to harvest <> recover::ok");
							return null;
						}))
						.thenComposeAsync(i -> harvestMap(zone))));
		return promise.thenCompose(Function.identity());
	}

	/**
	 * Attempt to join the given map
	 * 
	 * @param map
	 *            the destination
	 * @return an empty CompletableFuture
	 */
	public CompletableFuture<Void> joinMap(BotMap map) {
		Threads.uSleep(2, TimeUnit.SECONDS);
		LOGGER.debug("joinmap");
		return CompletableFuture.supplyAsync(() -> getPerso().getMapUtilities().searchPath(map))
				.thenComposeAsync(this::loopMap)
				.thenComposeAsync(Matcher.<Boolean, CompletableFuture<Void>>matcher(
						when(b -> {
							LOGGER.debug("value = " + b);
							return b;
						}, CompletableFuture.completedFuture(null)),
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
		LOGGER.debug("loopmap");
		if (path.isEmpty()) {
			LOGGER.debug("empty return true !");
			return CompletableFuture.completedFuture(true);
		}
		return CompletableFuture.supplyAsync(path.peek().getDest()::getMapId)
				.thenComposeAsync(i -> CompletableFuture.runAsync(() -> getPerso().getUtilities().setNextMapId(i)))
				.thenComposeAsync(i -> CompletableFuture.supplyAsync(path::poll))
				.thenComposeAsync(Matcher.<TeleporterTrigger, CompletableFuture<Boolean>>matcher(
						when(Objects::isNull, CompletableFuture.completedFuture(null)),
						when(t -> !getPerso().getUtilities().isOnPath(), CompletableFuture.completedFuture(false)),
						when(TeleporterTrigger::isZaap, trg -> CompletableFuture.completedFuture(trg.getCellId()).thenComposeAsync(down()::openZaap).thenApplyAsync(t -> true)),
						when(TeleporterTrigger::isZaapi, trg -> CompletableFuture.completedFuture(trg.getCellId()).thenComposeAsync(down()::openZaapi).thenApplyAsync(t -> true)),
						when(TeleporterTrigger::isTeleporter, trg -> CompletableFuture.runAsync(() -> down().move(trg.getCellId(), true, true)).thenApplyAsync(t -> true)),
						when(TeleporterTrigger::isPopoAstrub, trg -> CompletableFuture.completedFuture(PotionType.RAPPEL).thenComposeAsync(down()::usePopo).thenApplyAsync(t -> true)),
						when(TeleporterTrigger::isPopoBonta, trg -> CompletableFuture.completedFuture(PotionType.BONTA).thenComposeAsync(down()::usePopo).thenApplyAsync(t -> true)),
						when(TeleporterTrigger::isPopoBrak, trg -> CompletableFuture.completedFuture(PotionType.BRAKMAR).thenComposeAsync(down()::usePopo).thenApplyAsync(t -> true)),
						def(CompletableFuture.completedFuture(false)))::match)
				.thenComposeAsync(Matcher.<Boolean, CompletableFuture<Boolean>>matcher(
						when(Objects::isNull, CompletableFuture.completedFuture(true)),
						when(Boolean::booleanValue, loopMap(path)),
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
