/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.io.handler;

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
		// TODO

	}

	@Override
	public void handle(AccountKeyPacket accountKeyPacket) {
		// TODO

	}

	@Override
	public void handle(AccountRegionalVersionPacket accountRegionalVersionPacket) {
		// TODO

	}

	@Override
	public void handle(ChatSubscribeChannelPacket chatSubscribeChannelPacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeLeavePacket exchangeLeavePacket) {
		// TODO

	}

	@Override
	public void handle(ZaapLeavePacket zaapLeavePacket) {
		// TODO

	}

	@Override
	public void handle(AccountCharactersListPacket accountCharactersListPacket) {
		// TODO

	}

	@Override
	public void handle(AccountCommunityPacket accountCommunityPacket) {
		// TODO

	}

	@Override
	public void handle(AccountHostPacket accountHostPacket) {
		// TODO

	}

	@Override
	public void handle(AccountLoginErrPacket accountLoginErrPacket) {
		// TODO

	}

	@Override
	public void handle(AccountLoginOkPacket accountLoginOkPacket) {
		// TODO

	}

	@Override
	public void handle(AccountPseudoPacket accountPseudoPacket) {
		// TODO

	}

	@Override
	public void handle(AccountQuestionPacket accountQuestionPacket) {
		// TODO

	}

	@Override
	public void handle(AccountQueuePosition accountQueuePosition) {
		// TODO

	}

	@Override
	public void handle(AccountRestrictionsPacket accountRestrictionsPacket) {
		// TODO

	}

	@Override
	public void handle(AccountSelectCharacterOkPacket accountSelectCharacterOkPacket) {
		// TODO

	}

	@Override
	public void handle(AccountServerEncryptedHostPacket accountServerEncryptedHostPacket) {
		// TODO

	}

	@Override
	public void handle(AccountServerHostPacket accountServerHostPacket) {
		// TODO

	}

	@Override
	public void handle(AccountServerListPacket accountServerListPacket) {
		// TODO

	}

	@Override
	public void handle(AccountTicketOkPacket accountTicketOkPacket) {
		// TODO

	}

	@Override
	public void handle(AccountTicketPacket accountTicketPacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeCreatePacket exchangeCreatePacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeListPacket exchangeListPacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeRequestPacket exchangeRequestPacket) {
		// TODO

	}

	@Override
	public void handle(BasicConfirmPacket basicConfirmPacket) {
		// TODO

	}

	@Override
	public void handle(GuildStatPacket guildStatPacket) {
		// TODO

	}

	@Override
	public void handle(HelloConnectionPacket helloConnectionPacket) {
		// TODO

	}

	@Override
	public void handle(InfoMessagePacket infoMessagePacket) {
		// TODO

	}

	@Override
	public void handle(MountXpPacket mountXpPacket) {
		// TODO

	}

	@Override
	public void handle(SpecializationSetPacket specializationSetPacket) {
		// TODO

	}

	@Override
	public void handle(SpellChangeOptionPacket spellChangeOptionPacket) {
		// TODO

	}

	@Override
	public void handle(SpellListPacket spellListPacket) {
		// TODO

	}

	@Override
	public void handle(SubareaListPacket subareaListPacket) {
		// TODO

	}

	@Override
	public void handle(ZaapCreatePacket zaapCreatePacket) {
		// TODO

	}

	@Override
	public void handle(ZaapUseErrorPacket zaapUseErrorPacket) {
		// TODO

	}

	@Override
	public void handle(GameActionFinishPacket gameActionFinishPacket) {
		// TODO

	}

	@Override
	public void handle(GameActionStartPacket gameActionStartPacket) {
		// TODO

	}

	@Override
	public void handle(GameEffectPacket gameEffectPacket) {
		// TODO

	}

	@Override
	public void handle(GameEndPacket gameEndPacket) {
		// TODO

	}

	@Override
	public void handle(GameFightChallengePacket gameFightChallengePacket) {
		// TODO

	}

	@Override
	public void handle(GameJoinPacket gameJoinPacket) {
		// TODO

	}

	@Override
	public void handle(GameMapDataPacket gameMapDataPacket) {
		// TODO

	}

	@Override
	public void handle(GameMapFramePacket gameMapFramePacket) {
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
	public void handle(GameOnReadyPacket gameOnReadyPacket) {
		// TODO

	}

	@Override
	public void handle(GamePositionsPacket gamePositionsPacket) {
		// TODO

	}

	@Override
	public void handle(GamePositionStartPacket gamePositionStartPacket) {
		// TODO

	}

	@Override
	public void handle(GameServerActionPacket gameServerActionPacket) {
		// TODO

	}

	@Override
	public void handle(GameServerReadyPacket gameServerReadyPacket) {
		// TODO

	}

	@Override
	public void handle(GameStartPacket gameStartPacket) {
		// TODO

	}

	@Override
	public void handle(GameStartToPlayPacket gameStartToPlayPacket) {
		// TODO

	}

	@Override
	public void handle(GameTurnFinishPacket gameTurnFinishPacket) {
		// TODO

	}

	@Override
	public void handle(GameTurnListPacket gameTurnListPacket) {
		// TODO

	}

	@Override
	public void handle(GameTurnMiddlePacket gameTurnMiddlePacket) {
		// TODO

	}

	@Override
	public void handle(GameTurnReadyPacket gameTurnReadyPacket) {
		// TODO

	}

	@Override
	public void handle(GameTurnStartPacket gameTurnStartPacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeRequestOkPacket exchangeRequestOkPacket) {
		// TODO

	}

	@Override
	public void handle(DialogCreateOkPacket dialogCreateOkPacket) {
		// TODO

	}

	@Override
	public void handle(DialogQuestionPacket dialogQuestionPacket) {
		// TODO

	}

	@Override
	public void handle(DialogPausePacket dialogPausePacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeReadyPacket exchangeReadyPacket) {
		// TODO

	}

	@Override
	public void handle(ItemAddOkPacket itemAddOkPacket) {
		// TODO

	}

	@Override
	public void handle(ItemAddErrorPacket itemAddErrorPacket) {
		// TODO

	}

	@Override
	public void handle(ItemDropErrorPacket itemDropErrorPacket) {
		// TODO

	}

	@Override
	public void handle(ItemRemovePacket itemRemovePacket) {
		// TODO

	}

	@Override
	public void handle(ItemQuantityUpdatePacket itemQuantityUpdatePacket) {
		// TODO

	}

	@Override
	public void handle(ItemMovementConfirmPacket itemMovementConfirmPacket) {
		// TODO

	}

	@Override
	public void handle(ItemToolPacket itemToolPacket) {
		// TODO

	}

	@Override
	public void handle(ItemWeightPacket itemWeightPacket) {
		// TODO

	}

	@Override
	public void handle(AccountStatsPacket accountStatsPacket) {
		// TODO

	}

	@Override
	public void handle(AccountNewLevelPacket accountNewLevelPacket) {
		// TODO

	}

	@Override
	public void handle(AccountServerQueuePacket accountServerQueuePacket) {
		// TODO

	}

}
