package fr.aresrpg.eratz.domain.data;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.database.Collection;
import fr.aresrpg.commons.domain.database.Filter;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 
 * @since
 */
public class MapsManager {

	private static final MapsManager instance = new MapsManager();
	private static ConcurrentMap<Integer, BotMap> mapsById = new ConcurrentHashMap<>();

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
			printer.incrementAndPrint("Loading maps.. [%s%%]");
		}
		LOGGER.info("Maps loaded from database ! (" + t.getAsLong() + "ms)");
	}

	public static BotMap getMap(int mapid) {
		return instance.mapsById.get(mapid);
	}

	public static synchronized BotMap getOrCreateMap(ManchouMap map) {
		BotMap map2 = getMap(map.getMapid());
		if (map2 == null) map2 = instance.createAndRegisterMap(map);
		return map2;
	}

	private BotMap createAndRegisterMap(ManchouMap map) {
		BotMap bm = new BotMapImpl(map.getMapId(), map.getDate(), new HashMap<>(), map);
		mapsById.put(map.getMapid(), bm);
		return bm;
	}

	public static void checkUpdate(ManchouMap map, BotPerso perso) {
		BotMap bm = getMap(map.getMapid());
		if (bm == null) {
			LOGGER.error("MAP NULL ON CREATE");
			bm = getOrCreateMap(map);
			updateMap(bm, perso);
			return;
		}
		boolean needUpdate = false;
		ManchouMap map2 = bm.getMap();
		if (map2.getDate() < map.getDate()) needUpdate = true;
		else if (map2.getMusicId() != map.getMusicId()) needUpdate = true;
		else if (map2.getCapabilities() != map.getCapabilities()) needUpdate = true;
		else if (map2.isOutdoor() != map.isOutdoor()) needUpdate = true;
		else if (map2.getBackgroundId() != map.getBackgroundId()) needUpdate = true;
		Set<Trigger> triggers = bm.getTriggers(TriggerType.TELEPORT);
		int faketrigCount = 0;
		for (int i = 0; i < map.getCells().length; i++) {
			ManchouCell cell1 = map.getCells()[i];
			ManchouCell cell2 = map2.getCells()[i];
			boolean fakeTrig = triggerExist(i, triggers) && !cell1.isTeleporter();
			if (fakeTrig || !cell1.fieldsEquals(cell2)) {
				if (fakeTrig) {
					faketrigCount++;
					removeTrigger(i, triggers);
				}
				needUpdate = true;
			}
		}
		if (needUpdate) {
			((BotMapImpl) bm).setMap(map);
			if (faketrigCount != 0) BotFather.broadcast(Chat.ADMIN, faketrigCount + " faux triggers " + (faketrigCount == 1 ? "a" : "onts") + " été suprimé ! [" + bm.getMap().getInfos() + "]");
			updateMap(bm, perso);
		}
	}

	private static boolean triggerExist(int cellid, Set<Trigger> triggers) {
		if (triggers == null) return false;
		for (Trigger t : triggers) {
			if (t.getCellId() == cellid) return true;
		}
		return false;
	}

	private static void removeTrigger(int cellid, Set<Trigger> triggers) {
		if (triggers == null) return;
		for (Trigger t : triggers)
			if (t.getCellId() == cellid) {
				triggers.remove(t);
				return;
			}
	}

	private static void updateMap(BotMap map, BotPerso perso) {
		BotFather.broadcast(Chat.ADMIN, perso.getPerso().getPseudo() + " a mis une map à jour [" + map.getMap().getInfos() + "]");
		Executors.FIXED.execute(() -> DbAccessor.<BotMapDao>create(Manchou.getDatabase(), "maps", BotMapDao.class).get().putOrUpdate(Filter.eq("mapid", map.getMapId()),
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
