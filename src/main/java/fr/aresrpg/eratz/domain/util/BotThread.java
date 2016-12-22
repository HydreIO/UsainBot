package fr.aresrpg.eratz.domain.util;

import java.util.concurrent.locks.LockSupport;

/**
 * 
 * @since
 */
public class BotThread {

	private Thread thread;

	public void pause(Thread thread) {
		this.thread = thread;
		LockSupport.park();
	}

	public void pause() {
		pause(thread.currentThread());
	}

	public void unpause() {
		if (thread != null) LockSupport.unpark(thread);
	}

}
