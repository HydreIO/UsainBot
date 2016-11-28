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
import fr.aresrpg.eratz.domain.handler.proxy.ProxyHandler;

public interface Proxy {

	DofusConnection getLocalConnection();

	DofusConnection getRemoteConnection();

	ProxyHandler getRemoteHandler();

	PacketHandler getLocalHandler();

	String getHc();

	void setHc(String hc);

	void changeConnection(DofusConnection connection, ProxyConnectionType type);

	void shutdown();

	public static enum ProxyConnectionType {
		LOCAL,
		REMOTE
	}
}
