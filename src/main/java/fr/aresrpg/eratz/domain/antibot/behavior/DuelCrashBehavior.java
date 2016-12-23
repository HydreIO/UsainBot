package fr.aresrpg.eratz.domain.antibot.behavior;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.dofus.protocol.game.actions.GameActions;
import fr.aresrpg.dofus.protocol.game.actions.client.GameDuelAction;
import fr.aresrpg.dofus.protocol.game.actions.client.GameRefuseDuelAction;
import fr.aresrpg.dofus.protocol.game.client.GameClientActionPacket;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbility;
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
	private int count;

	/**
	 * @param perso
	 */
	public DuelCrashBehavior(Perso perso, int target) {
		super(perso);
		this.running = true;
		this.target = target;
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
		count = 0;
		while (isRunning()) {
			if (++count > 120) break;
			Threads.uSleep(2, TimeUnit.MILLISECONDS);
			GameDuelAction action = new GameDuelAction(getTarget());
			getPerso().getAbilities().getBaseAbility().getStates().currentToCrash = getTarget();
			GameClientActionPacket ga = new GameClientActionPacket(GameActions.DUEL, action);
			getPerso().sendPacketToServer(ga);

			Threads.uSleep(2, TimeUnit.MILLISECONDS);
			GameRefuseDuelAction actionr = new GameRefuseDuelAction();
			actionr.setTargetId(getPerso().getId());
			getPerso().sendPacketToServer(new GameClientActionPacket(GameActions.REFUSE_DUEL, actionr));

		}
		return BehaviorStopReason.FINISHED;
	}

	@Override
	public String toString() {
		return "DuelCrashBehavior [running=" + running + ", target=" + target + "]";
	}

}
