/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.util.concurrent;

import fr.aresrpg.commons.domain.util.schedule.Scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 
 * @since
 */
public class Executors {

	public static ExecutorService FIXED = java.util.concurrent.Executors.newFixedThreadPool(40);
	public static ScheduledExecutorService SCHEDULED = java.util.concurrent.Executors.newScheduledThreadPool(40);
	public static Scheduler SCHEDULER = new Scheduler(SCHEDULED);
}
