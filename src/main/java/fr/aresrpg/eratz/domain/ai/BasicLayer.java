package fr.aresrpg.eratz.domain.ai;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.event.EventBus;
import fr.aresrpg.commons.domain.event.Subscriber;
import fr.aresrpg.commons.domain.functional.*;
import fr.aresrpg.commons.domain.functional.consumer.BiConsumer;
import fr.aresrpg.commons.domain.functional.consumer.TriConsumer;
import fr.aresrpg.commons.domain.util.Consumers;
import fr.aresrpg.dofus.protocol.exchange.client.ExchangeMoveItemsPacket.MovedItem;
import fr.aresrpg.dofus.protocol.exchange.client.ExchangeRequestPacket;
import fr.aresrpg.dofus.protocol.tutorial.client.TutorialQuitPacket;
import fr.aresrpg.dofus.structures.*;
import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.util.exception.*;
import fr.aresrpg.tofumanchou.domain.data.Account;
import fr.aresrpg.tofumanchou.domain.data.enums.*;
import fr.aresrpg.tofumanchou.domain.event.TutorialCreatedEvent;
import fr.aresrpg.tofumanchou.domain.event.entity.EntityPaChangeEvent;
import fr.aresrpg.tofumanchou.domain.event.entity.EntityPlayerJoinMapEvent;
import fr.aresrpg.tofumanchou.domain.event.exchange.*;
import fr.aresrpg.tofumanchou.domain.event.fight.FightEndEvent;
import fr.aresrpg.tofumanchou.domain.event.map.FrameUpdateEvent;
import fr.aresrpg.tofumanchou.domain.event.map.HarvestTimeReceiveEvent;
import fr.aresrpg.tofumanchou.domain.event.player.*;
import fr.aresrpg.tofumanchou.infra.data.ManchouSpell;

import java.util.*;
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
	private final TriPredicate<BotPerso, Exchange, CompletableFuture<?>> bankOpeningValidator = this::validateBankOpening;
	private final BiPredicate<ExchangeCreateEvent, CompletableFuture<?>> hdvOpeningValidator = this::validateHdvOpening;

	private final BiConsumer<Skills, Integer> interractAction = getPerso().getPerso()::interract;
	private final BiConsumer<ManchouSpell, Integer> launchSpellAction = getPerso().getPerso()::launchSpell;
	private final TriConsumer<Integer, Boolean, Boolean> moveAction = getPerso().getPerso()::moveToCell;
	private final Consumer<Integer> usePopoAction = getPerso().getPerso()::useAllItemsWithType;
	private final Consumer<Zaap> useZaapAction = getPerso().getPerso()::useZaap;
	private final Consumer<Zaapi> useZaapiAction = getPerso().getPerso()::useZaapi;
	private final Consumer<MovedItem> moveItem = getPerso().getPerso()::moveItem;
	private final Consumer<Long> moveKamas = getPerso().getPerso()::moveKama;
	private final Consumer<List<Item>> updateHdv = getPerso().getUtilities()::updateHdv;
	private final Executable openBank = () -> {
		getPerso().getPerso().speakToNpc(-2);
		getPerso().getPerso().npcTalkChoice(318, 259);
	};
	private final Executable playTofuSmash = () -> {
		getPerso().getPerso().speakToNpc(-1);
		getPerso().getPerso().npcTalkChoice(1677, 1297);
	};
	private final Executable winTofuSmash = () -> {
		TutorialQuitPacket pkt = new TutorialQuitPacket();
		pkt.setCellid(284);
		pkt.setOrientation(Orientation.UP_LEFT);
		pkt.setSuccessId(2);
		getPerso().getPerso().sendPacketToServer(pkt);
	};
	private final Consumer<Long> openHdv = i -> {
		getPerso().sendPacketToServer(new ExchangeRequestPacket(Exchange.BIG_STORE_BUY, i, -1000));
	};

	private final BiFunction firstIdentity = (a, b) -> a;

	private final EventBus<PersoMoveEndEvent> moved = EventBus.getBus(PersoMoveEndEvent.class);
	private final EventBus<HarvestTimeReceiveEvent> stealed = EventBus.getBus(HarvestTimeReceiveEvent.class);
	private final EventBus<FrameUpdateEvent> harvested = EventBus.getBus(FrameUpdateEvent.class);
	private final EventBus<EntityPaChangeEvent> launched = EventBus.getBus(EntityPaChangeEvent.class);
	private final EventBus<FightEndEvent> ended = EventBus.getBus(FightEndEvent.class);
	private final EventBus<EntityPlayerJoinMapEvent> joined = EventBus.getBus(EntityPlayerJoinMapEvent.class);
	private final EventBus<ZaapGuiOpenEvent> zaapOpened = EventBus.getBus(ZaapGuiOpenEvent.class);
	private final EventBus<ZaapiGuiOpenEvent> zaapiOpened = EventBus.getBus(ZaapiGuiOpenEvent.class);
	private final EventBus<ExchangeStorageMoveEvent> itemMoved = EventBus.getBus(ExchangeStorageMoveEvent.class);
	private final EventBus<ExchangeListEvent> exchangeList = EventBus.getBus(ExchangeListEvent.class);
	private final EventBus<TutorialCreatedEvent> tutoCreated = EventBus.getBus(TutorialCreatedEvent.class);
	private final EventBus<ExchangeCreateEvent> exchangeCreated = EventBus.getBus(ExchangeCreateEvent.class);

	private List<Subscriber> subscribers = new ArrayList<>();

	public BasicLayer(BotPerso perso) {
		super(perso);
	}

	void subscribe(Subscriber s) {
		subscribers.add(s);
	}

	public CompletableFuture<Void> openHdv(long npc) {
		return listenHdvOpenResponse().thenCombineAsync(CompletableFuture.runAsync(() -> openHdv.accept(npc)), firstIdentity);
	}

	public CompletableFuture<Void> winTofuSmash() {
		return listenMapJoinResponse().thenCombineAsync(CompletableFuture.runAsync(winTofuSmash::execute), firstIdentity);
	}

	public CompletableFuture<Void> playTofuSmash() {
		return listenTutoCreateResponse().thenCombineAsync(CompletableFuture.runAsync(playTofuSmash::execute), firstIdentity);
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

	public CompletableFuture<Long> move(int cellTo, boolean diagonals, boolean avoidMobs, boolean waitingMapJoin) {
		LOGGER.debug("calling move");
		if (getPerso().getPerso().getCellId() == cellTo) return CompletableFuture.completedFuture(0L);
		CompletableFuture<Void> listen = waitingMapJoin ? listenMapJoinResponse() : listenMoveResponse();
		return listen.thenCombineAsync(CompletableFuture.runAsync(Consumers.executeCommon(Consumers.from(moveAction, cellTo, diagonals), avoidMobs)::toNothing), firstIdentity);
	}

	public CompletableFuture<Void> moveItem(long uid, int amount) {
		return listenExchangeMoveResponse().thenCombineAsync(CompletableFuture.runAsync(Consumers.executeNative(moveItem, new MovedItem(ExchangeMove.ADD, uid, amount))::toNothing), firstIdentity);
	}

	public CompletableFuture<Void> sellItem(long uid, int amount, long price) {
		return listenExchangeMoveResponse().thenCombineAsync(CompletableFuture.runAsync(Consumers.executeNative(moveItem, new MovedItem(ExchangeMove.ADD, uid, amount, price))::toNothing),
				firstIdentity);
	}

	public CompletableFuture<Void> retrieveItem(long uid, int amount) {
		return listenExchangeMoveResponse().thenCombineAsync(CompletableFuture.runAsync(Consumers.executeNative(moveItem, new MovedItem(ExchangeMove.REMOVE, uid, amount))::toNothing), firstIdentity);
	}

	public CompletableFuture<Void> moveKamas(long amount) {
		return listenExchangeMoveResponse().thenCombineAsync(CompletableFuture.runAsync(Consumers.executeNative(moveKamas, amount)::toNothing), firstIdentity);
	}

	public CompletableFuture<Void> openBank() {
		LOGGER.debug("openning bank !");
		return listenExchangeListResponse().thenCombineAsync(CompletableFuture.runAsync(openBank::execute), firstIdentity);
	}

	public CompletableFuture<Void> waitTime(long time, TimeUnit unit) {
		if (time == 0) return CompletableFuture.completedFuture(null);
		return CompletableFuture.runAsync(() -> Threads.uSleep(time, unit));
	}

	private CompletableFuture<Void> listenTutoCreateResponse() {
		LOGGER.debug("listenTutoCreateResponse");
		CompletableFuture<Void> promise = new CompletableFuture<>();
		subscribe(Layers.cancelling(getPerso(), new CancellationException("Manually canceled"), promise));
		subscribe(tutoCreated.subscribe(event -> Consumers.execute(promise::complete, null, toPlayer.apply(event.getClient()).equals(getPerso()))));
		return promise.thenCompose(v -> {
			LOGGER.debug("tutocreated promise completed");
			subscribers.forEach(EventBus::unsubscribing);
			return CompletableFuture.completedFuture(null);
		});
	}

	private CompletableFuture<Void> listenExchangeListResponse() {
		LOGGER.debug("listenExchangeListResponse");
		CompletableFuture<Void> promise = new CompletableFuture<>();
		subscribe(Layers.cancelling(getPerso(), new CancellationException("Manually canceled"), promise));
		subscribe(exchangeList
				.subscribe(event -> Consumers.execute(promise::complete, null, bankOpeningValidator.test(toPlayer.apply(event.getClient()), event.getInvType(), promise))));
		return promise.thenCompose(v -> {
			LOGGER.debug("exchangelist promise completed");
			subscribers.forEach(EventBus::unsubscribing);
			return CompletableFuture.completedFuture(null);
		});
	}

	private CompletableFuture<Void> listenExchangeMoveResponse() {
		LOGGER.debug("listenExchangeMoveResponse");
		CompletableFuture<Void> promise = new CompletableFuture<>();
		subscribe(Layers.cancelling(getPerso(), new CancellationException("Manually canceled"), promise));
		subscribe(itemMoved.subscribe(event -> Consumers.execute(promise::complete, null, toPlayer.apply(event.getClient()).equals(getPerso()))));
		return promise.thenCompose(v -> {
			LOGGER.debug("exchange promise completed");
			subscribers.forEach(EventBus::unsubscribing);
			return CompletableFuture.completedFuture(null);
		});
	}

	private CompletableFuture<Void> listenSpellResponse() {
		LOGGER.debug("listenSpellResponse");
		CompletableFuture<Void> promise = new CompletableFuture<>();
		subscribe(Layers.cancelling(getPerso(), new CancellationException("Manually canceled"), promise));
		subscribe(Layers.failing(getPerso(), new HarvestActionError(), promise));
		subscribe(ended.subscribe(
				event -> Consumers.execute(promise::obtrudeException, new NotInFightException("Unable to launch spell"), persoValidator.test(toPlayer.apply(event.getClient()), promise))));
		subscribe(launched.subscribe(event -> Consumers.execute(promise::complete, null, persoUidValidator.test(toPlayer.apply(event.getClient()), event.getEntity().getUUID(), promise))));
		return promise.thenCompose(v -> {
			LOGGER.debug("spell promise completed");
			subscribers.forEach(EventBus::unsubscribing);
			return CompletableFuture.completedFuture(null);
		});
	}

	private CompletableFuture<Void> listenHarvestResponse() {
		LOGGER.debug("listenHarvestResponse");
		CompletableFuture<Void> promise = new CompletableFuture<>();
		subscribe(Layers.cancelling(getPerso(), new CancellationException("Manually canceled"), promise));
		subscribe(Layers.failing(getPerso(), new HarvestActionError(), promise));

		subscribe(stealed.subscribe(event -> Consumers.execute(
				promise::obtrudeException,
				new RessourceStealedException(getPerso().getCellAt(event.getCellId()).getInterractable(), event.getPlayer()),
				stealValidator.test(toPlayer.apply(event.getClient()), event.getCellId(), event.getPlayer().getUUID(), promise))));

		subscribe(harvested.subscribe(event -> Consumers.execute(
				promise::complete,
				null,
				harvestValidator.test(toPlayer.apply(event.getClient()), event.getCell().getId(), event.getFrame(), promise))));

		return promise.thenCompose(v -> {
			LOGGER.debug("harvest promise completed");
			subscribers.forEach(EventBus::unsubscribing);
			return CompletableFuture.completedFuture(null);
		});
	}

	private CompletableFuture<Void> listenMoveResponse() {
		LOGGER.debug("listenMoveResponse");
		CompletableFuture<Void> promise = new CompletableFuture<>();
		subscribe(Layers.cancelling(getPerso(), new CancellationException("Manually canceled"), promise));
		subscribe(Layers.failing(getPerso(), new MoveActionError(), promise));
		subscribe(moved.subscribe(event -> Consumers.execute(promise::complete, null, persoValidator.test(toPlayer.apply(event.getClient()), promise))));
		return promise.thenCompose(v -> {
			LOGGER.debug("move promise completed");
			subscribers.forEach(EventBus::unsubscribing);
			return CompletableFuture.completedFuture(null);
		});
	}

	public CompletableFuture<Void> listenMapJoinResponse() {
		LOGGER.debug("listenMapJoinResponse");
		CompletableFuture<Void> promise = new CompletableFuture<>();
		subscribe(Layers.cancelling(getPerso(), new CancellationException("Manually canceled"), promise));
		subscribe(Layers.failing(getPerso(), new MoveActionError(), promise));
		subscribe(joined.subscribe(event -> Consumers.execute(promise::complete, null, persoValidator.test(toPlayer.apply(event.getClient()), promise))));
		return promise.thenCompose(v -> {
			LOGGER.debug("mapjoin promise completed");
			subscribers.forEach(EventBus::unsubscribing);
			return CompletableFuture.completedFuture(null);
		});
	}

	private CompletableFuture<Void> listenZaapOpenResponse() {
		CompletableFuture<Void> promise = new CompletableFuture<>();
		subscribe(Layers.cancelling(getPerso(), new CancellationException("Manually canceled"), promise));
		subscribe(Layers.failing(getPerso(), new MoveActionError(), promise));
		subscribe(zaapOpened.subscribe(event -> Consumers.execute(promise::complete, null, persoValidator.test(toPlayer.apply(event.getClient()), promise))));
		return promise.thenCompose(v -> {
			LOGGER.debug("zaap open promise completed");
			subscribers.forEach(EventBus::unsubscribing);
			return CompletableFuture.completedFuture(null);
		});
	}

	private CompletableFuture<Void> listenZaapiOpenResponse() {
		CompletableFuture<Void> promise = new CompletableFuture<>();
		subscribe(Layers.cancelling(getPerso(), new CancellationException("Manually canceled"), promise));
		subscribe(Layers.failing(getPerso(), new MoveActionError(), promise));
		subscribe(zaapiOpened.subscribe(event -> Consumers.execute(promise::complete, null, persoValidator.test(toPlayer.apply(event.getClient()), promise))));
		return promise.thenCompose(v -> {
			LOGGER.debug("zaapi open promise completed");
			subscribers.forEach(EventBus::unsubscribing);
			return CompletableFuture.completedFuture(null);
		});
	}

	public CompletableFuture<Void> listenHdvOpenResponse() {
		LOGGER.debug("listenHdvOpenning");
		CompletableFuture<Void> promise = new CompletableFuture<>();
		subscribe(Layers.cancelling(getPerso(), new CancellationException("Manually canceled"), promise));
		subscribe(exchangeCreated.subscribe(event -> Consumers.execute(promise::complete, null, hdvOpeningValidator.test(event, promise))));
		return promise.thenCompose(v -> {
			LOGGER.debug("hdvopen promise completed");
			subscribers.forEach(EventBus::unsubscribing);
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

	protected boolean validateBankOpening(BotPerso perso, Exchange type, CompletableFuture<?> promise) {
		return perso == getPerso() && type == Exchange.BANK && !promise.isDone();
	}

	protected boolean validateHdvOpening(ExchangeCreateEvent event, CompletableFuture<?> promise) {
		return toPlayer.apply(event.getClient()) == getPerso() && event.getType() == Exchange.BIG_STORE_BUY && !promise.isDone();
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
