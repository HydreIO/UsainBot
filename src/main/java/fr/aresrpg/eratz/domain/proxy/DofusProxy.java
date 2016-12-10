/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.proxy;

import fr.aresrpg.dofus.protocol.DofusConnection;
import fr.aresrpg.dofus.protocol.PacketHandler;
import fr.aresrpg.dofus.protocol.ProtocolRegistry.Bound;
import fr.aresrpg.eratz.domain.handler.BaseHandler;
import fr.aresrpg.eratz.domain.handler.proxy.*;
import fr.aresrpg.eratz.domain.player.Account;
import fr.aresrpg.eratz.domain.player.state.AccountState;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

import java.io.IOException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.SocketChannel;

public class DofusProxy implements Proxy {

	private DofusConnection localConnection;
	private DofusConnection remoteConnection;

	private PacketHandler localHandler = new LocalHandler(this);
	private PacketHandler remoteHandler = new RemoteHandler(this);
	private ProxyHandler proxyHandler = new ProxyHandler(remoteHandler);

	private Account account;
	private String hc;

	public DofusProxy(SocketChannel localChannel, SocketChannel remoteChannel) throws IOException {
		changeConnection(new DofusConnection<>("Local", localChannel, localHandler, Bound.CLIENT), ProxyConnectionType.LOCAL);
		changeConnection(new DofusConnection<>("Remote", remoteChannel, proxyHandler, Bound.SERVER), ProxyConnectionType.REMOTE);
	}

	public void initAccount(Account account) {
		this.account = account;
		account.setProxy(this);
		account.setState(AccountState.CLIENT_IN_REALM);
		account.setLastConnection(System.currentTimeMillis());
		((BaseHandler) localHandler).setAccount(account);
		((BaseHandler) remoteHandler).setAccount(account);
	}

	@Override
	public String getHc() {
		return this.hc;
	}

	@Override
	public void setHc(String hc) {
		this.hc = hc;
	}

	@Override
	public void shutdown() {
		try {
			getLocalConnection().close();
			getRemoteConnection().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the localConnection
	 */
	public DofusConnection getLocalConnection() {
		return localConnection;
	}

	/**
	 * @return the remoteConnection
	 */
	public DofusConnection getRemoteConnection() {
		return remoteConnection;
	}

	@Override
	public void changeConnection(DofusConnection connection, ProxyConnectionType type) {
		try {
			if (type == ProxyConnectionType.LOCAL) {
				if (this.localConnection != null) this.localConnection.close();
				this.localConnection = connection;
			} else {
				if (this.remoteConnection != null) this.remoteConnection.close();
				this.remoteConnection = connection;
				if (account != null) account.setRemoteConnection(remoteConnection);
			}
			Executors.FIXED.execute(() -> {
				System.out.println("Start connection.");
				try {
					while (connection.getChannel().isOpen())
						connection.read();
				} catch (Exception e) {
					e.printStackTrace();
					if (!(e instanceof AsynchronousCloseException)) {
						try {
							connection.close(); // on close le server socket
							getRemoteHandler().removeHandlers(); // on vire les handlers remotre (dofus -> app)
							if (account != null) account.notifyDisconnect(); // on notify que le client n'est plus la pour possiblement connecter le bot
						} catch (IOException e1) {
							e.printStackTrace();
						}
					}
				}
			});
		} catch (Exception e2) {
			e2.printStackTrace();
		}

	}

	@Override
	public ProxyHandler getRemoteHandler() {
		return this.proxyHandler;
	}

	@Override
	public PacketHandler getLocalHandler() {
		return this.localHandler;
	}

}
