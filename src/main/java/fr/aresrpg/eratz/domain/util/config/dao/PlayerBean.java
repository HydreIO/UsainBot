package fr.aresrpg.eratz.domain.util.config.dao;

import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.eratz.domain.player.BotJob;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public class PlayerBean {

	private String accountName;
	private String password;
	private List<PersoBean> persos;

	public PlayerBean(String account, String pass, PersoBean... persos) {
		this.accountName = account;
		this.password = pass;
		this.persos = Arrays.stream(persos).collect(Collectors.toList());
	}

	public PlayerBean() {
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName
	 *            the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the persos
	 */
	public List<PersoBean> getPersos() {
		return persos;
	}

	/**
	 * @param persos
	 *            the persos to set
	 */
	public void setPersos(List<PersoBean> persos) {
		this.persos = persos;
	}

	@Override
	public String toString() {
		return "PlayerBean [accountName=" + accountName + ", password=" + password + ", persos=" + persos + "]";
	}

	public static class PersoBean {
		private String pseudo;
		private String botJob;
		private String server;

		public PersoBean(String pseudo, String job, String server) {
			this.pseudo = pseudo;
			this.botJob = job;
		}

		public PersoBean() {
		}

		/**
		 * @return the server
		 */
		public String getServer() {
			return server;
		}

		/**
		 * @param server
		 *            the server to set
		 */
		public void setServer(String server) {
			this.server = server;
		}

		/**
		 * @param pseudo
		 *            the pseudo to set
		 */
		public void setPseudo(String pseudo) {
			this.pseudo = pseudo;
		}

		/**
		 * @param botJob
		 *            the botJob to set
		 */
		public void setBotJob(String botJob) {
			this.botJob = botJob;
		}

		/**
		 * @return the pseudo
		 */
		public String getPseudo() {
			return pseudo;
		}

		public Server getDofusServer() {
			return Server.valueOf(getServer());
		}

		public BotJob getBotJob() {
			return botJob == null ? BotJob.NONE : BotJob.valueOf(botJob.toUpperCase());
		}

		@Override
		public String toString() {
			return "PersoBean [pseudo=" + pseudo + ", botJob=" + botJob + ", server=" + server + "]";
		}

	}
}
