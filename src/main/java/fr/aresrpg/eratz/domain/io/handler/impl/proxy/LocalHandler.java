/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.io.handler.impl.proxy;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.dofus.protocol.*;
import fr.aresrpg.dofus.protocol.account.AccountKeyPacket;
import fr.aresrpg.dofus.protocol.account.AccountRegionalVersionPacket;
import fr.aresrpg.dofus.protocol.account.client.*;
import fr.aresrpg.dofus.protocol.chat.ChatSubscribeChannelPacket;
import fr.aresrpg.dofus.protocol.chat.client.BasicUseSmileyPacket;
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
import fr.aresrpg.eratz.domain.data.AccountsManager;
import fr.aresrpg.eratz.domain.data.player.Account;
import fr.aresrpg.eratz.domain.data.player.Perso;
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
	public LocalHandler(Proxy proxy, Perso perso) {
		super(perso);
		this.proxy = proxy;
		this.account = perso.getAccount();
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	protected void transmit(Packet pkt) {
		try {
			LOGGER.info("[SEND:]>> " + pkt);
			getProxy().getRemoteConnection().send(pkt);
		} catch (IOException e) {
			e.printStackTrace();
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
		if (registry == null || contains(registry)) {
			try {
				((SocketChannel) getProxy().getLocalConnection().getChannel()).write(ByteBuffer.wrap((packet + "\n\0").getBytes()));
			} catch (IOException e) {
				e.printStackTrace();
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
		setAccount(AccountsManager.getInstance().getOrRegister(pkt.getPseudo(), CryptHelper.decryptpass(pkt.getHashedPassword().substring(2), getProxy().getHc())));
		((DofusProxy) getProxy()).initAccount(getAccount());
		transmit(pkt);
		state_machine = true;
	}

	@Override
	public void handle(AccountSelectCharacterPacket pkt) {
		getAccount().getPersos().stream()
				.filter(p -> p.getId() == pkt.getCharacterId())
				.forEach(getAccount()::setCurrentPlayed);
		transmit(pkt);
	}

	@Override
	public void handle(GameClientActionPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(BasicUseSmileyPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(AccountAccessServerPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(AccountGetCharactersPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(AccountGetGiftsPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(AccountGetQueuePosition pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(AccountIdentityPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(AccountListServersPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(AccountSetCharacterPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(GameActionACKPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(GameClientReadyPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(GameCreatePacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(GameEndTurnPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(GameExtraInformationPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(GameFreeMySoulPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(GameLeavePacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(GameSetPlayerPositionPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(GameTurnEndPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(GameTurnOkPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(InfoMapPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(MountPlayerPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeAcceptPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ZaapUsePacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(EmoteUsePacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeRequestPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeMoveItemsPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeSendReadyPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeShopPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeMoveKamasPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeSellToNpcPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeBuyToNpcPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeDisconnectAsMerchantPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeAskToDisconnectAsMerchantPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeHdvPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeGetCrafterForJobPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeMountPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeParkMountPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeReplayCraftPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeRepeatCraftPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(DialogBeginPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(DialogCreatePacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(DialogLeavePacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(DialogResponsePacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(FightBlockSpectatePacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(FightRestrictGroupPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(FightBlockAllPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(FightNeedHelpPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(GameActionCancel pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ItemMovementPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ItemDropPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ItemDestroyPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ItemUsePacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeLeavePacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(PartyInvitePacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(PartyLeavePacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(PartyFollowPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(PartyWherePacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(PartyFollowAllPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(JobChangeStatsPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(WorldInfosJoinPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(WorldInfosLeavePacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ItemSkinPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(AccountKeyPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(AccountRegionalVersionPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ChatSubscribeChannelPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(ZaapLeavePacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(PartyAcceptPacket pkt) {
		transmit(pkt);

	}

	@Override
	public void handle(PartyRefusePacket pkt) {
		transmit(pkt);

	}

}
