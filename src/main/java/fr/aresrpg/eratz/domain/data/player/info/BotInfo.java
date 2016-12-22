package fr.aresrpg.eratz.domain.data.player.info;

import fr.aresrpg.eratz.domain.data.dofus.player.BotJob;
import fr.aresrpg.eratz.domain.data.player.Perso;

/**
 * 
 * @since
 */
public class BotInfo extends Info {

	private BotJob botJob;
	private boolean sit;

	/**
	 * @param perso
	 */
	public BotInfo(Perso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {
		sit = false;
	}

	/**
	 * @return the sit
	 */
	public boolean isSit() {
		return sit;
	}

	/**
	 * @param sit
	 *            the sit to set
	 */
	public void setSit(boolean sit) {
		this.sit = sit;
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
