package fr.aresrpg.eratz.domain.player;

/**
 * 
 * @since
 */
public class BotInfo {

	private Perso perso;
	private BotJob botJob;

	/**
	 * @param perso
	 * @param botJob
	 */
	public BotInfo(Perso perso) {
		this.perso = perso;
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	/**
	 * @param perso
	 *            the perso to set
	 */
	public void setPerso(Perso perso) {
		this.perso = perso;
	}

	/**
	 * @return the botJob
	 */
	public BotJob getBotJob() {
		return botJob;
	}

	/**
	 * @param botJob
	 *            the botJob to set
	 */
	public void setBotJob(BotJob botJob) {
		this.botJob = botJob;
	}

	@Override
	public String toString() {
		return "BotInfo [perso=" + perso + ", botJob=" + botJob + "]";
	}

}
