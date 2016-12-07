package fr.aresrpg.eratz.domain.util.config.dao;

import fr.aresrpg.eratz.domain.player.BotJob;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public class PlayerBean {

	private String accountName;
	private String password;
	private Map<String, String> persos = new HashMap<>();

	public PlayerBean(String account, String pass, PersoBean... persos) {
		this.accountName = account;
		this.password = pass;
		Arrays.stream(persos).forEach(p -> this.persos.put(p.getPseudo(), p.getBotJob().name().toLowerCase()));
	}

	public PlayerBean() {
	}

	/**
	 * @return the persos
	 */
	public Set<PersoBean> getPersos() {
		return persos.entrySet().stream().map(PersoBean::fromEntry).collect(Collectors.toSet());
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

	public static class PersoBean {
		private String pseudo;
		private String botJob;

		public PersoBean(String pseudo, String job) {
			this.pseudo = pseudo;
			this.botJob = job;
		}

		/**
		 * @return the pseudo
		 */
		public String getPseudo() {
			return pseudo;
		}

		public BotJob getBotJob() {
			return botJob == null ? BotJob.NONE : BotJob.valueOf(botJob.toUpperCase());
		}

		public static PersoBean fromEntry(Map.Entry<String, String> entry) {
			return new PersoBean(entry.getKey(), entry.getValue());
		}
	}
}
