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
public class ItemsData {

	private static final ItemsData instance = new ItemsData();
	private final Map<Integer, String> names = new HashMap<>();

	private ItemsData() {
		names.put(39, "Petite Amulette du Hibou");
	}

	public void init(Map<String, Object> datas) {
		for (Entry<String, Object> d : datas.entrySet()) {
			if (d.getKey().length() < 5 || d.getKey().charAt(4) == 'u' || !d.getKey().startsWith("I.u.")) continue;
			names.put(parseId(d.getKey()), parseName(d.getValue().toString()));
		}
	}

	/**
	 * @return the instance
	 */
	public static ItemsData getInstance() {
		return instance;
	}

	public static String getName(int itemid) {
		return instance.names.get(itemid);
	}

	private String parseName(String data) {
		int index = data.lastIndexOf("n=");
		String nam = data.substring(index + 2).split(",")[0];
		if (nam.endsWith("}")) nam = nam.substring(0, nam.length() - 1);
		return nam;
	}

	private int parseId(String data) {
		return Integer.valueOf(data.split("\\.")[2]);
	}

	public static void main(String[] args) throws IOException {
		instance.init(Lang.getDatas("fr", "items"));
		System.out.println(instance.getName(993));
	}

}
