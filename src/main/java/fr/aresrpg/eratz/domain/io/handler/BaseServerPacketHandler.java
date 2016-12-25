/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.io.handler;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.dofus.protocol.*;
import fr.aresrpg.dofus.protocol.account.AccountKeyPacket;
import fr.aresrpg.dofus.protocol.account.AccountRegionalVersionPacket;
import fr.aresrpg.dofus.protocol.account.server.*;
import fr.aresrpg.dofus.protocol.aks.Aks0MessagePacket;
import fr.aresrpg.dofus.protocol.basic.server.BasicConfirmPacket;
import fr.aresrpg.dofus.protocol.chat.ChatSubscribeChannelPacket;
import fr.aresrpg.dofus.protocol.chat.server.ChatMessageOkPacket;
import fr.aresrpg.dofus.protocol.dialog.DialogLeavePacket;
import fr.aresrpg.dofus.protocol.dialog.server.*;
import fr.aresrpg.dofus.protocol.exchange.server.*;
import fr.aresrpg.dofus.protocol.fight.server.*;
import fr.aresrpg.dofus.protocol.friend.server.FriendListPacket;
import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction;
import fr.aresrpg.dofus.protocol.game.actions.client.GameAcceptDuelAction;
import fr.aresrpg.dofus.protocol.game.actions.client.GameRefuseDuelAction;
import fr.aresrpg.dofus.protocol.game.actions.server.*;
import fr.aresrpg.dofus.protocol.game.movement.*;
import fr.aresrpg.dofus.protocol.game.server.*;
import fr.aresrpg.dofus.protocol.guild.server.GuildStatPacket;
import fr.aresrpg.dofus.protocol.hello.server.HelloConnectionPacket;
import fr.aresrpg.dofus.protocol.hello.server.HelloGamePacket;
import fr.aresrpg.dofus.protocol.info.server.*;
import fr.aresrpg.dofus.protocol.item.server.*;
import fr.aresrpg.dofus.protocol.job.server.*;
import fr.aresrpg.dofus.protocol.mount.server.MountXpPacket;
import fr.aresrpg.dofus.protocol.party.PartyRefusePacket;
import fr.aresrpg.dofus.protocol.party.server.*;
import fr.aresrpg.dofus.protocol.specialization.server.SpecializationSetPacket;
import fr.aresrpg.dofus.protocol.spell.server.SpellChangeOptionPacket;
import fr.aresrpg.dofus.protocol.spell.server.SpellListPacket;
import fr.aresrpg.dofus.protocol.subarea.server.SubareaListPacket;
import fr.aresrpg.dofus.protocol.waypoint.ZaapLeavePacket;
import fr.aresrpg.dofus.protocol.waypoint.server.ZaapCreatePacket;
import fr.aresrpg.dofus.protocol.waypoint.server.ZaapUseErrorPacket;
import fr.aresrpg.dofus.structures.character.AvailableCharacter;
import fr.aresrpg.dofus.structures.game.*;
import fr.aresrpg.dofus.structures.map.*;
import fr.aresrpg.dofus.structures.server.DofusServer;
import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.dofus.util.Maps;
import fr.aresrpg.dofus.util.SwfVariableExtractor;
import fr.aresrpg.eratz.domain.data.AccountsManager;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.data.dofus.map.BotMap;
import fr.aresrpg.eratz.domain.data.dofus.ressource.Interractable;
import fr.aresrpg.eratz.domain.data.player.Account;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.info.StatsInfo;
import fr.aresrpg.eratz.domain.data.player.object.Ressource;
import fr.aresrpg.eratz.domain.gui.MapView;
import fr.aresrpg.eratz.domain.std.aproach.AccountServerHandler;
import fr.aresrpg.eratz.domain.std.area.SubareaServerHandler;
import fr.aresrpg.eratz.domain.std.chat.ChatServerHandler;
import fr.aresrpg.eratz.domain.std.dialog.DialogServerHandler;
import fr.aresrpg.eratz.domain.std.exchange.ExchangeServerHandler;
import fr.aresrpg.eratz.domain.std.fight.FightServerHandler;
import fr.aresrpg.eratz.domain.std.friend.FriendServerHandler;
import fr.aresrpg.eratz.domain.std.game.GameServerHandler;
import fr.aresrpg.eratz.domain.std.game.action.GameActionServerHandler;
import fr.aresrpg.eratz.domain.std.guild.GuildServerHandler;
import fr.aresrpg.eratz.domain.std.info.InfoServerHandler;
import fr.aresrpg.eratz.domain.std.item.ItemServerHandler;
import fr.aresrpg.eratz.domain.std.job.JobServerHandler;
import fr.aresrpg.eratz.domain.std.mount.MountServerHandler;
import fr.aresrpg.eratz.domain.std.party.PartyServerHandler;
import fr.aresrpg.eratz.domain.std.specialization.SpecializationServerHandler;
import fr.aresrpg.eratz.domain.std.spell.SpellServerHandler;
import fr.aresrpg.eratz.domain.std.zaap.ZaapServerHandler;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;

/**
 * 
 * @since
 */
public abstract class BaseServerPacketHandler implements ServerPacketHandler {

	private Perso perso;
	private Account account;
	private String ticket;
	private Set<FightServerHandler> fightHandler = new HashSet<>();
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
	private Set<ItemServerHandler> itemHandler = new HashSet<>();
	private Set<PartyServerHandler> partyHandler = new HashSet<>();
	private Set<JobServerHandler> jobHandler = new HashSet<>();
	private Set<FriendServerHandler> friendHandler = new HashSet<>();

	public BaseServerPacketHandler(Perso perso) {
		this.perso = perso;
	}

	public void addFriendHandlers(FriendServerHandler... handlers) {
		Arrays.stream(handlers).forEach(friendHandler::add);
	}

	public void addJobHandlers(JobServerHandler... handlers) {
		Arrays.stream(handlers).forEach(jobHandler::add);
	}

	public void addPartyHandlers(PartyServerHandler... handlers) {
		Arrays.stream(handlers).forEach(partyHandler::add);
	}

	public void addItemHandlers(ItemServerHandler... handlers) {
		Arrays.stream(handlers).forEach(itemHandler::add);
	}

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

	public void addExchangeHandlers(ExchangeServerHandler... handlers) {
		Arrays.stream(handlers).forEach(exchangeHandler::add);
	}

	public void addFightHandlers(FightServerHandler... handlers) {
		Arrays.stream(handlers).forEach(fightHandler::add);
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
	 * @param account
	 *            the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @return the jobHandler
	 */
	public Set<JobServerHandler> getJobHandler() {
		return jobHandler;
	}

	/**
	 * @return the exchangeHandler
	 */
	public Set<ExchangeServerHandler> getExchangeHandler() {
		return exchangeHandler;
	}

	/**
	 * @return the partyHandler
	 */
	public Set<PartyServerHandler> getPartyHandler() {
		return partyHandler;
	}

	/**
	 * @return the friendHandler
	 */
	public Set<FriendServerHandler> getFriendHandler() {
		return friendHandler;
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
	 * @return the itemHandler
	 */
	public Set<ItemServerHandler> getItemHandler() {
		return itemHandler;
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
	 * @return the fightHandler
	 */
	public Set<FightServerHandler> getFightHandler() {
		return fightHandler;
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
		if (getPerso() == null) LOGGER.info("[RCV:]< " + pkt);
		else LOGGER.info("[" + getPerso().getPseudo() + ":RCV:]< " + pkt);
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
		forEachAccountHandlers(h -> h.onHelloServer(ticket));
	}

	@Override
	public void handle(AccountKeyPacket pkt) {
		log(pkt);
		forEachAccountHandlers(h -> h.onKey(pkt.getKey(), pkt.getData()));
	}

	@Override
	public void handle(FriendListPacket pkt) {
		log(pkt);
		if (!pkt.getOfflineFriends().isEmpty()) getFriendHandler().forEach(h -> h.onOfflineFriends(pkt.getOfflineFriends()));
		if (!pkt.getOnlineFriends().isEmpty()) getFriendHandler().forEach(h -> h.onOnlineFriends(pkt.getOnlineFriends()));
	}

	@Override
	public void handle(AccountRegionalVersionPacket pkt) {
		log(pkt);
		forEachAccountHandlers(AccountServerHandler::onRegion);
	}

	@Override
	public void handle(ChatMessageOkPacket pkt) {
		log(pkt);
		getChatHandler().forEach(h -> h.onMsg(pkt.getChat(), pkt.getPlayerId(), pkt.getPseudo(), pkt.getMsg()));
	}

	@Override
	public void handle(ChatSubscribeChannelPacket pkt) {
		log(pkt);
		Arrays.stream(pkt.getChannels()).forEach(c -> getPerso().getChatInfos().getChats().put(c, pkt.isAdd()));
		if (pkt.isAdd()) getChatHandler().forEach(h -> h.onSubscribeChannel(pkt.getChannels()));
		else getChatHandler().forEach(h -> h.onUnsubscribe(pkt.getChannels()));
	}

	@Override
	public void handle(ZaapLeavePacket pkt) {
		log(pkt);
		getZaapHandler().forEach(ZaapServerHandler::onLeaveZaap);
	}

	@Override
	public void handle(AccountCharactersListPacket pkt) {
		log(pkt);
		for (AvailableCharacter c : pkt.getCharacters())
			for (Perso p : getAccount().getPersos())
				if (p.getPseudo().equals(c.getPseudo())) {
					p.setId(c.getId());
					p.getStatsInfos().setLvl(c.getLevel());
				}
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
		for (DofusServer s : pkt.getServers())
			if (s.getId() == Server.ERATZ.getId()) {
				AccountsManager.ERATZ.setState(s.getState());
				AccountsManager.ERATZ.setServerPopulation(s.getServerPopulation());
			} else {
				AccountsManager.HENUAL.setState(s.getState());
				AccountsManager.HENUAL.setServerPopulation(s.getServerPopulation());
			}
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
		this.ticket = pkt.getTicketKey();
		forEachAccountHandlers(h -> h.onReceiveServerHost(pkt.getIp(), pkt.getPort(), this));
	}

	@Override
	public void handle(AccountServerHostPacket pkt) {
		log(pkt);
		this.ticket = pkt.getTicketKey();
		forEachAccountHandlers(h -> h.onReceiveServerHost(pkt.getIp(), pkt.getPort(), this));
	}

	@Override
	public void handle(AccountServerListPacket pkt) {
		log(pkt);
		forEachAccountHandlers(h -> h.onReceiveServerPersoCount(pkt.getSubscriptionDuration(), pkt.getCharacters()));
	}

	@Override
	public void handle(AccountTicketOkPacket pkt) {
		log(pkt);
		getAccountHandler().forEach(h -> h.onTicketOk(pkt.getKey(), pkt.getData()));
	}

	@Override
	public void handle(AccountTicketPacket pkt) {
		log(pkt);
		getAccountHandler().forEach(h -> h.onTicket(pkt.getTicket()));
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
	public void handle(BasicConfirmPacket pkt) {
		log(pkt);
		// useless
	}

	@Override
	public void handle(Aks0MessagePacket pkt) {
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
		if (pkt.getMessage() == null) return;
		Fight fight = getPerso().getFightInfos().getCurrentFight();
		switch (pkt.getMessage()) {
			case FIGHT_ATTRIBUTE_ALLOW_GROUP_ACTIVE:
				fight.setGroupBlocked(true);
				break;
			case FIGHT_ATTRIBUTE_ALLOW_GROUP_NOT_ACTIVE:
				fight.setGroupBlocked(false);
				break;
			case FIGHT_ATTRIBUTE_DENY_ACTIVE:
				fight.setBlocked(true);
				break;
			case FIGHT_ATTRIBUTE_DENY_NOT_ACTIVE:
				fight.setBlocked(false);
				break;
			case FIGHT_ATTRIBUTE_DENY_SPECTATE_ACTIVE:
				fight.setSpecBlocked(true);
				break;
			case FIGHT_ATTRIBUTE_DENY_SPECTATE_NOT_ACTIVE:
				fight.setSpecBlocked(false);
				break;
			case FIGHT_ATTRIBUTE_NEED_HELP_ACTIVE:
				fight.setHelpNeeded(true);
				break;
			case FIGHT_ATTRIBUTE_NEED_HELP_NOT_ACTIVE:
				fight.setHelpNeeded(false);
				break;
			default:
				break;
		}
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
		getPerso().getFightInfos().getCurrentFight().setEnded(true);
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
		if (pkt.getState() == GameType.FIGHT)
			getPerso().getFightInfos().setCurrentFight(Fight.fromGame(pkt.getFightType(), pkt.isSpectator(), pkt.getStartTimer(), pkt.isDuel()));
		getGameHandler().forEach(h -> h.onFightJoin(pkt.getState(), pkt.getFightType(), pkt.isSpectator(), pkt.getStartTimer(), pkt.isCancelButton(), pkt.isDuel()));
	}

	@Override
	public void handle(GameMapDataPacket pkt) {
		log(pkt);
		DofusMap m = null;
		try {
			InputStream downloadMap = Maps.downloadMap(pkt.getMapId(), pkt.getSubid());
			Map<String, Object> extractVariable = SwfVariableExtractor.extractVariable(downloadMap);
			m = Maps.loadMap(extractVariable, pkt.getDecryptKey());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		BotMap bm = MapsManager.getOrCreate(m);
		for (Cell cell : m.getCells()) {
			if (Interractable.isInterractable(cell.getLayerObject2Num())) // add ressource
				bm.getRessources().add(new Ressource(cell, Interractable.fromId(cell.getLayerObject2Num()))); // interractable peut etre null dans le cas des zaapi porte coffre etc
		}
		getPerso().getMapInfos().setMap(bm);
		MapView.setTitle(getPerso().getPseudo() + " | " + bm.getInfos());
		getPerso().getDebugView().setOnCellClick(a -> Executors.FIXED.execute(() -> {
			System.out.println(bm.getDofusMap().getCell(a));
			getPerso().getNavigation().moveToCell(a);
		}));
		getPerso().getDebugView().setPath(null);
		getPerso().getDebugView().setMap(bm.getDofusMap());
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
		log(gameMovementPacket);
		if (gameMovementPacket.getType() == GameMovementType.REMOVE) {
			gameMovementPacket.getActors().forEach(v -> {
				MovementRemoveActor actor = (MovementRemoveActor) (Object) v.getSecond();
				if (getPerso().isInFight()) getPerso().getFightInfos().getCurrentFight().removeEntity(actor.getId());
				else getPerso().getMapInfos().getMap().removeActor(actor.getId());
				getGameHandler().forEach(h -> h.onEntityLeave(actor.getId()));
				getPerso().getDebugView().removeActor(actor.getId());
			});
			return;
		}
		gameMovementPacket.getActors().forEach(e -> {
			switch (e.getFirst()) {
				case DEFAULT:
					MovementPlayer player = (MovementPlayer) (Object) e.getSecond();
					if (player.getId() == getPerso().getId()) {
						if (player.isFight()) getPerso().getStatsInfos().setLvl(player.getPlayerInFight().getLvl());
						getPerso().getMapInfos().setCellId(player.getCellId());
						getPerso().getNavigation().notifyMovementEnd();
					} else {
						if (getPerso().isInFight()) getPerso().getFightInfos().getCurrentFight().addEntity(player);
						else getPerso().getMapInfos().getMap().entityUpdate(player);
					}
					getPerso().getDebugView().addPlayer(player.getId(), player.getCellId());
					getGameHandler().forEach(h -> h.onPlayerMove(player));
					return;
				case CREATE_INVOCATION:
				case CREATE_MONSTER:
					MovementMonster mob = (MovementMonster) (Object) e.getSecond();
					if (getPerso().isInFight()) getPerso().getFightInfos().getCurrentFight().addEntity(mob);
					else getPerso().getMapInfos().getMap().entityUpdate(mob);
					getPerso().getDebugView().addMob(mob.getId(), mob.getCellId());
					getGameHandler().forEach(h -> h.onMobMove(mob));
					return;
				case CREATE_MONSTER_GROUP:
					MovementMonsterGroup mobs = (MovementMonsterGroup) (Object) e.getSecond();
					if (getPerso().isInFight()) getPerso().getFightInfos().getCurrentFight().addEntity(mobs);
					else getPerso().getMapInfos().getMap().entityUpdate(mobs);
					getPerso().getDebugView().addMob(mobs.getId(), mobs.getCellId());
					getGameHandler().forEach(h -> h.onMobGroupMove(mobs));
					return;
				case CREATE_NPC:
					MovementNpc npc = (MovementNpc) (Object) e.getSecond();
					if (getPerso().isInFight()) getPerso().getFightInfos().getCurrentFight().addEntity(npc);
					else getPerso().getMapInfos().getMap().entityUpdate(npc);
					getPerso().getDebugView().addNpc(npc.getId(), npc.getCellId());
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
		pkt.getPositions().forEach(p -> getPerso().getFightInfos().getCurrentFight().entityMove(p.getEntityId(), p.getPosition()));
		getGameHandler().forEach(h -> pkt.getPositions().forEach(p -> h.onEntityFightPositionChange(p.getEntityId(), p.getPosition())));
	}

	@Override
	public void handle(GamePositionStartPacket pkt) {
		log(pkt);
		Fight f = getPerso().getFightInfos().getCurrentFight();
		f.setPlaceTeam0(pkt.getPlacesTeam0());
		f.setPlaceTeam1(pkt.getPlacesTeam1());
	}

	@Override
	public void handle(GameServerActionPacket pkt) {
		log(pkt);
		switch (pkt.getType()) {
			case ERROR:
				getGameActionHandler().forEach(GameActionServerHandler::onActionError);
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
				getGameActionHandler().forEach(h -> actions.getSummoned().forEach(h::onEntitySummoned));
				break;
			case FIGHT_JOIN_ERROR:
				GameJoinErrorAction actionj = (GameJoinErrorAction) pkt.getAction();
				getGameActionHandler().forEach(h -> h.onFightJoinError(actionj.getError()));
				break;
			case MOVE:
				GameMoveAction actionm = (GameMoveAction) pkt.getAction();
				int cell = actionm.getPath().get(actionm.getPath().size() - 1).getCellId();
				getPerso().getDebugView().addEntity(pkt.getEntityId(), cell);
				if (pkt.getEntityId() == getPerso().getId()) {
					getPerso().getMapInfos().setCellId(cell);
					getPerso().getBotInfos().setLastMove(System.currentTimeMillis());
				}

				getGameActionHandler().forEach(h -> h.onEntityMove(pkt.getEntityId(), actionm.getPath()));
				break;
			case DUEL_SERVER_ASK:
				GameDuelServerAction actiond = (GameDuelServerAction) pkt.getAction();
				getGameActionHandler().forEach(h -> h.onDuel(pkt.getEntityId(), actiond.getTargetId()));
				break;
			case ACCEPT_DUEL:
				GameAcceptDuelAction actionda = (GameAcceptDuelAction) pkt.getAction();
				getGameActionHandler().forEach(h -> h.onPlayerAcceptDuel(pkt.getEntityId(), actionda.getTargetId()));
				break;
			case REFUSE_DUEL:
				GameRefuseDuelAction actiondr = (GameRefuseDuelAction) pkt.getAction();
				getGameActionHandler().forEach(h -> h.onPlayerRefuseDuel(pkt.getEntityId(), actiondr.getTargetId()));
				break;
			case SPELL_LAUNCHED:
				GameSpellLaunchedAction actionsp = (GameSpellLaunchedAction) pkt.getAction();
				getGameActionHandler().forEach(h -> h.onSpellLaunched(actionsp.getSpellId(), actionsp.getCellId(), actionsp.getLvl()));
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
		Fight fight = getPerso().getFightInfos().getCurrentFight();
		for (FightEntity e : pkt.getEntities())
			fight.addEntity(e);
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
		getPerso().getFightInfos().getCurrentFight().setCurrentTurn(pkt.getCharacterId());
		getGameHandler().forEach(h -> h.onEntityTurnStart(pkt.getCharacterId(), pkt.getTime()));
	}

	@Override
	public void handle(ExchangeRequestOkPacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onExchangeRequestOk(pkt.getPlayerId(), pkt.getTargetId(), pkt.getExchange()));
	}

	@Override
	public void handle(DialogLeavePacket pkt) {
		log(pkt);
		getDialogHandler().forEach(DialogServerHandler::onDialogLeave);
	}

	@Override
	public void handle(DialogCreateOkPacket pkt) {
		log(pkt);
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
		getDialogHandler().forEach(h -> h.onDialogCreate(pkt.getNpcId()));
	}

	@Override
	public void handle(DialogQuestionPacket pkt) {
		log(pkt);
		getDialogHandler().forEach(h -> h.onQuestion(pkt.getQuestion(), pkt.getQuestionParam(), pkt.getResponse()));
	}

	@Override
	public void handle(DialogPausePacket pkt) {
		log(pkt);
		getDialogHandler().forEach(DialogServerHandler::onDialogPause);
	}

	@Override
	public void handle(ExchangeReadyPacket pkt) {
		log(pkt);
		getExchangeHandler().forEach(h -> h.onExchangeReady(pkt.getExtraData()));
	}

	@Override
	public void handle(ItemAddOkPacket pkt) {
		log(pkt);
		getItemHandler().forEach(h -> h.onItemsAdd(pkt.getItems()));
	}

	@Override
	public void handle(ItemAddErrorPacket pkt) {
		log(pkt);
		getItemHandler().forEach(h -> h.onItemAddError(pkt.getResult()));
	}

	@Override
	public void handle(ItemDropErrorPacket pkt) {
		log(pkt);
		getItemHandler().forEach(h -> h.onItemDropError(pkt.getResult()));
	}

	@Override
	public void handle(ItemRemovePacket pkt) {
		log(pkt);
		getItemHandler().forEach(h -> h.onItemRemove(pkt.getItemuid()));
	}

	@Override
	public void handle(ItemQuantityUpdatePacket pkt) {
		log(pkt);
		getItemHandler().forEach(h -> h.onItemQuantityUpdate(pkt.getItemUid(), pkt.getAmount()));
	}

	@Override
	public void handle(ItemMovementConfirmPacket pkt) {
		log(pkt);
		getItemHandler().forEach(h -> h.onItemMove(pkt.getItemUid(), pkt.getPosition()));
	}

	@Override
	public void handle(ItemToolPacket pkt) {
		log(pkt);
		getItemHandler().forEach(h -> h.onItemToolEquip(pkt.getJobId()));
	}

	@Override
	public void handle(ItemWeightPacket pkt) {
		log(pkt);
		getPerso().getStatsInfos().setPods(pkt.getCurrentWeight());
		getPerso().getStatsInfos().setMaxPods(pkt.getMaxWeight());
		getItemHandler().forEach(h -> h.onPodsUpdate(pkt.getCurrentWeight(), pkt.getMaxWeight()));
	}

	@Override
	public void handle(AccountStatsPacket pkt) {
		log(pkt);
		Perso p = getPerso();
		StatsInfo s = p.getStatsInfos();
		s.setXp(pkt.getXp());
		s.setMinXp(pkt.getXpLow());
		s.setMaxXp(pkt.getXpHigh());
		p.getInventory().setKamas(pkt.getKama());
		s.setStatsPoint(pkt.getBonusPoints());
		s.setSpellsPoints(pkt.getBonusPointsSpell());
		p.getPvpInfos().setAlignment(pkt.getAlignment());
		p.getPvpInfos().setRank(pkt.getRank());
		s.setLife(pkt.getLife());
		s.setLifeMax(pkt.getLifeMax());
		s.setEnergy(pkt.getEnergy());
		s.setEnergyMax(pkt.getEnergyMax());
		s.setInitiative(pkt.getInitiative());
		s.setProspection(pkt.getProspection());
		s.setStats(pkt.getStats());
		getAccountHandler().forEach(AccountServerHandler::onStatsUpdate);
	}

	@Override
	public void handle(AccountNewLevelPacket pkt) {
		log(pkt);
		getAccountHandler().forEach(h -> h.onNewLvl(pkt.getNewlvl()));
	}

	@Override
	public void handle(AccountServerQueuePacket pkt) {
		log(pkt);
		getAccountHandler().forEach(h -> h.onServerQueue(pkt.getPosition()));
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
	public void handle(PartyRefusePacket pkt) {
		log(pkt);
		getPartyHandler().forEach(PartyServerHandler::onPlayerRefuse);
	}

	@Override
	public void handle(PartyInviteRequestOkPacket pkt) {
		log(pkt);
		getPartyHandler().forEach(h -> h.onInvitePlayerInGroup(pkt.getInviter(), pkt.getInvited()));
	}

	@Override
	public void handle(PartyInviteRequestErrorPacket pkt) {
		log(pkt);
		getPartyHandler().forEach(h -> h.onInviteFail(pkt.getReason()));
	}

	@Override
	public void handle(PartyLeaderPacket pkt) {
		log(pkt);
		getPartyHandler().forEach(h -> h.onGroupLeaderUpdate(pkt.getLeaderId()));
	}

	@Override
	public void handle(PartyCreateOkPacket pkt) {
		log(pkt);
		getPartyHandler().forEach(PartyServerHandler::onJoinGroupOk);
	}

	@Override
	public void handle(PartyCreateErrorPacket pkt) {
		log(pkt);
		getPartyHandler().forEach(h -> h.onJoinGroupError(pkt.getReason()));
	}

	@Override
	public void handle(PartyPlayerLeavePacket pkt) {
		log(pkt);
		getPartyHandler().forEach(h -> h.onPlayerLeaveGroup(pkt.getPlayer()));
	}

	@Override
	public void handle(PartyFollowReceivePacket pkt) {
		log(pkt);
		if (pkt.isSuccess())
			getPartyHandler().forEach(h -> h.onFollow(pkt.getFollowed()));
		else getPartyHandler().forEach(PartyServerHandler::onStopFollow);
	}

	@Override
	public void handle(PartyMovementPacket pkt) {
		log(pkt);
		getPartyHandler().forEach(h -> Arrays.stream(pkt.getMembers()).forEach(m -> h.onPartyMemberUpdate(pkt.getMove(), m)));
	}

	@Override
	public void handle(GameTeamPacket pkt) {
		log(pkt);
		getGameHandler().forEach(h -> h.onFightTeams(pkt.getFirstId(), pkt.getEntities()));
	}

	@Override
	public void handle(JobSkillsPacket pkt) {
		log(pkt);
		getJobHandler().forEach(h -> Arrays.stream(pkt.getJobs()).forEach(h::onPlayerJobInfo));
	}

	@Override
	public void handle(JobXpPacket pkt) {
		log(pkt);
		getJobHandler().forEach(h -> Arrays.stream(pkt.getInfos()).forEach(h::onJobXp));
	}

	@Override
	public void handle(JobLevelPacket pkt) {
		log(pkt);
		getJobHandler().forEach(h -> h.onJobLvl(pkt.getJob(), pkt.getLvl()));
	}

	@Override
	public void handle(GameSpawnPacket pkt) {
		log(pkt);
		if (pkt.isCreated()) {
			getPerso().getFightInfos().getFightsOnMap().add(pkt.getFight());
			getGameHandler().forEach(h -> h.onFightSpawn(pkt.getFight()));
		} else {
			getPerso().getFightInfos().getFightsOnMap().remove(pkt.getFight());
			getGameHandler().forEach(h -> h.onFightRemoved(pkt.getFight()));
		}
	}

	@Override
	public void handle(FightCountPacket pkt) {
		log(pkt);
		getFightHandler().forEach(h -> h.onFightCount(pkt.getCount()));
	}

	@Override
	public void handle(FightListPacket pkt) {
		log(pkt);
		getFightHandler().forEach(h -> pkt.getFights().forEach(h::onFightInfos));
	}

	@Override
	public void handle(FightDetailsPacket pkt) {
		log(pkt);
		getFightHandler().forEach(h -> h.onFightDetails(pkt.getDetailsId(), pkt.getT0(), pkt.getT1()));
	}

	@Override
	public void handle(InfoCompassPacket pkt) {
		log(pkt);
		getInfoHandler().forEach(h -> h.onCompass(pkt.getX(), pkt.getY()));
	}

	@Override
	public void handle(InfoCoordinatePacket pkt) {
		log(pkt);
		getInfoHandler().forEach(h -> pkt.getPlayers().forEach(h::onFollowedPlayerMove));
	}

}
