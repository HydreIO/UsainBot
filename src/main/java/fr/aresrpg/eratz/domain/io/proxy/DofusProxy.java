/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.io.proxy;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.commons.domain.log.AnsiColors.AnsiColor;
import fr.aresrpg.dofus.protocol.DofusConnection;
import fr.aresrpg.dofus.protocol.ProtocolRegistry.Bound;
import fr.aresrpg.eratz.domain.data.player.Account;
import fr.aresrpg.eratz.domain.data.player.state.AccountState;
import fr.aresrpg.eratz.domain.io.handler.proxy.LocalHandler;
import fr.aresrpg.eratz.domain.io.handler.proxy.RemoteHandler;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class DofusProxy implements Proxy {

	private DofusConnection localConnection;
	private DofusConnection remoteConnection;

	private LocalHandler localHandler = new LocalHandler(this);
	private RemoteHandler remoteHandler = new RemoteHandler(this);

	private Account account;
	private String hc;

	public DofusProxy(SocketChannel localChannel, SocketChannel remoteChannel) throws IOException {
		changeConnection(new DofusConnection<>("Local", localChannel, localHandler, Bound.CLIENT), ProxyConnectionType.LOCAL);
		changeConnection(new DofusConnection<>("Remote", remoteChannel, remoteHandler, Bound.SERVER), ProxyConnectionType.REMOTE);
	}

	public void initAccount(Account account) {
		this.account = account;
		account.setProxy(this);
		account.setState(AccountState.CLIENT_IN_REALM);
		account.setLastConnection(System.currentTimeMillis());
		localHandler.setAccount(account);
		remoteHandler.setAccount(account);
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
		getLocalConnection().closeConnection();
		getRemoteConnection().closeConnection();
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
		LOGGER.info("Starting '" + connection.getLabel() + "' connection !");
		try {
			if (type == ProxyConnectionType.LOCAL) {
				if (this.localConnection != null) this.localConnection.closeConnection();
				this.localConnection = connection;
			} else {
				if (this.remoteConnection != null) this.remoteConnection.closeConnection();
				this.remoteConnection = connection;
				if (account != null) account.setRemoteConnection(remoteConnection);
			}
			Executors.FIXED.execute(() -> {
				LOGGER.info("Connection '" + connection.getLabel() + "' Started !");
				try {
					connection.start();
				} catch (Exception e) {
					LOGGER.info(AnsiColor.CYAN + "Client déconnecté !");
					connection.closeConnection(); // on close le server socket
					if (account != null) {
						if (account.getCurrentPlayed() != null) account.getCurrentPlayed().shutdown();
						account.setState(AccountState.OFFLINE); // on notify que le client n'est plus la pour possiblement connecter le bot
					}

				}
			});
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	@Override
	public RemoteHandler getRemoteHandler() {
		return this.remoteHandler;
	}

	@Override
	public LocalHandler getLocalHandler() {
		return this.localHandler;
	}

}
