package fr.aresrpg.eratz.domain.proxy;

import fr.aresrpg.dofus.protocol.DofusConnection;
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
		changeConnection(new DofusConnection(localChannel, new LocalProxyHandler(account)), ProxyConnectionType.LOCAL);
		changeConnection(new DofusConnection(remoteChannel, new RemoteProxyHandler(account)), ProxyConnectionType.REMOTE);
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
