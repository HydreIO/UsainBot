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
import fr.aresrpg.eratz.domain.util.CompletableRunnable;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public interface Behavior extends CompletableRunnable {

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

}
