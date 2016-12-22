package fr.aresrpg.eratz.domain.util;

import java.util.concurrent.locks.LockSupport;

/**
 * 
 * @since
 */
public class BotThread {

	private Thread thread;
	private boolean paused;

	public void pause(Thread thread) {
		if (paused) return;
		paused = true;
		this.thread = thread;
		LockSupport.park();
	}

	public void pause() {
		pause(thread.currentThread());
	}

	public void unpause() {
		if (!paused) return;
		if (thread != null) {
			paused = false;
			LockSupport.unpark(thread);
		}
	}

}
