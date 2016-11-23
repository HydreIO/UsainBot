package fr.aresrpg.eratz.domain.handler;

import fr.aresrpg.dofus.protocol.DofusConnection;
import fr.aresrpg.dofus.protocol.Packet;
import fr.aresrpg.dofus.protocol.account.AccountKeyPacket;
import fr.aresrpg.dofus.protocol.account.AccountRegionalVersionPacket;
import fr.aresrpg.dofus.protocol.account.client.*;
import fr.aresrpg.dofus.protocol.account.server.*;
import fr.aresrpg.dofus.protocol.basic.server.BasicConfirmPacket;
import fr.aresrpg.dofus.protocol.hello.client.HelloGamePacket;
import fr.aresrpg.dofus.protocol.hello.server.HelloConnectionPacket;
import fr.aresrpg.eratz.domain.dofus.Constants;
import fr.aresrpg.eratz.domain.player.Account;
import fr.aresrpg.eratz.domain.proxy.Proxy;
import fr.aresrpg.eratz.domain.proxy.Proxy.ProxyConnectionType;
import fr.aresrpg.eratz.domain.util.encryption.CryptHelper;

import java.io.IOException;
import java.net.InetSocketAddress;
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
	public void register(DofusConnection<?> connection) {
	}

	@Override
	public void handle(HelloGamePacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(HelloConnectionPacket pkt) {
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
			String ip = CryptHelper.decryptIp(pkt.getIp());
			Proxy proxy = getAccount().getProxy();
			proxy.getLocalConnection().send(new AccountServerHostPacket().setIp(Constants.LOCALHOST).setPort(Constants.BOT_PORT).setTicketKey(pkt.getTicketKey()));
			getAccount().getProxy().changeConnection(new DofusConnection(SocketChannel.open(new InetSocketAddress(ip, 443)), new RemoteProxyHandler(getAccount())), ProxyConnectionType.REMOTE);
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

}
