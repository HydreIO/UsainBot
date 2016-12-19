/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.io.handler;

import fr.aresrpg.commons.domain.condition.Option;
import fr.aresrpg.commons.domain.log.Logger;
import fr.aresrpg.commons.domain.log.LoggerBuilder;
import fr.aresrpg.dofus.protocol.*;
import fr.aresrpg.dofus.protocol.account.AccountKeyPacket;
import fr.aresrpg.dofus.protocol.account.AccountRegionalVersionPacket;
import fr.aresrpg.dofus.protocol.account.server.*;
import fr.aresrpg.dofus.protocol.basic.server.BasicConfirmPacket;
import fr.aresrpg.dofus.protocol.chat.ChatSubscribeChannelPacket;
import fr.aresrpg.dofus.protocol.dialog.server.*;
import fr.aresrpg.dofus.protocol.exchange.client.ExchangeRequestPacket;
import fr.aresrpg.dofus.protocol.exchange.server.*;
import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction;
import fr.aresrpg.dofus.protocol.game.actions.server.*;
import fr.aresrpg.dofus.protocol.game.movement.*;
import fr.aresrpg.dofus.protocol.game.server.*;
import fr.aresrpg.dofus.protocol.guild.server.GuildStatPacket;
import fr.aresrpg.dofus.protocol.hello.server.HelloConnectionPacket;
import fr.aresrpg.dofus.protocol.hello.server.HelloGamePacket;
import fr.aresrpg.dofus.protocol.info.server.message.InfoMessagePacket;
import fr.aresrpg.dofus.protocol.item.server.*;
import fr.aresrpg.dofus.protocol.job.server.*;
import fr.aresrpg.dofus.protocol.mount.server.MountXpPacket;
import fr.aresrpg.dofus.protocol.party.PartyAcceptPacket;
import fr.aresrpg.dofus.protocol.party.PartyRefusePacket;
import fr.aresrpg.dofus.protocol.party.server.*;
import fr.aresrpg.dofus.protocol.specialization.server.SpecializationSetPacket;
import fr.aresrpg.dofus.protocol.spell.server.SpellChangeOptionPacket;
import fr.aresrpg.dofus.protocol.spell.server.SpellListPacket;
import fr.aresrpg.dofus.protocol.subarea.server.SubareaListPacket;
import fr.aresrpg.dofus.protocol.waypoint.ZaapLeavePacket;
import fr.aresrpg.dofus.protocol.waypoint.server.ZaapCreatePacket;
import fr.aresrpg.dofus.protocol.waypoint.server.ZaapUseErrorPacket;
import fr.aresrpg.dofus.structures.game.GameMovementType;
import fr.aresrpg.dofus.structures.map.*;
import fr.aresrpg.dofus.util.Maps;
import fr.aresrpg.dofus.util.SwfVariableExtractor;
import fr.aresrpg.eratz.domain.data.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.data.dofus.map.BotMap;
import fr.aresrpg.eratz.domain.data.dofus.ressource.Interractable;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.object.Ressource;
import fr.aresrpg.eratz.domain.io.handler.std.aproach.AccountServerHandler;
import fr.aresrpg.eratz.domain.io.handler.std.area.SubareaServerHandler;
import fr.aresrpg.eratz.domain.io.handler.std.chat.ChatServerHandler;
import fr.aresrpg.eratz.domain.io.handler.std.craft.CraftHandler;
import fr.aresrpg.eratz.domain.io.handler.std.dialog.DialogServerHandler;
import fr.aresrpg.eratz.domain.io.handler.std.exchange.ExchangeServerHandler;
import fr.aresrpg.eratz.domain.io.handler.std.fight.FightHandler;
import fr.aresrpg.eratz.domain.io.handler.std.game.GameServerHandler;
import fr.aresrpg.eratz.domain.io.handler.std.game.action.GameActionServerHandler;
import fr.aresrpg.eratz.domain.io.handler.std.guild.GuildServerHandler;
import fr.aresrpg.eratz.domain.io.handler.std.info.InfoServerHandler;
import fr.aresrpg.eratz.domain.io.handler.std.mount.MountServerHandler;
import fr.aresrpg.eratz.domain.io.handler.std.specialization.SpecializationServerHandler;
import fr.aresrpg.eratz.domain.io.handler.std.spell.SpellServerHandler;
import fr.aresrpg.eratz.domain.io.handler.std.zaap.ZaapServerHandler;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;

/**
 * 
 * @since
 */
public class BaseServerPacketHandler implements ServerPacketHandler {

	private static final Logger logger = new LoggerBuilder("Game").setUseConsoleHandler(true, true, Option.none(), Option.none()).build();
	private Perso perso;
	private Set<FightServerHandler> fightHandler = new HashSet<>();
	private Set<CraftServerHandler> craftHandler = new HashSet<>();
	private Set<MapServerHandler> mapHandler = new HashSet<>();
	private Set<AccountServerHandler> accountHandler = new HashSet<>();
	private Set<ChatServerHandler> chatHandler = new HashSet<>();
	private Set<ExchangeServerHandler> exchangeHandler = new HashSet<>();
	private Set<ZaapServerHandler> zaapHandler = new HashSet<>();
	private Set<GuildServerHandler> guildHandler = new HashSet<>();
	private Set<InfoServerHandler> infoHandler = new HashSet<>();
	private Set<MountServerHandler> mountHandler = new HashSet<>();
	private Set<SpecializationServerHandler> specializationHandler = new HashSet<>();
	private Set<SpellServerHandler> spellServerHandler = new HashSet<>();
	private Set<SubareaServerHandler> subareaServerHandler = new HashSet<>();
	private Set<GameActionServerHandler> gameActionHandler = new HashSet<>();
	private Set<GameServerHandler> gameHandler = new HashSet<>();
	private Set<DialogServerHandler> dialogHandler = new HashSet<>();

	public void addDialogHandlers(DialogServerHandler... handlers) {
		Arrays.stream(handlers).forEach(dialogHandler::add);
	}

	public void addGameActionHandlers(GameActionServerHandler... handlers) {
		Arrays.stream(handlers).forEach(gameActionHandler::add);
	}

	public void addGameHandlers(GameServerHandler... handlers) {
		Arrays.stream(handlers).forEach(gameHandler::add);
	}

	public void addSpellHandlers(SubareaServerHandler... handlers) {
		Arrays.stream(handlers).forEach(subareaServerHandler::add);
	}

	public void addSpellHandlers(SpellServerHandler... handlers) {
		Arrays.stream(handlers).forEach(spellServerHandler::add);
	}

	public void addSpecializationHandlers(SpecializationServerHandler... handlers) {
		Arrays.stream(handlers).forEach(specializationHandler::add);
	}

	public void addMountHandlers(MountServerHandler... handlers) {
		Arrays.stream(handlers).forEach(mountHandler::add);
	}

	public void addInfoHandlers(InfoServerHandler... handlers) {
		Arrays.stream(handlers).forEach(infoHandler::add);
	}

	public void addGuildHandlers(GuildServerHandler... handlers) {
		Arrays.stream(handlers).forEach(guildHandler::add);
	}

	public void addFightHandlers(FightHandler... handlers) {
		Arrays.stream(handlers).forEach(fightHandler::add);
	}

	public void addFightHandlers(ExchangeServerHandler... handlers) {
		Arrays.stream(handlers).forEach(exchangeHandler::add);
	}

	public void addCraftHandlers(CraftHandler... handlers) {
		Arrays.stream(handlers).forEach(craftHandler::add);
	}

	public void addMapHandlers(MapHandler... handlers) {
		Arrays.stream(handlers).forEach(mapHandler::add);
	}

	public void addZaapHandlers(ZaapServerHandler... handlers) {
		Arrays.stream(handlers).forEach(zaapHandler::add);
	}

	public void addAccountHandlers(AccountServerHandler... handlers) {
		Arrays.stream(handlers).forEach(accountHandler::add);
	}

	public void addChatHandlers(ChatServerHandler... handlers) {
		Arrays.stream(handlers).forEach(chatHandler::add);
	}

	// base doit handle tout les packets correspondant au handlers custom <maphandler etc>
	// transfert doit virer
	// remote doit super.handle puis transmit pour tout les packets

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	/**
	 * @return the exchangeHandler
	 */
	public Set<ExchangeServerHandler> getExchangeHandler() {
		return exchangeHandler;
	}

	/**
	 * @return the subareaServerHandler
	 */
	public Set<SubareaServerHandler> getSubareaServerHandler() {
		return subareaServerHandler;
	}

	/**
	 * @return the dialogHandler
	 */
	public Set<DialogServerHandler> getDialogHandler() {
		return dialogHandler;
	}

	/**
	 * @return the chatHandler
	 */
	public Set<ChatServerHandler> getChatHandler() {
		return chatHandler;
	}

	/**
	 * @return the gameActionHandler
	 */
	public Set<GameActionServerHandler> getGameActionHandler() {
		return gameActionHandler;
	}

	/**
	 * @return the gameHandler
	 */
	public Set<GameServerHandler> getGameHandler() {
		return gameHandler;
	}

	/**
	 * @return the guildHandler
	 */
	public Set<GuildServerHandler> getGuildHandler() {
		return guildHandler;
	}

	/**
	 * @return the spellServerHandler
	 */
	public Set<SpellServerHandler> getSpellServerHandler() {
		return spellServerHandler;
	}

	/**
	 * @return the mountHandler
	 */
	public Set<MountServerHandler> getMountHandler() {
		return mountHandler;
	}

	/**
	 * @param perso
	 *            the perso to set
	 */
	public void setPerso(Perso perso) {
		this.perso = perso;
	}

	protected void forEachAccountHandlers(Consumer<? super AccountServerHandler> actions) {
		getAccountHandler().forEach(actions);
	}

	/**
	 * @return the specializationHandler
	 */
	public Set<SpecializationServerHandler> getSpecializationHandler() {
		return specializationHandler;
	}

	/**
	 * @return the accountHandler
	 */
	public Set<AccountServerHandler> getAccountHandler() {
		return accountHandler;
	}

	/**
	 * @return the infoHandler
	 */
	public Set<InfoServerHandler> getInfoHandler() {
		return infoHandler;
	}

	/**
	 * @return the zaapHandler
	 */
	public Set<ZaapServerHandler> getZaapHandler() {
		return zaapHandler;
	}

	protected void log(Packet pkt) {
		logger.info(pkt.toString());
	}

	@Override
	public void register(DofusConnection<?> connection) {
	}

	@Override
	public void handle(HelloConnectionPacket pkt) {
		log(pkt);
		forEachAccountHandlers(h -> h.onHelloConnection(pkt.getHashKey()));
	}

	@Override
	public void handle(HelloGamePacket pkt) {
		log(pkt);
		forEachAccountHandlers(AccountServerHandler::onHelloServer);
	}

	@Override
	public void handle(AccountKeyPacket pkt) {
		log(pkt);
		forEachAccountHandlers(h -> h.onKey(pkt.getKey(), pkt.getData()));
	}

	@Override
	public void handle(AccountRegionalVersionPacket pkt) {
		log(pkt);
		forEachAccountHandlers(AccountServerHandler::onRegion);
	}

	@Override
	public void handle(ChatSubscribeChannelPacket pkt) {
		log(pkt);
		if (pkt.isAdd()) getChatHandler().forEach(h -> h.onSubscribeChannel(pkt.getChannels()));
		else getChatHandler().forEach(h -> h.onUnsubscribre(pkt.getChannels()));
	}

	@Override
	public void handle(ZaapLeavePacket pkt) {
		log(pkt);
		getZaapHandler().forEach(ZaapServerHandler::onLeaveZaap);
	}

	@Override
	public void handle(AccountCharactersListPacket pkt) {
		log(pkt);
		forEachAccountHandlers(h -> h.onCharacterList(pkt.getSubscriptionTime(), pkt.getCharacters()));
	}

	@Override
	public void handle(AccountCommunityPacket pkt) {
		log(pkt);
		forEachAccountHandlers(h -> h.onCommunity(pkt.getCommunity()));
	}

	@Override
	public void handle(AccountHostPacket pkt) {
		log(pkt);
		forEachAccountHandlers(h -> h.onServers(pkt.getServers()));
	}

	@Override
	public void handle(AccountLoginErrPacket pkt) {
		log(pkt);
		forEachAccountHandlers(h -> h.onLoginError(pkt.getErr(), pkt.getTime(), pkt.getVersion()));
	}

	@Override
	public void handle(AccountLoginOkPacket pkt) {
		log(pkt);
		pkt.setAdmin(true);
		forEachAccountHandlers(h -> h.onLogged(pkt.isAdmin()));
	}

	@Override
	public void handle(AccountPseudoPacket pkt) {
		log(pkt);
		forEachAccountHandlers(h -> h.onPseudo(pkt.getName()));
	}

	@Override
	public void handle(AccountQuestionPacket pkt) {
		log(pkt);
		forEachAccountHandlers(h -> h.onSecretQuestion(pkt.getQuestion()));
	}

	@Override
	public void handle(AccountQueuePosition pkt) {
		log(pkt);
		forEachAccountHandlers(h -> h.onQueueRealmPosition(pkt.getPosition(), pkt.getTotalSubscriber(), pkt.getTotalNoSubscribed(), pkt.isSubscribed(), pkt.getPositionInQueue()));
	}

	@Override
	public void handle(AccountRestrictionsPacket pkt) {
		log(pkt);
	}

	@Override
	public void handle(AccountSelectCharacterOkPacket pkt) {
		log(pkt);
		forEachAccountHandlers(h -> h.onCharacterSelect(pkt.getCharacter()));
	}

	@Override
	public void handle(AccountServerEncryptedHostPacket pkt) {
		log(pkt);
		forEachAccountHandlers(h -> h.onReceiveServerHost(pkt.getIp(), pkt.getPort(), pkt.getTicketKey()));
	}

	@Override
	public void handle(AccountServerHostPacket pkt) {
		log(pkt);
		forEachAccountHandlers(h -> h.onReceiveServerHost(pkt.getIp(), pkt.getPort(), pkt.getTicketKey()));
	}

	@Override
	public void handle(AccountServerListPacket pkt) {
		log(pkt);
		forEachAccountHandlers(h -> h.onReceiveServerPersoCount(pkt.getSubscriptionDuration(), pkt.getCharacters()));
	}

	@Override
	public void handle(AccountTicketOkPacket pkt) {
		log(pkt);

	}

	@Override
	public void handle(AccountTicketPacket pkt) {
		log(pkt);

	}

	@Override
	public void handle(ExchangeCreatePacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onCreate(pkt.getType(), pkt.getData()));
	}

	@Override
	public void handle(ExchangeListPacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onInventoryList(pkt.getInvType(), pkt.getItems(), pkt.getKamas()));
	}

	@Override
	public void handle(ExchangeRequestPacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onExchangeRequest(pkt.getTargetId(), pkt.getExchange(), pkt.getCellid()));
	}

	@Override
	public void handle(BasicConfirmPacket pkt) {
		log(pkt);

	}

	@Override
	public void handle(GuildStatPacket pkt) {
		log(pkt);
		getGuildHandler().forEach(h -> h.onGuildStats(pkt.getGuild()));
	}

	@Override
	public void handle(InfoMessagePacket pkt) {
		log(pkt);
		getInfoHandler().forEach(h -> h.onInfos(pkt.getMessage(), pkt.getExtraDatas()));
	}

	@Override
	public void handle(MountXpPacket pkt) {
		log(pkt);
		getMountHandler().forEach(h -> h.onMountXp(pkt.getPercent()));
	}

	@Override
	public void handle(SpecializationSetPacket pkt) {
		log(pkt);
		getSpecializationHandler().forEach(h -> h.onSpecializationSet(pkt.getSpecialization()));
	}

	@Override
	public void handle(SpellChangeOptionPacket pkt) {
		log(pkt);
		getSpellServerHandler().forEach(h -> h.onSpellChangeOption(pkt.canUseAllSpell()));
	}

	@Override
	public void handle(SpellListPacket pkt) {
		log(pkt);
		getSpellServerHandler().forEach(h -> h.onSpellList(pkt.getSpells()));
	}

	@Override
	public void handle(SubareaListPacket pkt) {
		log(pkt);
		getSubareaServerHandler().forEach(h -> h.onSubareaList(pkt.getSubareas()));
	}

	@Override
	public void handle(ZaapCreatePacket pkt) {
		log(pkt);
		getZaapHandler().forEach(h -> h.onDiscover(pkt.getRespawnWaypoint(), pkt.getWaypoints()));
	}

	@Override
	public void handle(ZaapUseErrorPacket pkt) {
		log(pkt);
		getZaapHandler().forEach(ZaapServerHandler::onZaapError);
	}

	@Override
	public void handle(GameActionFinishPacket pkt) {
		log(pkt);
		getGameActionHandler().forEach(h -> h.onActionFinish(pkt.getAckId(), pkt.getCharacterId()));
	}

	@Override
	public void handle(GameActionStartPacket pkt) {
		log(pkt);
		getGameActionHandler().forEach(h -> h.onActionStart(pkt.getCharacterId()));
	}

	@Override
	public void handle(GameEffectPacket pkt) {
		log(pkt);
		getGameHandler().forEach(h -> h.onEffect(pkt.getEffect(), pkt.getEntities()));
	}

	@Override
	public void handle(GameEndPacket pkt) {
		log(pkt);
		getGameHandler().forEach(h -> h.onFightEnd(pkt));
	}

	@Override
	public void handle(GameFightChallengePacket pkt) {
		log(pkt);
		getGameHandler().forEach(h -> h.onFightChallenge(pkt.getChallenge()));
	}

	@Override
	public void handle(GameJoinPacket pkt) {
		log(pkt);
		getGameHandler().forEach(h -> h.onFightJoin(pkt.getState(), pkt.getFightType(), pkt.isSpectator(), pkt.getStartTimer(), pkt.isCancelButton(), pkt.isDuel()));
	}

	@Override
	public void handle(GameMapDataPacket pkt) {
		log(pkt);
		DofusMap m = Maps.loadMap(SwfVariableExtractor.extractVariable(Maps.downloadMap(pkt.getMapId(), pkt.getSubid())), pkt.getDecryptKey());
		BotMap bm = BotMap.fromDofusMap(m);
		for (Cell cell : m.getCells()) {
			if (Interractable.isInterractable(cell.getLayerObject2Num())) // add ressource
				bm.getRessources().add(new Ressource(cell, Interractable.fromId(cell.getLayerObject2Num()))); // interractable peut etre null dans le cas des zaapi porte coffre etc
		}
		getGameHandler().forEach(h -> h.onMap(bm));
	}

	@Override
	public void handle(GameMapFramePacket pkt) {
		log(pkt);
		for (Cell cell : getPerso().getMapInfos().getMap().getDofusMap().getCells())
			for (Entry<Integer, Frame> i : pkt.getFrames().entrySet())
				if (cell.getId() == i.getKey()) {
					cell.applyFrame(i.getValue());
				}
	}

	@Override
	public void handle(GameMovementPacket gameMovementPacket) {
		if (gameMovementPacket.getType() == GameMovementType.REMOVE) {
			gameMovementPacket.getActors().forEach(v -> {
				MovementRemoveActor actor = (MovementRemoveActor) (Object) v.getSecond();
				getGameHandler().forEach(h -> h.onEntityLeave(actor.getId()));
			});
			return;
		}
		gameMovementPacket.getActors().forEach(e -> {
			switch (e.getFirst()) {
				case DEFAULT:
					MovementPlayer player = (MovementPlayer) (Object) e.getSecond();
					getGameHandler().forEach(h -> h.onPlayerMove(player));
					return;
				case CREATE_INVOCATION:
					MovementInvocation invoc = (MovementInvocation) (Object) e.getSecond();
					getGameHandler().forEach(h -> h.onInvocMove(invoc));
					return;
				case CREATE_MONSTER:
					MovementMonster mob = (MovementMonster) (Object) e.getSecond();
					getGameHandler().forEach(h -> h.onMobMove(mob));
					return;
				case CREATE_MONSTER_GROUP:
					MovementMonsterGroup mobs = (MovementMonsterGroup) (Object) e.getSecond();
					getGameHandler().forEach(h -> h.onMobGroupMove(mobs));
					return;
				case CREATE_NPC:
					MovementNpc npc = (MovementNpc) (Object) e.getSecond();
					getGameHandler().forEach(h -> h.onNpcMove(npc));
					return;
				default:
					break;
			}
		});
	}

	@Override
	public void handle(GamePositionsPacket pkt) {
		log(pkt);
		getGameHandler().forEach(h -> pkt.getPositions().forEach(p -> h.onEntityFightPositionChange(p.getEntityId(), p.getPosition())));
	}

	@Override
	public void handle(GamePositionStartPacket pkt) {
		log(pkt);
		Fight f = getPerso().getFightInfos().getCurrentFight();
		f.setPlaceTeam0(pkt.getPlacesTeam0());
		f.setPlaceTeam1(pkt.getPlacesTeam1());
		getGameHandler().forEach(h -> h.onTeamAssign(pkt.getCurrentTeam()));
	}

	@Override
	public void handle(GameServerActionPacket pkt) {
		log(pkt);
		switch (pkt.getType()) {
			case ERROR:
				getGameHandler().forEach(GameServerHandler::onActionError);
				break;
			case LIFE_CHANGE:
				GameLifeChangeAction actionl = (GameLifeChangeAction) pkt.getAction();
				getGameActionHandler().forEach(h -> h.onEntityLifeChange(actionl.getEntity(), actionl.getLife()));
				break;
			case PA_CHANGE:
				GamePaChangeAction actionpa = (GamePaChangeAction) pkt.getAction();
				getGameActionHandler().forEach(h -> h.onEntityPaChange(actionpa.getEntity(), actionpa.getPa()));
				break;
			case PM_CHANGE:
				GamePmChangeAction actionpm = (GamePmChangeAction) pkt.getAction();
				getGameActionHandler().forEach(h -> h.onEntityPmChange(actionpm.getEntity(), actionpm.getPm()));
				break;
			case KILL:
				GameKillAction actionk = (GameKillAction) pkt.getAction();
				getGameActionHandler().forEach(h -> h.onEntityKilled(actionk.getKilled()));
				break;
			case SUMMON:
				GameSummonAction actions = (GameSummonAction) pkt.getAction();
				getGameActionHandler().forEach(h -> actions.getSummoned().forEach(s -> h.onEntitySummoned(s)));
				break;
			case FIGHT_JOIN_ERROR:
				GameJoinErrorAction actionj = (GameJoinErrorAction) pkt.getAction();
				getGameActionHandler().forEach(h -> h.onFightJoinError(actionj.getError()));
				break;
			case MOVE:
				GameMoveAction actionm = (GameMoveAction) pkt.getAction();
				getGameActionHandler().forEach(h -> h.onEntityMove(pkt.getEntityId(), actionm.getPath()));
				break;
			default:
				break;
		}
	}

	@Override
	public void handle(GameServerReadyPacket pkt) {
		log(pkt);
		getGameHandler().forEach(h -> h.onPlayerReadyToFight(pkt.getEntityId(), pkt.isReady()));
	}

	@Override
	public void handle(GameStartToPlayPacket pkt) {
		log(pkt);
		getGameHandler().forEach(GameServerHandler::onFightStart);
	}

	@Override
	public void handle(GameTurnFinishPacket pkt) {
		log(pkt);
		getGameHandler().forEach(h -> h.onEntityTurnEnd(pkt.getEntityId()));
	}

	@Override
	public void handle(GameTurnListPacket pkt) {
		log(pkt);
		getGameHandler().forEach(h -> h.onFightTurnInfos(pkt.getTurns()));
	}

	@Override
	public void handle(GameTurnMiddlePacket pkt) {
		log(pkt);
		getGameHandler().forEach(h -> h.onFighterInfos(pkt.getEntities()));
	}

	@Override
	public void handle(GameTurnReadyPacket pkt) {
		log(pkt);
		getGameHandler().forEach(h -> h.onEntityTurnReady(pkt.getEntityId()));
	}

	@Override
	public void handle(GameTurnStartPacket pkt) {
		log(pkt);
		getGameHandler().forEach(h -> h.onEntityTurnStart(pkt.getCharacterId(), pkt.getTime()));
	}

	@Override
	public void handle(ExchangeRequestOkPacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onExchangeRequestOk(pkt.getPlayerId(), pkt.getTargetId(), pkt.getExchange()));
	}

	@Override
	public void handle(DialogCreateOkPacket pkt) {
		log(pkt);
		getDialogHandler().forEach(h -> h.onDialogCreate(pkt.getNpcId()));
	}

	@Override
	public void handle(DialogQuestionPacket pkt) {
		log(pkt);

	}

	@Override
	public void handle(DialogPausePacket pkt) {
		log(pkt);

	}

	@Override
	public void handle(ExchangeReadyPacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onExchangeReady(pkt.getExtraData()));
	}

	@Override
	public void handle(ItemAddOkPacket pkt) {
		log(pkt);

	}

	@Override
	public void handle(ItemAddErrorPacket pkt) {
		log(pkt);

	}

	@Override
	public void handle(ItemDropErrorPacket pkt) {
		log(pkt);

	}

	@Override
	public void handle(ItemRemovePacket pkt) {
		log(pkt);

	}

	@Override
	public void handle(ItemQuantityUpdatePacket pkt) {
		log(pkt);

	}

	@Override
	public void handle(ItemMovementConfirmPacket pkt) {
		log(pkt);

	}

	@Override
	public void handle(ItemToolPacket pkt) {
		log(pkt);

	}

	@Override
	public void handle(ItemWeightPacket pkt) {
		log(pkt);

	}

	@Override
	public void handle(AccountStatsPacket pkt) {
		log(pkt);

	}

	@Override
	public void handle(AccountNewLevelPacket pkt) {
		log(pkt);

	}

	@Override
	public void handle(AccountServerQueuePacket pkt) {
		log(pkt);
	}

	@Override
	public void handle(ExchangeCraftPacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onCraft(pkt.getResult()));
	}

	@Override
	public void handle(ExchangeLocalMovePacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onLocalMove(pkt.getItemType(), pkt.getItemAmount(), pkt.getLocalKama()));
	}

	@Override
	public void handle(ExchangeDistantMovePacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onDistantMove(pkt.getMoved(), pkt.isAdd(), pkt.getKamas(), pkt.getRemainingHours()));
	}

	@Override
	public void handle(ExchangeCoopMovePacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onCoopMove(pkt.getMoved(), pkt.getKamas(), pkt.isAdd()));
	}

	@Override
	public void handle(ExchangeStorageMovePacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onStorageMove(pkt.getMoved(), pkt.getKamas(), pkt.isAdd()));
	}

	@Override
	public void handle(ExchangeShopMovePacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onShopMove(pkt.getMoved(), pkt.isAdd()));
	}

	@Override
	public void handle(ExchangeCraftPublicPacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onCraftPublic(pkt.isCraftPublicMode(), pkt.getItemid(), pkt.getMultiCraftSkill()));
	}

	@Override
	public void handle(ExchangeSellToNpcResultPacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onSellToNpc(pkt.isSuccess()));
	}

	@Override
	public void handle(ExchangeBuyToNpcResultPacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onBuyToNpc(pkt.isSuccess()));
	}

	@Override
	public void handle(ExchangeCraftLoopPacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onCraftLoop(pkt.getIndex()));
	}

	@Override
	public void handle(ExchangeCraftLoopEndPacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onCraftLoopEnd(pkt.getResult()));
	}

	@Override
	public void handle(ExchangeLeaveResultPacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onLeave(pkt.isSuccess()));
	}

	@Override
	public void handle(PartyAcceptPacket partyAcceptPacket) {
		// TODO

	}

	@Override
	public void handle(PartyRefusePacket partyRefusePacket) {
		// TODO

	}

	@Override
	public void handle(PartyInviteRequestOkPacket partyInviteRequestPacket) {
		// TODO

	}

	@Override
	public void handle(PartyInviteRequestErrorPacket partyInviteRequestErrorPacket) {
		// TODO

	}

	@Override
	public void handle(PartyLeaderPacket partyLeaderPacket) {
		// TODO

	}

	@Override
	public void handle(PartyCreateOkPacket partyCreateOkPacket) {
		// TODO

	}

	@Override
	public void handle(PartyCreateErrorPacket partyCreateErrorPacket) {
		// TODO

	}

	@Override
	public void handle(PartyPlayerLeavePacket partyPlayerLeavePacket) {
		// TODO

	}

	@Override
	public void handle(PartyFollowReceivePacket partyFollowReceivePacket) {
		// TODO

	}

	@Override
	public void handle(PartyMovementPacket partyMovementPacket) {
		// TODO

	}

	@Override
	public void handle(GameTeamPacket gameTeamPacket) {
		// TODO

	}

	@Override
	public void handle(JobSkillsPacket jobSkillsPacket) {
		// TODO

	}

	@Override
	public void handle(JobXpPacket jobXpPacket) {
		// TODO

	}

	@Override
	public void handle(JobLevelPacket jobLevelPacket) {
		// TODO

	}

}
