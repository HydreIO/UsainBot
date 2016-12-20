package fr.aresrpg.eratz.domain.ia.behavior.antibot;

import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;

/**
 * 
 * @since
 */
public class DuelCrashBehavior extends Behavior {

	private boolean running;

	/**
	 * @param perso
	 */
	public DuelCrashBehavior(Perso perso, boolean running) {
		super(perso);
		this.running = running;
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running
	 *            the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public BehaviorStopReason start() {
		while (isRunning()) {

		}
		return BehaviorStopReason.FINISHED;
	}

}
