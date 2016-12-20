package fr.aresrpg.eratz.domain.util.config.dao;

import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.eratz.domain.data.dofus.player.BotJob;

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
		private String classe;
		private boolean male;

		public PersoBean(String pseudo, String botJob, String server, String classe, boolean male) {
			this.pseudo = pseudo;
			this.botJob = botJob;
			this.server = server;
			this.classe = classe;
			this.male = male;
		}

		public PersoBean(String pseudo, String job, Server server, String classe, boolean male) {
			this(pseudo, job, server.name(), classe, male);
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
		 * @return the classe
		 */
		public String getClasse() {
			return classe;
		}

		/**
		 * @param classe
		 *            the classe to set
		 */
		public void setClasse(String classe) {
			this.classe = classe;
		}

		/**
		 * @return the male
		 */
		public boolean isMale() {
			return male;
		}

		/**
		 * @param male
		 *            the male to set
		 */
		public void setMale(boolean male) {
			this.male = male;
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
			return "PersoBean [pseudo=" + pseudo + ", botJob=" + botJob + ", server=" + server + ", classe=" + classe + ", male=" + male + "]";
		}

	}
}
