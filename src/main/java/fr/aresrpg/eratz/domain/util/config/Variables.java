package fr.aresrpg.eratz.domain.util.config;

import fr.aresrpg.eratz.domain.util.config.dao.GroupBean;
import fr.aresrpg.eratz.domain.util.config.dao.PlayerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @since
 */
public class Variables {

	@Configured("internal.options.")
	public static boolean CONNECT_BOT_ON_CLIENT_DECONNECTION = false;
	@Configured("internal.")
	public static String IP_MACHINE = "127.0.0.1";
	@Configured("internal.")
	public static int PORT_BOT = 2727;
	@Configured("bot.ia.fight.")
	public static boolean HUMAN_FIGHT = true;
	@Configured("internal.recover.")
	public static boolean AUTO_RECONNECTION = true;
	@Configured("internal.recover.wait.")
	public static int SEC_AFTER_CRASH = 30; // si la connection crash on se reco dans X seconds
	@Configured("internal.recover.wait.")
	public static int MIN_SEC_AFTER_NORMAL = 4 * 60; // temps minimum en cas de déconnection "pause" pour eviter de trop apparaitre comme une machine
	@Configured("internal.recover.wait.")
	public static int MAX_SEC_AFTER_NORMAL = 6 * 60; // temps maximum en cas de déconnection "pause" pour eviter de trop apparaitre comme une machine
	@Configured("accounts.")
	public static List<PlayerBean> ACCOUNTS = new ArrayList<>();
	@Configured("groups.")
	public static List<GroupBean> GROUPS = new ArrayList<>();

	private Variables() {

	}

}
