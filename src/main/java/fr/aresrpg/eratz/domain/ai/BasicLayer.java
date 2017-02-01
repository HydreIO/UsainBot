package fr.aresrpg.eratz.domain.ai;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.event.EventBus;
import fr.aresrpg.commons.domain.event.Subscriber;
import fr.aresrpg.commons.domain.functional.TetraPredicate;
import fr.aresrpg.commons.domain.functional.TriPredicate;
import fr.aresrpg.commons.domain.functional.consumer.BiConsumer;
import fr.aresrpg.commons.domain.functional.consumer.TriConsumer;
import fr.aresrpg.commons.domain.util.Consumers;
import fr.aresrpg.dofus.structures.Skills;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.event.LayerCancellingEvent;
import fr.aresrpg.eratz.domain.util.exception.*;
import fr.aresrpg.tofumanchou.domain.data.Account;
import fr.aresrpg.tofumanchou.domain.data.enums.*;
import fr.aresrpg.tofumanchou.domain.event.ActionErrorEvent;
import fr.aresrpg.tofumanchou.domain.event.entity.EntityPaChangeEvent;
import fr.aresrpg.tofumanchou.domain.event.entity.EntityPlayerJoinMapEvent;
import fr.aresrpg.tofumanchou.domain.event.fight.FightEndEvent;
import fr.aresrpg.tofumanchou.domain.event.map.FrameUpdateEvent;
import fr.aresrpg.tofumanchou.domain.event.map.HarvestTimeReceiveEvent;
import fr.aresrpg.tofumanchou.domain.event.player.*;
import fr.aresrpg.tofumanchou.infra.data.ManchouSpell;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.*;

/**
 * 
 * @since
 */
public class BasicLayer extends Info implements Layer {

	private final Function<Account, BotPerso> toPlayer = this::retrievePlayer;
	private final TriPredicate<BotPerso, Long, CompletableFuture<?>> persoUidValidator = this::validateUid;
	private final TetraPredicate<BotPerso, Integer, Long, CompletableFuture<?>> stealValidator = this::validateSteal;
	private final TetraPredicate<BotPerso, Integer, Integer, CompletableFuture<?>> harvestValidator = this::validateHarvest;
	private final BiPredicate<BotPerso, CompletableFuture<?>> persoValidator = this::validatePerso;

	private final BiConsumer<Skills, Integer> interractAction = getPerso().getPerso()::interract;
	private final BiConsumer<ManchouSpell, Integer> launchSpellAction = getPerso().getPerso()::launchSpell;
	private final TriConsumer<Integer, Boolean, Boolean> moveAction = getPerso().getPerso()::moveToCell;
	private final Consumer<Integer> usePopoAction = getPerso().getPerso()::useAllItemsWithType;
	private final Consumer<Zaap> useZaapAction = getPerso().getPerso()::useZaap;
	private final Consumer<Zaapi> useZaapiAction = getPerso().getPerso()::useZaapi;

	private final BiFunction firstIdentity = (a, b) -> a;

	private final EventBus<PersoMoveEndEvent> moved = EventBus.getBus(PersoMoveEndEvent.class);
	private final EventBus<HarvestTimeReceiveEvent> stealed = EventBus.getBus(HarvestTimeReceiveEvent.class);
	private final EventBus<FrameUpdateEvent> harvested = EventBus.getBus(FrameUpdateEvent.class);
	private final EventBus<EntityPaChangeEvent> launched = EventBus.getBus(EntityPaChangeEvent.class);
	private final EventBus<FightEndEvent> ended = EventBus.getBus(FightEndEvent.class);
	private final EventBus<EntityPlayerJoinMapEvent> joined = EventBus.getBus(EntityPlayerJoinMapEvent.class);
	private final EventBus<ZaapGuiOpenEvent> zaapOpened = EventBus.getBus(ZaapGuiOpenEvent.class);
	private final EventBus<ZaapiGuiOpenEvent> zaapiOpened = EventBus.getBus(ZaapiGuiOpenEvent.class);

	// success
	private Subscriber<PersoMoveEndEvent> moving;
	private Subscriber<FrameUpdateEvent> harvesting;
	private Subscriber<EntityPaChangeEvent> spellLaunching;
	private Subscriber<EntityPlayerJoinMapEvent> mapJoining;
	private Subscriber<ZaapGuiOpenEvent> zaapOpening;
	private Subscriber<ZaapiGuiOpenEvent> zaapiOpening;

	// failing
	private Subscriber<ActionErrorEvent> failing;
	private Subscriber<LayerCancellingEvent> cancelling;

	// other
	private Subscriber<HarvestTimeReceiveEvent> harvestStealing;
	private Subscriber<FightEndEvent> fightEnding;

	public BasicLayer(BotPerso perso) {
		super(perso);
	}

	public void setFightEnding(Subscriber<FightEndEvent> fightEnding) {
		this.fightEnding = fightEnding;
	}

	public void setHarvestStealing(Subscriber<HarvestTimeReceiveEvent> harvestStealing) {
		this.harvestStealing = harvestStealing;
	}

	public void setFailing(Subscriber<ActionErrorEvent> failing) {
		this.failing = failing;
	}

	public void setZaapiOpening(Subscriber<ZaapiGuiOpenEvent> zaapiOpening) {
		this.zaapiOpening = zaapiOpening;
	}

	public void setZaapOpening(Subscriber<ZaapGuiOpenEvent> zaapOpening) {
		this.zaapOpening = zaapOpening;
	}

	public void setCancelling(Subscriber<LayerCancellingEvent> cancelling) {
		this.cancelling = cancelling;
	}

	public void setMapJoining(Subscriber<EntityPlayerJoinMapEvent> mapJoining) {
		this.mapJoining = mapJoining;
	}

	public void setSpellLaunching(Subscriber<EntityPaChangeEvent> spellLaunching) {
		this.spellLaunching = spellLaunching;
	}

	public void setMoving(Subscriber<PersoMoveEndEvent> moving) {
		this.moving = moving;
	}

	public void setHarvesting(Subscriber<FrameUpdateEvent> harvesting) {
		this.harvesting = harvesting;
	}

	public CompletableFuture<Void> useZaap(int mapId) {
		return listenMapJoinResponse().thenCombineAsync(CompletableFuture.runAsync(Consumers.executeNative(useZaapAction, Zaap.getWithMap(mapId))::toNothing), firstIdentity);
	}

	public CompletableFuture<Void> useZaapi(int mapId) {
		return listenMapJoinResponse().thenCombineAsync(CompletableFuture.runAsync(Consumers.executeNative(useZaapiAction, Zaapi.getWithMap(mapId))::toNothing), firstIdentity);
	}

	public CompletableFuture<Void> openZaap(int cell) {
		return listenZaapOpenResponse().thenCombineAsync(CompletableFuture.runAsync(Consumers.executeCommon(Consumers.from(interractAction, Skills.UTILISER), cell)::toNothing), firstIdentity);
	}

	public CompletableFuture<Void> openZaapi(int cell) {
		return listenZaapiOpenResponse().thenCombineAsync(CompletableFuture.runAsync(Consumers.executeCommon(Consumers.from(interractAction, Skills.SE_FAIRE_TRANSPORTER), cell)::toNothing),
				firstIdentity);
	}

	public CompletableFuture<Void> usePopo(PotionType type) {
		return listenMapJoinResponse().thenCombineAsync(CompletableFuture.runAsync(Consumers.executeNative(usePopoAction, type.getItemType())::toNothing), firstIdentity);
	}

	public CompletableFuture<Void> launchSpell(ManchouSpell spell, int cell) {
		Objects.requireNonNull(spell, "The spell is null !");
		return listenSpellResponse().thenCombineAsync(CompletableFuture.runAsync(Consumers.executeCommon(Consumers.from(launchSpellAction, spell), cell)::toNothing), firstIdentity);
	}

	public CompletableFuture<Void> endTurn() {
		return CompletableFuture.runAsync(getPerso().getPerso()::endTurn);
	}

	public CompletableFuture<Void> harvest(int cell, Skills skill) {
		return listenHarvestResponse().thenCombineAsync(CompletableFuture.runAsync(Consumers.executeCommon(Consumers.from(interractAction, skill), cell)::toNothing), firstIdentity);
	}

	public CompletableFuture<Long> move(int cellTo, boolean diagonals, boolean avoidMobs) {
		LOGGER.debug("calling move");
		if (getPerso().getPerso().getCellId() == cellTo) return CompletableFuture.completedFuture(0L);
		return listenMoveResponse().thenCombineAsync(CompletableFuture.runAsync(Consumers.executeCommon(Consumers.from(moveAction, cellTo, diagonals), avoidMobs)::toNothing), firstIdentity);
	}

	public CompletableFuture<Void> waitTime(long time, TimeUnit unit) {
		if (time == 0) return CompletableFuture.completedFuture(null);
		return CompletableFuture.runAsync(() -> Threads.uSleep(time, unit));
	}

	private CompletableFuture<Void> listenSpellResponse() {
		LOGGER.debug("listenSpellResponse");
		CompletableFuture<Void> promise = new CompletableFuture<>();
		setCancelling(Layers.cancelling(getPerso(), new CancellationException("Manually canceled"), promise));
		setFailing(Layers.failing(getPerso(), new HarvestActionError(), promise));
		setFightEnding(ended.subscribe(
				event -> Consumers.execute(promise::obtrudeException, new NotInFightException("Unable to launch spell"), persoValidator.test(toPlayer.apply(event.getClient()), promise))));
		setSpellLaunching(launched.subscribe(event -> Consumers.execute(promise::complete, null, persoUidValidator.test(toPlayer.apply(event.getClient()), event.getEntity().getUUID(), promise))));
		return promise.thenCompose(v -> {
			Consumers.execute(ended::unsubscribe, fightEnding)
					.then(Layers.CANCELLING::unsubscribe, cancelling)
					.then(Layers.FAILING::unsubscribe, failing)
					.then(launched::unsubscribe, spellLaunching);
			return CompletableFuture.completedFuture(null);
		});
	}

	private CompletableFuture<Void> listenHarvestResponse() {
		LOGGER.debug("listenHarvestResponse");
		CompletableFuture<Void> promise = new CompletableFuture<>();
		setCancelling(Layers.cancelling(getPerso(), new CancellationException("Manually canceled"), promise));
		setFailing(Layers.failing(getPerso(), new HarvestActionError(), promise));

		setHarvestStealing(stealed.subscribe(event -> Consumers.execute(
				promise::obtrudeException,
				new RessourceStealedException(getPerso().getCellAt(event.getCellId()).getInterractable(), event.getPlayer()),
				stealValidator.test(toPlayer.apply(event.getClient()), event.getCellId(), event.getPlayer().getUUID(), promise))));

		setHarvesting(harvested.subscribe(event -> Consumers.execute(
				promise::complete,
				null,
				harvestValidator.test(toPlayer.apply(event.getClient()), event.getCell().getId(), event.getFrame(), promise))));

		return promise.thenCompose(v -> {
			LOGGER.debug("harvest promise complete");
			Consumers.execute(
					stealed::unsubscribe, harvestStealing)
					.then(Layers.CANCELLING::unsubscribe, cancelling)
					.then(Layers.FAILING::unsubscribe, failing)
					.then(harvested::unsubscribe, harvesting);
			return CompletableFuture.completedFuture(null);
		});
	}

	private CompletableFuture<Void> listenMoveResponse() {
		LOGGER.debug("listenMoveResponse");
		CompletableFuture<Void> promise = new CompletableFuture<>();
		setCancelling(Layers.cancelling(getPerso(), new CancellationException("Manually canceled"), promise));
		setFailing(Layers.failing(getPerso(), new MoveActionError(), promise));
		this.moving = moved.subscribe(event -> Consumers.execute(promise::complete, null, persoValidator.test(toPlayer.apply(event.getClient()), promise)));
		return promise.thenCompose(v -> {
			LOGGER.debug("move promise completed");
			Consumers.execute(moved::unsubscribe, moving)
					.then(Layers.CANCELLING::unsubscribe, cancelling)
					.then(Layers.FAILING::unsubscribe, failing);
			return CompletableFuture.completedFuture(null);
		});
	}

	public CompletableFuture<Void> listenMapJoinResponse() {
		LOGGER.debug("listenMapJoinResponse");
		CompletableFuture<Void> promise = new CompletableFuture<>();
		setCancelling(Layers.cancelling(getPerso(), new CancellationException("Manually canceled"), promise));
		setFailing(Layers.failing(getPerso(), new MoveActionError(), promise));
		setMapJoining(joined.subscribe(event -> Consumers.execute(promise::complete, null, persoValidator.test(toPlayer.apply(event.getClient()), promise))));
		return promise.thenCompose(v -> {
			Consumers.execute(joined::unsubscribe, mapJoining)
					.then(Layers.CANCELLING::unsubscribe, cancelling)
					.then(Layers.FAILING::unsubscribe, failing);
			return CompletableFuture.completedFuture(null);
		});
	}

	private CompletableFuture<Void> listenZaapOpenResponse() {
		CompletableFuture<Void> promise = new CompletableFuture<>();
		setCancelling(Layers.cancelling(getPerso(), new CancellationException("Manually canceled"), promise));
		setFailing(Layers.failing(getPerso(), new MoveActionError(), promise));
		setZaapOpening(zaapOpened.subscribe(event -> Consumers.execute(promise::complete, null, persoValidator.test(toPlayer.apply(event.getClient()), promise))));
		return promise.thenCompose(v -> {
			Consumers.execute(zaapOpened::unsubscribe, zaapOpening)
					.then(Layers.CANCELLING::unsubscribe, cancelling)
					.then(Layers.FAILING::unsubscribe, failing);
			return CompletableFuture.completedFuture(null);
		});
	}

	private CompletableFuture<Void> listenZaapiOpenResponse() {
		CompletableFuture<Void> promise = new CompletableFuture<>();
		setCancelling(Layers.cancelling(getPerso(), new CancellationException("Manually canceled"), promise));
		setFailing(Layers.failing(getPerso(), new MoveActionError(), promise));
		setZaapiOpening(zaapiOpened.subscribe(event -> Consumers.execute(promise::complete, null, persoValidator.test(toPlayer.apply(event.getClient()), promise))));
		return promise.thenCompose(v -> {
			Consumers.execute(zaapiOpened::unsubscribe, zaapiOpening)
					.then(Layers.CANCELLING::unsubscribe, cancelling)
					.then(Layers.FAILING::unsubscribe, failing);
			return CompletableFuture.completedFuture(null);
		});
	}

	// logic ======================

	protected boolean validateUid(BotPerso perso, long uuid, CompletableFuture<?> promise) {
		return perso == getPerso() && uuid == getPerso().getPerso().getUUID() && !promise.isDone();
	}

	protected boolean validateSteal(BotPerso perso, Integer cell, Long uuid, CompletableFuture<?> promise) {
		return perso == getPerso() && cell == getPerso().getUtilities().getCurrentHarvest() && uuid != getPerso().getPerso().getUUID() && !promise.isDone();
	}

	protected boolean validateHarvest(BotPerso perso, Integer cell, Integer frame, CompletableFuture<?> promise) {
		boolean bool = perso == getPerso() && cell == getPerso().getUtilities().getCurrentHarvest() && frame == 3 && !promise.isDone();
		LOGGER.debug("validateHarvest ? = " + bool);
		return bool;
	}

	protected boolean validatePerso(BotPerso perso, CompletableFuture<?> promise) {
		return perso == getPerso() && !promise.isDone();
	}

	protected BotPerso retrievePlayer(Account client) {
		return BotFather.getPerso(client);
	}

	@Override
	public void shutdown() {
	}

	@Override
	public ActionLayer up() {
		return getPerso().getLayers().actionLayer;
	}

	@Override
	public Layer down() {
		throw new IllegalAccessError("No more layers");
	}

}
