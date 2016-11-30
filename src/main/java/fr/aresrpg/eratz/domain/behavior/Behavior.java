/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.behavior;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.eratz.domain.player.Perso;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public interface Behavior extends Runnable {

	Perso getPerso();

	default void reset() {
		getPerso().resetBehavior();
	}

	default <T extends Behavior> T botWait(int time, TimeUnit unit) {
		try {
			Threads.sleep(time, unit);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return (T) this;
	}

	default <T extends Behavior> T waitSec(int sec) {
		return botWait(sec, TimeUnit.SECONDS);
	}

	void start();

	@Override
	default void run() {
		start();
		reset();
	}

}
