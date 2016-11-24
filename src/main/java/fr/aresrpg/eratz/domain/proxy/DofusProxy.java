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
import fr.aresrpg.dofus.protocol.ProtocolRegistry.Bound;
import fr.aresrpg.eratz.domain.handler.LocalProxyHandler;
import fr.aresrpg.eratz.domain.handler.RemoteProxyHandler;
import fr.aresrpg.eratz.domain.player.Account;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class DofusProxy implements Proxy {

	private DofusConnection localConnection;
	private DofusConnection remoteConnection;

	public DofusProxy(Account account, SocketChannel localChannel, SocketChannel remoteChannel) throws IOException {
		account.setProxy(this);
		changeConnection(new DofusConnection("Local", localChannel, new LocalProxyHandler(account), Bound.CLIENT), ProxyConnectionType.LOCAL);
		changeConnection(new DofusConnection("Remote", remoteChannel, new RemoteProxyHandler(account), Bound.SERVER), ProxyConnectionType.REMOTE);
	}

	public void close() {
		getLocalConnection().close();
		getRemoteConnection().close();
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
		if (type == ProxyConnectionType.LOCAL) this.localConnection = connection;
		else this.remoteConnection = connection;
		Executors.FIXED.execute(() -> {
			try {

				System.out.println("Start connection.");
				connection.read();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
