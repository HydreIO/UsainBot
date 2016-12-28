/*******************************************************************************
 * BotFather (C) - Dofus 1.29 bot
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.data;

import fr.aresrpg.dofus.util.Lang;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @since
 */
public class SpellsData {

	private static final SpellsData instance = new SpellsData();
	private final Map<Integer, LangSpell> items = new HashMap<>();

	private SpellsData() {
	}

	public void init() throws IOException {
		Map<String, Object> datas = Lang.getDatas("fr", "spells");
		for (Entry<String, Object> d : datas.entrySet()) {
			if (d.getKey().length() < 5 || d.getKey().charAt(4) == 'u' || !d.getKey().startsWith("I.u.")) continue;
			Map<String, Object> vv = (Map<String, Object>) d.getValue();
			int id = parseId(d.getKey());
			int cate = Integer.parseInt(vv.get("t").toString());
			String name = vv.get("n").toString();
			String desc = vv.get("d").toString();
			int pods = Integer.parseInt(vv.get("w").toString());
			//	items.put(id, new LangItem(id, cate, name, desc, pods));
		}
	}

	/**
	 * @return the instance
	 */
	public static SpellsData getInstance() {
		return instance;
	}

	private int parseId(String data) {
		return Integer.valueOf(data.split("\\.")[2]);
	}

	public static class LangSpell {

	}

}
