/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 *  
 * Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.handler;

import fr.aresrpg.dofus.protocol.PacketHandler;
import fr.aresrpg.eratz.domain.player.Account;

/**
 * 
 * @since
 */
public abstract class BaseHandler implements PacketHandler {

	private Account account;

	public BaseHandler(Account account) {
		this.account = account;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

}
