package fr.aresrpg.eratz.domain.player;

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

	}

	/**
	 * @return the instance
	 */
	public static AccountsManager getInstance() {
		return instance;
	}

	public void registerAccount(Account account) {
		System.out.println("New account registered ! | " + account);
		getAccounts().putIfAbsent(account.getUsername(), account);
	}

	/**
	 * @return the accounts
	 */
	public Map<String, Account> getAccounts() {
		return accounts;
	}

}
