package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.tofumanchou.infra.config.Configured;

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
	@Configured("ia.general.cleverbot")
	public static boolean AUTO_SPEAK = true;

}
