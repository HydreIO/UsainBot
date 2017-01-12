package fr.aresrpg.eratz.domain.data.map.trigger;

import fr.aresrpg.commons.domain.database.Filter;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.map.Destination;
import fr.aresrpg.eratz.infra.map.BotMapImpl;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger.TeleportType;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @since
 */
public class TriggerSniffer {

	private int currentMapId;
	private int currentCellId;

	private TriggerSniffer() {

	}

	public static TriggerSniffer start(int currentMap, int triggerCell) {
		TriggerSniffer ss = new TriggerSniffer();
		ss.currentMapId = currentMap;
		ss.currentCellId = triggerCell;
		return ss;
	}

	private boolean isValid(TeleporterTrigger trigger, Destination dest) {
		return trigger.getDest().equals(dest);
	}

	private void updateTrigger(TeleporterTrigger t, BotMap map) {
		Set<Trigger> triggers = map.getTriggers(t.getType());
		if (triggers.contains(t)) {
			for (Trigger tr : triggers)
				if (tr.equals(t)) ((TeleporterTrigger) tr).setDest(t.getDest());
		} else triggers.add(t);
		BotFather.broadcast(Chat.ADMIN, "Nouveau trigger découvert ! [" + map.getMapId() + ":" + t.getCellId() + "] -> [" + t.getDest().getMapId() + ":" + t.getDest().getCellId() + "]");
		BotFather.MAPS_DB.putOrUpdate(Filter.eq("mapid", map.getMapId()), map.toDao());
	}

	public void complete(Destination dest) {
		BotMap map = MapsManager.getMap(currentMapId);
		Set<Trigger> triggers = map.getTriggers(TriggerType.TELEPORT);
		if (triggers == null) ((BotMapImpl) map).getTriggers().put(TriggerType.TELEPORT, triggers = new HashSet<>());
		for (Trigger t : triggers)
			if (t.getCellId() == currentCellId) {
				if (!isValid((TeleporterTrigger) t, dest)) updateTrigger((TeleporterTrigger) t, map);
				return;
			}
		updateTrigger(new TeleporterTrigger(currentCellId, TeleportType.MAP_TP, dest), map);
	}

}
