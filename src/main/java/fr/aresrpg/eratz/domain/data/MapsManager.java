package fr.aresrpg.eratz.domain.data;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.database.Collection;
import fr.aresrpg.commons.domain.database.Filter;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.util.PercentPrinter;
import fr.aresrpg.eratz.infra.map.BotMapImpl;
import fr.aresrpg.eratz.infra.map.adapter.BotMapAdapter;
import fr.aresrpg.eratz.infra.map.dao.BotMapDao;
import fr.aresrpg.tofumanchou.domain.Manchou;
import fr.aresrpg.tofumanchou.domain.util.BenchTime;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.ManchouCell;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;
import fr.aresrpg.tofumanchou.infra.db.DbAccessor;

import java.awt.Point;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 
 * @since
 */
public class MapsManager {

	private static final MapsManager instance = new MapsManager();
	private static ConcurrentMap<Integer, BotMap> mapsById = new ConcurrentHashMap<>();
	private static ConcurrentMap<MapNode, BotMap> mapsByCoords = new ConcurrentHashMap<>();

	private MapsManager() {
	}

	public static void init() {
		LOGGER.info("Loading maps..");
		BenchTime t = new BenchTime();
		DbAccessor<BotMapDao> maps = DbAccessor.create(Manchou.getDatabase(), "maps", BotMapDao.class);
		Collection<BotMapDao> coll = maps.get();
		int count = (int) coll.count();
		BotMapDao[] find = coll.find(null, count);
		PercentPrinter printer = new PercentPrinter(find.length);
		for (BotMapDao dao : find) {
			BotMapImpl map = BotMapAdapter.IDENTITY.adaptFrom(dao);
			mapsById.put(dao.getMapid(), map);
			mapsByCoords.put(new MapNode(map.getMap().getX(), map.getMap().getY()), map);
			printer.incrementAndPrint("Loading maps.. [%s%%]");
		}
		LOGGER.info("Maps loaded from database ! (" + t.getAsLong() + "ms)");
	}

	public static BotMap getMap(int mapid) {
		return instance.mapsById.get(mapid);
	}

	public static BotMap getMap(int x, int y) {
		return instance.mapsByCoords.get(new MapNode(x, y));
	}

	public static BotMap getMap(Point coords) {
		return getMap(coords.x, coords.y);
	}

	public static BotMap getMap(ManchouMap map) {
		return getMap(map.getMapid());
	}

	public static void checkUpdate(ManchouMap map) {
		BotMap bm = getMap(map);
		boolean needUpdate = false;
		if (bm == null) {
			bm = new BotMapImpl(map.getMapId(), map.getDate(), new HashMap<>(), new DofusMap(map.getMapId(), map.getDate(), map.getWidth(), map.getHeight(), map.getMusicId(), map.getCapabilities(),
					map.isOutdoor(), map.getBackgroundId(), map.getProtocolCells()));
		}
		ManchouMap map2 = bm.getMap();
		if (map2 == null) {
			((BotMapImpl) bm).setMap(map);
			updateMap(bm);
			return;
		}
		if (map2.getDate() < map.getDate()) needUpdate = true;
		else if (map2.getMusicId() != map.getMusicId()) needUpdate = true;
		else if (map2.getCapabilities() != map.getCapabilities()) needUpdate = true;
		else if (map2.isOutdoor() != map.isOutdoor()) needUpdate = true;
		else if (map2.getBackgroundId() != map.getBackgroundId()) needUpdate = true;
		for (int i = 0; i < map.getCells().length; i++) {
			ManchouCell cell1 = map.getCells()[i];
			ManchouCell cell2 = map2.getCells()[i];
			if (!cell1.fieldsEquals(cell2)) {
				needUpdate = true;
				break;
			}
		}
		if (needUpdate) {
			((BotMapImpl) bm).setMap(map);
			updateMap(bm);
		}
	}

	private static void updateMap(BotMap map) {
		Executors.FIXED.execute(() -> DbAccessor.<BotMapDao> create(Manchou.getDatabase(), "maps", BotMapDao.class).get().putOrUpdate(Filter.eq("mapid", map.getMapId()),
				BotMapAdapter.IDENTITY.adaptTo((BotMapImpl) map)));
	}

	public static class MapNode {
		int x, y;

		public MapNode(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) return false;
			if (obj == this) return true;
			return obj instanceof MapNode && ((MapNode) obj).x == x && ((MapNode) obj).y == y;
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}
	}
}
