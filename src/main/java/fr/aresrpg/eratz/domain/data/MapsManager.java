package fr.aresrpg.eratz.domain.data;

import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.eratz.domain.dofus.map.BotMap;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 
 * @since
 */
public class MapsManager {

	private static MapsManager INSTANCE = new MapsManager();
	private ConcurrentMap<Integer, BotMap> maps = new ConcurrentHashMap<>();

	private MapsManager() {

	}

	public static BotMap getMap(int mapid) {
		return INSTANCE.getMaps().get(mapid);
	}

	public static void updateMap(BotMap map) {
		INSTANCE.getMaps().put(map.getDofusMap().getId(), map);
	}

	public static BotMap getOrCreate(DofusMap defaultMap) {
		BotMap map = getMap(defaultMap.getId());
		if (map == null) map = BotMap.fromDofusMap(defaultMap);
		updateMap(map);
		return map;
	}

	/**
	 * Empty the cache to free memory
	 */
	public static void clearMaps() {
		INSTANCE.maps.clear();
	}

	/**
	 * @return the maps
	 */
	public ConcurrentMap<Integer, BotMap> getMaps() {
		return maps;
	}

	/**
	 * @return the iNSTANCE
	 */
	public static MapsManager getInstance() {
		return INSTANCE;
	}

}