package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.eratz.domain.TheBotFather;

import java.util.HashSet;
import java.util.Set;

/**
 * Met un thread en pls grace à des incantation magique
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
		TheBotFather.LOGGER.warning("Suspend " + thread.getName());
		thread.suspend();
		//UnsafeAccessor.getUnsafe().monitorEnter(thread);
	}

	public void resume() {
		TheBotFather.LOGGER.warning("Resume " + thread.getName());
		thread.resume();
		//UnsafeAccessor.getUnsafe().monitorExit(thread);
		blockeds.remove(thread);
	}

}
