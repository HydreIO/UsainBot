package fr.aresrpg.eratz.domain.util.config;

import fr.aresrpg.eratz.domain.util.config.dao.PlayerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @since
 */
public class Variables {

	@Configured("accounts.")
	public static List<PlayerBean> ACCOUNTS = new ArrayList<>();

	private Variables() {

	}

}
