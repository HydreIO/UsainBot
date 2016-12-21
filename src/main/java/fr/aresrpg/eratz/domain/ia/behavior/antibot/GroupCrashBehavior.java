package fr.aresrpg.eratz.domain.ia.behavior.antibot;

import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbilityState.InvitationState;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class GroupCrashBehavior extends Behavior {

	private boolean running;
	private List<String> targets = new ArrayList<>();

	/**
	 * @param perso
	 */
	public GroupCrashBehavior(Perso perso) {
		super(perso);
		this.running = true;
	}

	/**
	 * @return the targets
	 */
	public List<String> getTargets() {
		return targets;
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
		loop: while (isRunning()) {
			for (String i : targets) {
				InvitationState st = ab.invitPlayerToGroupAndCancel(i, 0, TimeUnit.NANOSECONDS);
				if (st == InvitationState.ACCEPTED) break loop;
			}
		}
		return BehaviorStopReason.FINISHED;
	}

	@Override
	public String toString() {
		return "GroupCrashBehavior [running=" + running + ", targets=" + targets + "]";
	}

}
