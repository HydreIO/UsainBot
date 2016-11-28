/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.handler.base;

import fr.aresrpg.dofus.protocol.PacketHandler;
import fr.aresrpg.eratz.domain.player.Account;
import fr.aresrpg.eratz.domain.proxy.Proxy;

/**
 * 
 * @since
 */
public abstract class BaseHandler implements PacketHandler {

	private Account account;
	private Proxy proxy;

	public BaseHandler(Proxy proxy) {
		this.proxy = proxy;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @return the proxy
	 */
	public Proxy getProxy() {
		return proxy;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

}
