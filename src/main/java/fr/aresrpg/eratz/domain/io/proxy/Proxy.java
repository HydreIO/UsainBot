/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.io.proxy;

import fr.aresrpg.dofus.protocol.DofusConnection;
import fr.aresrpg.eratz.domain.io.handler.impl.proxy.LocalHandler;
import fr.aresrpg.eratz.domain.io.handler.impl.proxy.RemoteHandler;

public interface Proxy {

	DofusConnection getLocalConnection();

	DofusConnection getRemoteConnection();

	RemoteHandler getRemoteHandler();

	LocalHandler getLocalHandler();

	String getHc();

	void setHc(String hc);

	void changeConnection(DofusConnection connection, ProxyConnectionType type);

	void shutdown();

	public static enum ProxyConnectionType {
		LOCAL,
		REMOTE
	}
}
