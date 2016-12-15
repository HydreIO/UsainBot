/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.io.handler;

import fr.aresrpg.dofus.protocol.ClientPacketHandler;
import fr.aresrpg.dofus.protocol.DofusConnection;
import fr.aresrpg.dofus.protocol.account.AccountKeyPacket;
import fr.aresrpg.dofus.protocol.account.AccountRegionalVersionPacket;
import fr.aresrpg.dofus.protocol.account.client.*;
import fr.aresrpg.dofus.protocol.chat.ChatSubscribeChannelPacket;
import fr.aresrpg.dofus.protocol.chat.client.BasicUseSmileyPacket;
import fr.aresrpg.dofus.protocol.dialog.DialogLeavePacket;
import fr.aresrpg.dofus.protocol.dialog.client.*;
import fr.aresrpg.dofus.protocol.emote.client.EmoteUsePacket;
import fr.aresrpg.dofus.protocol.exchange.ExchangeLeavePacket;
import fr.aresrpg.dofus.protocol.exchange.client.*;
import fr.aresrpg.dofus.protocol.fight.client.*;
import fr.aresrpg.dofus.protocol.game.client.*;
import fr.aresrpg.dofus.protocol.hello.server.HelloGamePacket;
import fr.aresrpg.dofus.protocol.info.client.InfoMapPacket;
import fr.aresrpg.dofus.protocol.item.client.*;
import fr.aresrpg.dofus.protocol.mount.client.MountPlayerPacket;
import fr.aresrpg.dofus.protocol.waypoint.ZaapLeavePacket;
import fr.aresrpg.dofus.protocol.waypoint.client.ZaapUsePacket;
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
public abstract class BaseClientPacketHandler implements ClientPacketHandler {

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
	public void handle(BasicUseSmileyPacket chatUseSmileyPacket) {
		// TODO

	}

	@Override
	public void handle(AccountAuthPacket accountAuthPacket) {
		// TODO

	}

	@Override
	public void handle(AccountAccessServerPacket accountAccessServerPacket) {
		// TODO

	}

	@Override
	public void handle(AccountGetCharactersPacket accountGetCharactersPacket) {
		// TODO

	}

	@Override
	public void handle(AccountGetGiftsPacket accountGetGiftsPacket) {
		// TODO

	}

	@Override
	public void handle(AccountGetQueuePosition accountGetQueuePosition) {
		// TODO

	}

	@Override
	public void handle(AccountIdentityPacket accountIdentityPacket) {
		// TODO

	}

	@Override
	public void handle(AccountListServersPacket accountListServersPacket) {
		// TODO

	}

	@Override
	public void handle(AccountSelectCharacterPacket accountSelectCharacterPacket) {
		// TODO

	}

	@Override
	public void handle(AccountSetCharacterPacket accountSetCharacterPacket) {
		// TODO

	}

	@Override
	public void handle(GameActionACKPacket gameActionACKPacket) {
		// TODO

	}

	@Override
	public void handle(GameClientActionPacket gameClientActionPacket) {
		// TODO

	}

	@Override
	public void handle(GameClientReadyPacket gameClientReadyPacket) {
		// TODO

	}

	@Override
	public void handle(GameCreatePacket gameCreatePacket) {
		// TODO

	}

	@Override
	public void handle(GameEndTurnPacket gameEndTurnPacket) {
		// TODO

	}

	@Override
	public void handle(GameExtraInformationPacket gameExtraInformationPacket) {
		// TODO

	}

	@Override
	public void handle(GameFreeMySoulPacket gameFreeMySoulPacket) {
		// TODO

	}

	@Override
	public void handle(GameLeavePacket gameLeavePacket) {
		// TODO

	}

	@Override
	public void handle(GameSetPlayerPositionPacket gameSetPlayerPositionPacket) {
		// TODO

	}

	@Override
	public void handle(GameTurnEndPacket gameTurnEndPacket) {
		// TODO

	}

	@Override
	public void handle(GameTurnOkPacket gameTurnOkPacket) {
		// TODO

	}

	@Override
	public void handle(HelloGamePacket helloGamePacket) {
		// TODO

	}

	@Override
	public void handle(InfoMapPacket infoMapPacket) {
		// TODO

	}

	@Override
	public void handle(MountPlayerPacket playerMountPacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeAcceptPacket exchangeAcceptPacket) {
		// TODO

	}

	@Override
	public void handle(ZaapUsePacket accountAccessServerPacket) {
		// TODO

	}

	@Override
	public void handle(EmoteUsePacket emoteUsePacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeRequestPacket exchangeRequestPacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeMoveItemsPacket exchangeMovePacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeShopPacket exchangeShopPacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeMoveKamasPacket exchangeMoveKamasPacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeSellToNpcPacket exchangeSellToNpcPacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeBuyToNpcPacket exchangeBuyToNpcPacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeDisconnectAsMerchantPacker exchangeDisconnectAsMerchandPacker) {
		// TODO

	}

	@Override
	public void handle(ExchangeAskToDisconnectAsMerchantPacket exchangeAskToDisconnectAsMerchantPacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeHdvPacket exchangeAskHdvTypePacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeGetCrafterForJobPacket exchangeGetCrafterForJobPacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeMountPacket exchangeMountPacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeParkMountPacket exchangeParkMountPacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeReplayCraftPacket exchangeReplayCraftPacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeRepeatCraftPacket exchangeRepeatCraftPacket) {
		// TODO

	}

	@Override
	public void handle(ExchangeSendReadyPacket exchangeReadyPacket) {
		// TODO

	}

	@Override
	public void handle(DialogBeginPacket dialogBeginPacket) {
		// TODO

	}

	@Override
	public void handle(DialogCreatePacket dialogCreatePacket) {
		// TODO

	}

	@Override
	public void handle(DialogLeavePacket dialogLeavePacket) {
		// TODO

	}

	@Override
	public void handle(DialogResponsePacket dialogResponsePacket) {
		// TODO

	}

	@Override
	public void handle(FightBlockSpectatePacket fightBlockSpectatePacket) {
		// TODO

	}

	@Override
	public void handle(FightRestrictGroupPacket fightRestrictGroupPacket) {
		// TODO

	}

	@Override
	public void handle(FightBlockAllPacket fightBlockAllPacket) {
		// TODO

	}

	@Override
	public void handle(FightNeedHelpPacket fightNeedHelpPacket) {
		// TODO

	}

	@Override
	public void handle(GameActionCancel gameActionCancel) {
		// TODO

	}

	@Override
	public void handle(ItemMovementPacket itemMovementFastInvPacket) {
		// TODO

	}

	@Override
	public void handle(ItemDropPacket itemDropPacket) {
		// TODO

	}

	@Override
	public void handle(ItemDestroyPacket itemDestroyPacket) {
		// TODO

	}

	@Override
	public void handle(ItemUsePacket itemUsePacket) {
		// TODO

	}

}
