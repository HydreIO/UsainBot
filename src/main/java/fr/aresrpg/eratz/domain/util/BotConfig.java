package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.tofumanchou.infra.config.Configured;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @since
 */
public class BotConfig {

	@Configured("ia.general.auto_reconnect")
	public static boolean AUTO_RECONNECT = true;
	@Configured("ia.general.reconnect_min_time")
	public static int RECONNECT_MIN = 2000;
	@Configured("ia.general.reconnect_max_time")
	public static int RECONNECT_MAX = 15000;
	@Configured("antibot.bots_to_crash")
	public static List<String> BOT_TO_CRASH = Arrays.asList("Roncillarbre");

}
