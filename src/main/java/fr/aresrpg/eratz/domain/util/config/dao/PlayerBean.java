package fr.aresrpg.eratz.domain.util.config.dao;

/**
 * 
 * @since
 */
public class PlayerBean {

	private String accountName;
	private String password;

	public PlayerBean(String account, String pass) {
		this.accountName = account;
		this.password = pass;
	}

	public PlayerBean() {
	}

	@Override
	public String toString() {
		if (accountName == null || accountName.length() < 5) return "Account[invalid]";
		return "Account[name=" + accountName.substring(0, 4) + "**|pass=**]";
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param accountName
	 *            the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
