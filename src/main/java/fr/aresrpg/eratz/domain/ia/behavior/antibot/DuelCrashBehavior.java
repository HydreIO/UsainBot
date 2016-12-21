package fr.aresrpg.eratz.domain.ia.behavior.antibot;

import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbilityState.InvitationState;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class DuelCrashBehavior extends Behavior {

	private boolean running;
	private int target;

	/**
	 * @param perso
	 */
	public DuelCrashBehavior(Perso perso) {
		super(perso);
		this.running = true;
	}

	/**
	 * @return the target
	 */
	public int getTarget() {
		return target;
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	public void stop() {
		this.running = false;
	}

	@Override
	public BehaviorStopReason start() {
		BaseAbility ab = getPerso().getAbilities().getBaseAbility();
		while (isRunning()) {
			InvitationState state = ab.defiPlayerAndCancel(getTarget(), 0, TimeUnit.NANOSECONDS);
			if (state == InvitationState.ACCEPTED) break;
		}
		return BehaviorStopReason.FINISHED;
	}

	@Override
	public String toString() {
		return "DuelCrashBehavior [running=" + running + ", target=" + target + "]";
	}

}
