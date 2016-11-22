package fr.aresrpg.eratz.domain.proxy;

import fr.aresrpg.dofus.protocol.DofusConnection;

public interface Proxy {

	DofusConnection getLocalConnection();

	DofusConnection getRemoteConnection();

	void changeConnection(DofusConnection connection, ProxyConnectionType type);

	public static enum ProxyConnectionType {
		LOCAL,
		REMOTE
	}
}
