package fr.aresrpg.eratz.domain.util.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @since
 */
public class BlackList {

	@Configured("bots.")
	public static List<String> BOTS = new ArrayList<>();

	private BlackList() {

	}

}
