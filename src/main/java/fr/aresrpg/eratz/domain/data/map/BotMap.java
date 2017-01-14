package fr.aresrpg.eratz.domain.data.map;

import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;
import fr.aresrpg.eratz.domain.util.BotNode;
import fr.aresrpg.eratz.infra.map.BotMapImpl;
import fr.aresrpg.eratz.infra.map.DestinationImpl;
import fr.aresrpg.eratz.infra.map.adapter.BotMapAdapter;
import fr.aresrpg.eratz.infra.map.dao.BotMapDao;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger.TeleportType;
import fr.aresrpg.tofumanchou.domain.data.enums.Zaap;
import fr.aresrpg.tofumanchou.domain.data.enums.Zaapi;
import fr.aresrpg.tofumanchou.infra.data.ManchouCell;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

import java.util.Set;

/**
 * 
 * @since
 */
public interface BotMap {

	int getMapId();

	long getTimeMs();

	Set<Trigger> getTriggers(TriggerType type);

	ManchouMap getMap();

	default BotNode toNode(TeleporterTrigger triggerUsed) {
		return new BotNode(getMap().getX(), getMap().getY(), 0, triggerUsed);
	}

	default BotNode toZaapiNode(int currentCell, int destMap) {
		return new BotNode(getMap().getX(), getMap().getY(), 0,
				new TeleporterTrigger(currentCell, TeleportType.ZAAPI, new DestinationImpl(destMap, Zaapi.getWithMap(destMap).findCellId(MapsManager.getMap(destMap).getMap().getCells()))));
	}

	default BotNode toZaapNode(int currentCell, int destMap) {
		return new BotNode(getMap().getX(), getMap().getY(), 0,
				new TeleporterTrigger(currentCell, TeleportType.ZAAP, new DestinationImpl(destMap, Zaap.getWithMap(destMap).findCellId(MapsManager.getMap(destMap).getMap().getCells()))));
	}

	default boolean hasRessource(Interractable ress) {
		for (ManchouCell c : getMap().getCells())
			if (c.getInterractable() == ress) return true;
		return false;
	}

	default boolean hasOne(Interractable... ress) {
		for (ManchouCell c : getMap().getCells()) {
			Interractable interractable = c.getInterractable();
			if (interractable == null) continue;
			if (ArrayUtils.contains(interractable, ress)) return true;
		}
		return false;
	}

	default int distance(BotMap map) {
		int xfrom = getMap().getX();
		int yfrom = getMap().getY();
		int xto = map.getMap().getX();
		int yto = map.getMap().getY();
		return Math.abs(xto - xfrom) + Math.abs(yto - yfrom);
	}

	default BotMapDao toDao() {
		return BotMapAdapter.IDENTITY.adaptTo((BotMapImpl) this);
	}

}
