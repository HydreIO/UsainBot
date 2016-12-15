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
import fr.aresrpg.dofus.protocol.DofusConnection;
import fr.aresrpg.dofus.protocol.ServerPacketHandler;
import fr.aresrpg.dofus.protocol.account.AccountKeyPacket;
import fr.aresrpg.dofus.protocol.account.AccountRegionalVersionPacket;
import fr.aresrpg.dofus.protocol.account.server.*;
import fr.aresrpg.dofus.protocol.basic.server.BasicConfirmPacket;
import fr.aresrpg.dofus.protocol.chat.ChatSubscribeChannelPacket;
import fr.aresrpg.dofus.protocol.dialog.server.*;
import fr.aresrpg.dofus.protocol.exchange.ExchangeLeavePacket;
import fr.aresrpg.dofus.protocol.exchange.client.ExchangeRequestPacket;
import fr.aresrpg.dofus.protocol.exchange.server.*;
import fr.aresrpg.dofus.protocol.game.movement.*;
import fr.aresrpg.dofus.protocol.game.server.*;
import fr.aresrpg.dofus.protocol.guild.server.GuildStatPacket;
import fr.aresrpg.dofus.protocol.hello.server.HelloConnectionPacket;
import fr.aresrpg.dofus.protocol.hello.server.HelloGamePacket;
import fr.aresrpg.dofus.protocol.info.server.message.InfoMessagePacket;
import fr.aresrpg.dofus.protocol.item.server.*;
import fr.aresrpg.dofus.protocol.mount.server.MountXpPacket;
import fr.aresrpg.dofus.protocol.specialization.server.SpecializationSetPacket;
import fr.aresrpg.dofus.protocol.spell.server.SpellChangeOptionPacket;
import fr.aresrpg.dofus.protocol.spell.server.SpellListPacket;
import fr.aresrpg.dofus.protocol.subarea.server.SubareaListPacket;
import fr.aresrpg.dofus.protocol.waypoint.ZaapLeavePacket;
import fr.aresrpg.dofus.protocol.waypoint.server.ZaapCreatePacket;
import fr.aresrpg.dofus.protocol.waypoint.server.ZaapUseErrorPacket;
import fr.aresrpg.dofus.structures.game.GameMovementType;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.io.handler.bot.craft.CraftHandler;
import fr.aresrpg.eratz.domain.io.handler.bot.fight.FightHandler;
import fr.aresrpg.eratz.domain.io.handler.bot.map.MapHandler;

import java.util.Arrays;
import java.util.Set;

/**
 * 
 * @since
 */
public class BaseServerPacketHandler implements ServerPacketHandler {

	private static final Logger logger = new LoggerBuilder("Game").setUseConsoleHandler(true, true, Option.none(), Option.none()).build();
	private Perso perso;
	private Set<FightHandler> fightHandler;
	private Set<CraftHandler> craftHandler;
	private Set<MapHandler> mapHandler;

	public void addFightHandlers(FightHandler... handlers) {
		Arrays.stream(handlers).forEach(fightHandler::add);
	}

	public void addCraftHandlers(CraftHandler... handlers) {
		Arrays.stream(handlers).forEach(craftHandler::add);
	}

	public void addMapHandlers(MapHandler... handlers) {
		Arrays.stream(handlers).forEach(mapHandler::add);
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
	 * @param perso
	 *            the perso to set
	 */
	public void setPerso(Perso perso) {
		this.perso = perso;
	}

	/**
	 * @return the fightHandler
	 */
	public Set<FightHandler> getFightHandler() {
		return fightHandler;
	}

	/**
	 * @param fightHandler
	 *            the fightHandler to set
	 */
	public void setFightHandler(Set<FightHandler> fightHandler) {
		this.fightHandler = fightHandler;
	}

	/**
	 * @return the craftHandler
	 */
	public Set<CraftHandler> getCraftHandler() {
		return craftHandler;
	}

	/**
	 * @param craftHandler
	 *            the craftHandler to set
	 */
	public void setCraftHandler(Set<CraftHandler> craftHandler) {
		this.craftHandler = craftHandler;
	}

	/**
	 * @return the mapHandler
	 */
	public Set<MapHandler> getMapHandler() {
		return mapHandler;
	}

	/**
	 * @param mapHandler
	 *            the mapHandler to set
	 */
	public void setMapHandler(Set<MapHandler> mapHandler) {
		this.mapHandler = mapHandler;
	}

	@Override
	public String toString() {
		return "BaseHandler [fightHandler=" + fightHandler + ", craftHandler=" + craftHandler + ", mapHandler=" + mapHandler + "]";
	}

	@Override
	public void register(DofusConnection<?> connection) {
	}

	@Override
	public void handle(HelloConnectionPacket pkt) {
		logger.info(pkt.toString());
		getPerso().setTicket(pkt.getHashKey());
	}

	@Override
	public void handle(HelloGamePacket pkt) {

	}

	@Override
	public void handle(AccountKeyPacket pkt) {

	}

	@Override
	public void handle(AccountRegionalVersionPacket pkt) {

	}

	@Override
	public void handle(ChatSubscribeChannelPacket pkt) {
		// TODO

	}

	@Override
	public void handle(ExchangeLeavePacket pkt) {
		// TODO

	}

	@Override
	public void handle(ZaapLeavePacket pkt) {
		// TODO

	}

	@Override
	public void handle(AccountCharactersListPacket pkt) {

	}

	@Override
	public void handle(AccountCommunityPacket pkt) {

	}

	@Override
	public void handle(AccountHostPacket pkt) {
	}

	@Override
	public void handle(AccountLoginErrPacket pkt) {

	}

	@Override
	public void handle(AccountLoginOkPacket pkt) {

	}

	@Override
	public void handle(AccountPseudoPacket pkt) {

	}

	@Override
	public void handle(AccountQuestionPacket pkt) {

	}

	@Override
	public void handle(AccountQueuePosition pkt) {
		// TODO

	}

	@Override
	public void handle(AccountRestrictionsPacket pkt) {
		// TODO

	}

	@Override
	public void handle(AccountSelectCharacterOkPacket pkt) {
		// TODO

	}

	@Override
	public void handle(AccountServerEncryptedHostPacket pkt) {
		// TODO

	}

	@Override
	public void handle(AccountServerHostPacket pkt) {
		// TODO

	}

	@Override
	public void handle(AccountServerListPacket pkt) {
		// TODO

	}

	@Override
	public void handle(AccountTicketOkPacket pkt) {
		// TODO

	}

	@Override
	public void handle(AccountTicketPacket pkt) {
		// TODO

	}

	@Override
	public void handle(ExchangeCreatePacket pkt) {
		// TODO

	}

	@Override
	public void handle(ExchangeListPacket pkt) {
		// TODO

	}

	@Override
	public void handle(ExchangeRequestPacket pkt) {
		// TODO

	}

	@Override
	public void handle(BasicConfirmPacket pkt) {
		// TODO

	}

	@Override
	public void handle(GuildStatPacket pkt) {
		// TODO

	}

	@Override
	public void handle(InfoMessagePacket pkt) {
		// TODO

	}

	@Override
	public void handle(MountXpPacket pkt) {
		// TODO

	}

	@Override
	public void handle(SpecializationSetPacket pkt) {
		// TODO

	}

	@Override
	public void handle(SpellChangeOptionPacket pkt) {
		// TODO

	}

	@Override
	public void handle(SpellListPacket pkt) {
		// TODO

	}

	@Override
	public void handle(SubareaListPacket pkt) {
		// TODO

	}

	@Override
	public void handle(ZaapCreatePacket pkt) {
		// TODO

	}

	@Override
	public void handle(ZaapUseErrorPacket pkt) {
		// TODO

	}

	@Override
	public void handle(GameActionFinishPacket pkt) {
		// TODO

	}

	@Override
	public void handle(GameActionStartPacket pkt) {
		// TODO

	}

	@Override
	public void handle(GameEffectPacket pkt) {
		// TODO

	}

	@Override
	public void handle(GameEndPacket pkt) {
		// TODO

	}

	@Override
	public void handle(GameFightChallengePacket pkt) {
		// TODO

	}

	@Override
	public void handle(GameJoinPacket pkt) {
		// TODO

	}

	@Override
	public void handle(GameMapDataPacket pkt) {
		// TODO

	}

	@Override
	public void handle(GameMapFramePacket pkt) {
		// TODO

	}

	@Override
	public void handle(GameMovementPacket gameMovementPacket) {
		if (gameMovementPacket.getType() == GameMovementType.REMOVE) {
			gameMovementPacket.getActors().forEach(v -> {
				MovementRemoveActor actor = (MovementRemoveActor) (Object) v.getSecond();
				getMapHandler().forEach(h -> h.onEntityLeave(actor.getId()));
			});
			return;
		}
		gameMovementPacket.getActors().forEach(e -> {
			switch (e.getFirst()) {
				case DEFAULT:
					MovementPlayer player = (MovementPlayer) (Object) e.getSecond();
					this.mapHandler.forEach(h -> h.onPlayerMove(player));
					return;
				case CREATE_INVOCATION:
					MovementInvocation invoc = (MovementInvocation) (Object) e.getSecond();
					this.mapHandler.forEach(h -> h.onInvocMove(invoc));
					return;
				case CREATE_MONSTER:
					MovementMonster mob = (MovementMonster) (Object) e.getSecond();
					this.mapHandler.forEach(h -> h.onMobMove(mob));
					return;
				case CREATE_MONSTER_GROUP:
					MovementMonsterGroup mobs = (MovementMonsterGroup) (Object) e.getSecond();
					this.mapHandler.forEach(h -> h.onMobGroupMove(mobs));
					return;
				case CREATE_NPC:
					MovementNpc npc = (MovementNpc) (Object) e.getSecond();
					this.mapHandler.forEach(h -> h.onNpcMove(npc));
					return;
				default:
					break;
			}
		});
	}

	@Override
	public void handle(GameOnReadyPacket pkt) {
		// TODO

	}

	@Override
	public void handle(GamePositionsPacket pkt) {
		// TODO

	}

	@Override
	public void handle(GamePositionStartPacket pkt) {
		// TODO

	}

	@Override
	public void handle(GameServerActionPacket pkt) {
		// TODO

	}

	@Override
	public void handle(GameServerReadyPacket pkt) {
		// TODO

	}

	@Override
	public void handle(GameStartPacket pkt) {
		// TODO

	}

	@Override
	public void handle(GameStartToPlayPacket pkt) {
		// TODO

	}

	@Override
	public void handle(GameTurnFinishPacket pkt) {
		// TODO

	}

	@Override
	public void handle(GameTurnListPacket pkt) {
		// TODO

	}

	@Override
	public void handle(GameTurnMiddlePacket pkt) {
		// TODO

	}

	@Override
	public void handle(GameTurnReadyPacket pkt) {
		// TODO

	}

	@Override
	public void handle(GameTurnStartPacket pkt) {
		// TODO

	}

	@Override
	public void handle(ExchangeRequestOkPacket pkt) {
		// TODO

	}

	@Override
	public void handle(DialogCreateOkPacket pkt) {
		// TODO

	}

	@Override
	public void handle(DialogQuestionPacket pkt) {
		// TODO

	}

	@Override
	public void handle(DialogPausePacket pkt) {
		// TODO

	}

	@Override
	public void handle(ExchangeReadyPacket pkt) {
		// TODO

	}

	@Override
	public void handle(ItemAddOkPacket pkt) {
		// TODO

	}

	@Override
	public void handle(ItemAddErrorPacket pkt) {
		// TODO

	}

	@Override
	public void handle(ItemDropErrorPacket pkt) {
		// TODO

	}

	@Override
	public void handle(ItemRemovePacket pkt) {
		// TODO

	}

	@Override
	public void handle(ItemQuantityUpdatePacket pkt) {
		// TODO

	}

	@Override
	public void handle(ItemMovementConfirmPacket pkt) {
		// TODO

	}

	@Override
	public void handle(ItemToolPacket pkt) {
		// TODO

	}

	@Override
	public void handle(ItemWeightPacket pkt) {
		// TODO

	}

	@Override
	public void handle(AccountStatsPacket pkt) {
		// TODO

	}

	@Override
	public void handle(AccountNewLevelPacket pkt) {
		// TODO

	}

	@Override
	public void handle(AccountServerQueuePacket pkt) {
		// TODO

	}

}
