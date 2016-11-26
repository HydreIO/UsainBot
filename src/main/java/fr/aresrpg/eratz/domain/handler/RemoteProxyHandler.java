/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.handler;

import fr.aresrpg.dofus.protocol.*;
import fr.aresrpg.dofus.protocol.ProtocolRegistry.Bound;
import fr.aresrpg.dofus.protocol.account.AccountKeyPacket;
import fr.aresrpg.dofus.protocol.account.AccountRegionalVersionPacket;
import fr.aresrpg.dofus.protocol.account.client.*;
import fr.aresrpg.dofus.protocol.account.server.*;
import fr.aresrpg.dofus.protocol.basic.server.BasicConfirmPacket;
import fr.aresrpg.dofus.protocol.game.client.GameCreatePacket;
import fr.aresrpg.dofus.protocol.game.client.GameExtraInformationPacket;
import fr.aresrpg.dofus.protocol.game.server.GameMapDataPacket;
import fr.aresrpg.dofus.protocol.hello.client.HelloGamePacket;
import fr.aresrpg.dofus.protocol.hello.server.HelloConnectionPacket;
import fr.aresrpg.dofus.protocol.info.client.InfoMapPacket;
import fr.aresrpg.dofus.protocol.info.server.message.InfoMessagePacket;
import fr.aresrpg.dofus.protocol.mount.server.MountXpPacket;
import fr.aresrpg.dofus.protocol.specialization.server.SpecializationSetPacket;
import fr.aresrpg.eratz.domain.dofus.Constants;
import fr.aresrpg.eratz.domain.player.Account;
import fr.aresrpg.eratz.domain.proxy.Proxy;
import fr.aresrpg.eratz.domain.proxy.Proxy.ProxyConnectionType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 
 * @since
 */
public class RemoteProxyHandler extends BaseHandler {

	/**
	 * @param account
	 */
	public RemoteProxyHandler(Account account) {
		super(account);
	}

	private Proxy getProxy() {
		if (getAccount() == null) throw new NullPointerException("The account is null");
		Proxy proxy = getAccount().getProxy();
		if (proxy == null) throw new NullPointerException("The proxy for this account is null !");
		return proxy;
	}

	private void transmit(Packet pkt) {
		try {
			System.out.println("[RCV:]<< " + pkt);
			getProxy().getLocalConnection().send(pkt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean parse(ProtocolRegistry registry, String packet) {
		System.out.println(registry);
		if (registry == null || registry == ProtocolRegistry.ACCOUNT_SERVER_LIST) {
			SocketChannel channel = (SocketChannel) getProxy().getLocalConnection().getChannel();
			try {
				packet += "\0";
				channel.write(ByteBuffer.wrap(packet.getBytes()));
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
	public void handle(HelloGamePacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(HelloConnectionPacket pkt) {
		getAccount().setCurrentHc(pkt.getHashKey());
		transmit(pkt);
	}

	@Override
	public void handle(AccountAuthPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountLoginErrPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountLoginOkPacket pkt) {
		pkt.setAdmin(true);
		transmit(pkt);
	}

	@Override
	public void handle(AccountCommunityPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountHostPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountQuestionPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountPseudoPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountListServersPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountServerListPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountAccessServerPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountServerEncryptedHostPacket pkt) {
		try {
			String ip = pkt.getIp();
			Proxy proxy = getAccount().getProxy();
			ServerSocketChannel srvchannel = ServerSocketChannel.open();
			srvchannel.bind(new InetSocketAddress(0));
			int localPort = srvchannel.socket().getLocalPort();
			proxy.getLocalConnection().send(new AccountServerHostPacket().setIp(Constants.LOCALHOST).setPort(localPort).setTicketKey(pkt.getTicketKey()));
			getAccount().getProxy().changeConnection(new DofusConnection("RemoteGame", SocketChannel.open(new InetSocketAddress(ip, 443)), new RemoteProxyHandler(getAccount()), Bound.SERVER),
					ProxyConnectionType.REMOTE);
			getAccount().getProxy().changeConnection(new DofusConnection("LocalGame", srvchannel.accept(), new LocalProxyHandler(getAccount()), Bound.CLIENT), ProxyConnectionType.LOCAL);
			System.out.println("blblbl============================================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(AccountServerHostPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountTicketPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountTicketOkPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(BasicConfirmPacket pkt) {
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
	public void handle(AccountGetGiftsPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountIdentity pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountGetCharactersPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountCharactersListPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountSelectCharacterPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountGetQueuePosition pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountQueuePosition pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(MountXpPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameExtraInformationPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(InfoMessagePacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(SpecializationSetPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(InfoMapPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameCreatePacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameMapDataPacket pkt) {
		transmit(pkt);
	}

}
