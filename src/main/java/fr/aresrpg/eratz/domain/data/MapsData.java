package fr.aresrpg.eratz.domain.data;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @since
 */
public class MapsData {

	private static final MapsData instance = new MapsData();
	private final Map<Integer, Point> coords = new HashMap<>();

	private MapsData() {
		coords.put(5867, new Point(-27, -50));
	}

	public void init(Map<String, Object> datas) {
		for (Entry<String, Object> d : datas.entrySet()) {
			if (d.getKey().length() < 6 || d.getKey().charAt(5) == 'F' || !d.getKey().startsWith("MA.m.")) continue;
			coords.put(parseId(d.getKey()), parseCoords(d.getValue().toString()));
		}
	}

	public static Point getCoords(int mapid) {
		return instance.coords.get(mapid);
	}

	/**
	 * @return the instance
	 */
	public static MapsData getInstance() {
		return instance;
	}

	private Point parseCoords(String data) {
		int xindex = data.lastIndexOf("x=");
		int yindex = data.lastIndexOf("y=");
		int x = Integer.parseInt(data.substring(xindex + 2).split(",")[0]);
		int y = Integer.parseInt(data.substring(yindex + 2).split(",")[0]);
		return new Point(x, y);
	}

	private int parseId(String data) {
		return Integer.valueOf(data.split("\\.")[2]);
	}

	public static void main(String[] args) {
		Map<String, Object> m = new HashMap<>();
		m.put("MA.m.1547", "{x=8, y=2, ep=2, sa=14}");
		m.put("z", "ceqceqqe");
		m.put("I.t.116", "{t=6, n=Potion de familier}");
		instance.init(m);
		System.out.println(instance.coords);
	}

}
