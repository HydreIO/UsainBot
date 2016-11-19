package fr.aresrpg.eratz.domain.util.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 
 * @since
 */
public class Executors {

	public static ExecutorService FIXED = java.util.concurrent.Executors.newFixedThreadPool(30);
	public static ScheduledExecutorService SCHEDULED = java.util.concurrent.Executors.newScheduledThreadPool(30);
}
