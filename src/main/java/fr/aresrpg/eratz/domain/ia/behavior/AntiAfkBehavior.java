/*******************************************************************************
 * BotFather (C) - Dofus 1.29 bot
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.ia.behavior;

import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.state.PlayerState;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Sit/unsit pour ne pas se faire déco
 * 
 * @since
 */
public class AntiAfkBehavior extends Behavior {

	private boolean infinite; // si true l'anti afk se relancera dés que le perso sera en mode IDLE

	/**
	 * @param perso
	 */
	public AntiAfkBehavior(Perso perso, boolean infinite) {
		super(perso);
		this.infinite = infinite;
	}

	public void run() {
		getPerso().getAbilities().getBaseAbility().sit(!getPerso().getBotInfos().isSit());
	}

	@Override
	public BehaviorStopReason start() {
		do {
			getPerso().setState(PlayerState.IDLE);
			ScheduledFuture ftr = Executors.SCHEDULER.register(this::run, 8, TimeUnit.MINUTES);
			while (getPerso().isIdling())
				;
			ftr.cancel(true);
			if (infinite) while (!getPerso().isIdling())
				;
		} while (infinite);
		return BehaviorStopReason.FINISHED;
	}

}
