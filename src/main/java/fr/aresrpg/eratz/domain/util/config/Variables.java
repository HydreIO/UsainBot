package fr.aresrpg.eratz.domain.util.config;

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

	private Variables() {

	}

}
