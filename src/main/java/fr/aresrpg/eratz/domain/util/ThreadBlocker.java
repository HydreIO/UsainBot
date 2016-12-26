package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.commons.domain.unsafe.UnsafeAccessor;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @since
 */
@SuppressWarnings("deprecation")
public class ThreadBlocker {

	public static final Set<Thread> blockeds = new HashSet<>();
	private final Thread thread;

	public ThreadBlocker(Thread thread) {
		this.thread = thread;
	}

	public void pause() {
		blockeds.add(thread);
		UnsafeAccessor.getUnsafe().monitorEnter(thread);
	}

	public void resume() {
		UnsafeAccessor.getUnsafe().monitorExit(thread);
		blockeds.remove(thread);
	}

}
