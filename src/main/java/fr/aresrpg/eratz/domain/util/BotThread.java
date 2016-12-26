package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.eratz.domain.TheBotFather;

import java.util.concurrent.locks.LockSupport;

/**
 * 
 * @since
 */
public class BotThread {

	private Thread thread;
	private boolean paused;

	/**
	 * Pause the current thread
	 */
	public void pause() {
		if (paused) return;
		this.thread = thread.currentThread();
		if (ThreadBlocker.blockeds.contains(thread)) return;
		paused = true;
		LockSupport.park();
	}

	/**
	 * force update the assigned thread
	 * 
	 * @param thread
	 */
	public void forceThread(Thread thread) {
		this.thread = thread;
	}

	/**
	 * Unpause the current assigned thread
	 */
	public void unpause() {
		if (!paused) return;
		if (thread != null) {
			if (ThreadBlocker.blockeds.contains(thread)) return;
			paused = false;
			TheBotFather.LOGGER.debug("Unlocking " + thread.getName());
			LockSupport.unpark(thread);
		}
	}

}
