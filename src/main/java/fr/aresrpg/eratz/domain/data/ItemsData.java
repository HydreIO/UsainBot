package fr.aresrpg.eratz.domain.data;

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

	public static void main(String[] args) {
		Map<String, Object> m = new HashMap<>();
		m.put("I.u.2410", "{p=5500, s=3, t=16, d=L'Abracaska permet d'éviter les gueules de bois., g=40, fm=true, w=50, ep=1, l=45, wd=true, n=Abracaska}");
		m.put("z", "ceqceqqe");
		m.put("I.t.116", "{t=6, n=Potion de familier}");
		m.put("I.u.51",
				"{c=CS>42&CI>6, d=Cette épée très prisée des jeunes Iops, vous permettra peut-être de faire vos preuves. Mais attention, leur propriétaire, une fois équipé, finit toujours par perdre l'Epée d'Alle., e=false, g=1, fm=true, false=50, ep=1, l=21, wd=true, an=14, n=Petite Epée d'Alle, p=1350, 1=4, t=6, 6=8, w=25, 50=1}");
		instance.init(m);
		System.out.println(instance.names);
	}

}
