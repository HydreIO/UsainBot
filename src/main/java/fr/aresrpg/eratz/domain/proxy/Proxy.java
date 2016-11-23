/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 *  
 * Created 2016
 *******************************************************************************/
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
