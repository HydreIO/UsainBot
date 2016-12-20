/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.data;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.eratz.domain.data.dofus.player.Classe;
import fr.aresrpg.eratz.domain.data.dofus.player.Genre;
import fr.aresrpg.eratz.domain.data.player.Account;
import fr.aresrpg.eratz.domain.data.player.Perso;
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
		Variables.ACCOUNTS.forEach(b -> {
			Account a = new Account(b.getAccountName(), b.getPassword());
			b.getPersos()
					.forEach(h -> a.addPerso(new Perso(-1, h.getPseudo(), a, h.getBotJob(), Classe.valueOf(h.getClasse().toUpperCase()), h.isMale() ? Genre.MALE : Genre.FEMALE, h.getDofusServer())));
			accounts.put(b.getAccountName(), a);
		});
	}

	public void connectAccount(String accountName, String perso) {
		Account a = getAccounts().get(accountName);
		if (a == null) throw new NullPointerException("L'account n'est pas configuré");
		for (Perso p : a.getPersos())
			if (p.getPseudo().equalsIgnoreCase(perso)) {
				p.connect();
				return;
			}
		throw new NullPointerException("Le Perso '" + perso + "' n'existe pas");
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
		LOGGER.info("New account registered ! | " + account);
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
