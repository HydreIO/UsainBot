/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.player;

import fr.aresrpg.eratz.domain.util.config.Variables;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @since
 */
public class AccountsManager {

	private static final AccountsManager instance = new AccountsManager();
	private Map<String, Account> accounts = new HashMap<>();

	private AccountsManager() {
		Variables.ACCOUNTS.forEach(b -> accounts.put(b.getAccountName(), new Account(b.getAccountName(), b.getPassword())));
	}

	/**
	 * @return the instance
	 */
	public static AccountsManager getInstance() {
		return instance;
	}

	/**
	 * Retrieve a registered account or create and register it
	 * 
	 * @param accountName
	 *            the pseudo
	 * @param pass
	 *            the password
	 * @return the retrieved account or the new created one
	 */
	public Account getOrRegister(String accountName, String pass) {
		if (exist(accountName)) return getAccounts().get(accountName);
		Account account = new Account(accountName, pass);
		registerAccount(account);
		return account;
	}

	private void registerAccount(Account account) {
		System.out.println("New account registered ! | " + account);
		getAccounts().put(account.getUsername(), account);
	}

	public boolean exist(String accountname) {
		return getAccounts().containsKey(accountname);
	}

	/**
	 * @return the accounts
	 */
	public Map<String, Account> getAccounts() {
		return accounts;
	}

}
