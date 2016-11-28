package fr.aresrpg.eratz.domain.util;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class Threads {

	public static void sleep(long time, TimeUnit unit) {
		try {
			Thread.sleep(unit.toMillis(time));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
