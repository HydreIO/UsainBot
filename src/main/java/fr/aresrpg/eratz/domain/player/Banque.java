package fr.aresrpg.eratz.domain.player;

/**
 * 
 * @since
 */
public class Banque extends Inventory {

	private Account account;

	public Banque(Account account) {
		this.account = account;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	@Override
	public String toString() {
		return "Banque [account=" + account + "]" + super.toString();
	}

}
