/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.io.handler.proxy;

import fr.aresrpg.dofus.protocol.*;
import fr.aresrpg.dofus.protocol.account.AccountKeyPacket;
import fr.aresrpg.dofus.protocol.account.AccountRegionalVersionPacket;
import fr.aresrpg.dofus.protocol.account.client.*;
import fr.aresrpg.dofus.protocol.basic.client.BasicUseSmileyPacket;
import fr.aresrpg.dofus.protocol.chat.ChatSubscribeChannelPacket;
import fr.aresrpg.dofus.protocol.conquest.client.WorldInfosJoinPacket;
import fr.aresrpg.dofus.protocol.conquest.client.WorldInfosLeavePacket;
import fr.aresrpg.dofus.protocol.dialog.DialogLeavePacket;
import fr.aresrpg.dofus.protocol.dialog.client.*;
import fr.aresrpg.dofus.protocol.emote.client.EmoteUsePacket;
import fr.aresrpg.dofus.protocol.exchange.ExchangeLeavePacket;
import fr.aresrpg.dofus.protocol.exchange.client.*;
import fr.aresrpg.dofus.protocol.fight.client.*;
import fr.aresrpg.dofus.protocol.game.client.*;
import fr.aresrpg.dofus.protocol.info.client.InfoMapPacket;
import fr.aresrpg.dofus.protocol.item.client.*;
import fr.aresrpg.dofus.protocol.job.client.JobChangeStatsPacket;
import fr.aresrpg.dofus.protocol.mount.client.MountPlayerPacket;
import fr.aresrpg.dofus.protocol.party.PartyAcceptPacket;
import fr.aresrpg.dofus.protocol.party.PartyRefusePacket;
import fr.aresrpg.dofus.protocol.party.client.*;
import fr.aresrpg.dofus.protocol.waypoint.ZaapLeavePacket;
import fr.aresrpg.dofus.protocol.waypoint.client.ZaapUsePacket;
import fr.aresrpg.eratz.domain.TheBotFather;
import fr.aresrpg.eratz.domain.data.AccountsManager;
import fr.aresrpg.eratz.domain.data.player.Account;
import fr.aresrpg.eratz.domain.io.handler.BaseClientPacketHandler;
import fr.aresrpg.eratz.domain.io.proxy.DofusProxy;
import fr.aresrpg.eratz.domain.io.proxy.Proxy;
import fr.aresrpg.eratz.domain.util.encryption.CryptHelper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 
 * @since
 */
public class LocalHandler extends BaseClientPacketHandler {

	private boolean state_machine = false;
	private Account account;
	private Proxy proxy;

	/**
	 * @param perso
	 */
	public LocalHandler(Proxy proxy) {
		super(null);
		this.proxy = proxy;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	protected void transmit(Packet pkt) {
		try {
			getProxy().getRemoteConnection().send(pkt);
		} catch (IOException e) {
			e.printStackTrace();
			TheBotFather.LOGGER.error("Client disconnected");
			getPerso().getAccount().getCurrentPlayed().shutdown();
		}
	}

	/**
	 * @return the proxy
	 */
	public Proxy getProxy() {
		return proxy;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @param state_machine
	 *            the state_machine to set
	 */
	public void setStateMachine(boolean state_machine) {
		this.state_machine = state_machine;
	}

	private boolean contains(ProtocolRegistry registry) {
		for (ProtocolRegistry r : RemoteHandler.toSkip)
			if (r == registry) return true;
		return false;
	}

	@Override
	public boolean parse(ProtocolRegistry registry, String packet) {
		if (state_machine && registry == null || contains(registry)) {
			try {
				System.out.println("[SEND direct] -> " + packet);
				((SocketChannel) getProxy().getRemoteConnection().getChannel()).write(ByteBuffer.wrap(packet.getBytes()));
			} catch (IOException e) {
				e.printStackTrace();
				TheBotFather.LOGGER.error("Client disconnected");
				getPerso().getAccount().getCurrentPlayed().shutdown();
			}
			return true;
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public void register(DofusConnection<?> connection) {
	}

	@Override
	public void handle(AccountAuthPacket pkt) {
		super.handle(pkt);
		setAccount(AccountsManager.getInstance().getOrRegister(pkt.getPseudo(), CryptHelper.decryptpass(pkt.getHashedPassword().substring(2), getProxy().getHc())));
		((DofusProxy) getProxy()).initAccount(getAccount());
		transmit(pkt);
		state_machine = true;
	}

	@Override
	public void handle(AccountSelectCharacterPacket pkt) {
		super.handle(pkt);
		transmit(pkt);
	}

	@Override
	public void handle(GameClientActionPacket pkt) {
		super.handle(pkt);
		transmit(pkt);
	}

	@Override
	public void handle(BasicUseSmileyPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountAccessServerPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountGetCharactersPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountGetGiftsPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountGetQueuePosition pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountIdentityPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountListServersPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountSetCharacterPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameActionACKPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameClientReadyPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameCreatePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameEndTurnPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameExtraInformationPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameFreeMySoulPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameLeavePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameSetPlayerPositionPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameTurnEndPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameTurnOkPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(InfoMapPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(MountPlayerPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeAcceptPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ZaapUsePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(EmoteUsePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeRequestPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeMoveItemsPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeSendReadyPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeShopPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeMoveKamasPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeSellToNpcPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeBuyToNpcPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeDisconnectAsMerchantPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeAskToDisconnectAsMerchantPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeHdvPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeGetCrafterForJobPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeMountPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeParkMountPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeReplayCraftPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeRepeatCraftPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(DialogBeginPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(DialogCreatePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(DialogLeavePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(DialogResponsePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(FightBlockSpectatePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(FightRestrictGroupPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(FightBlockAllPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(FightNeedHelpPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameActionCancel pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ItemMovementPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ItemDropPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ItemDestroyPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ItemUsePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeLeavePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(PartyInvitePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(PartyLeavePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(PartyFollowPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(PartyWherePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(PartyFollowAllPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(JobChangeStatsPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(WorldInfosJoinPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(WorldInfosLeavePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ItemSkinPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountKeyPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountRegionalVersionPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ChatSubscribeChannelPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ZaapLeavePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(PartyAcceptPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(PartyRefusePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

}
