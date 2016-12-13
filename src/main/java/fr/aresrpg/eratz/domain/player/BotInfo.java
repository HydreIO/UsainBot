package fr.aresrpg.eratz.domain.player;

/**
 * 
 * @since
 */
public class BotInfo extends Info {

	private BotJob botJob;

	/**
	 * @param perso
	 */
	public BotInfo(Perso perso) {
		super(perso);
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
		return "BotInfo [botJob=" + botJob + super.toString() + "]";
	}

}
