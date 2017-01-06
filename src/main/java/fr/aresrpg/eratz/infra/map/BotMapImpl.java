package fr.aresrpg.eratz.infra.map;

import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @since
 */
public class BotMapImpl implements BotMap {

	private int mapId;
	private String subId;
	private Map<TriggerType, Trigger[]> triggers = new HashMap<>();

	/**
	 * @param mapId
	 * @param subId
	 * @param triggers
	 */
	public BotMapImpl(int mapId, String subId, Map<TriggerType, Trigger[]> triggers) {
		this.mapId = mapId;
		this.subId = subId;
		this.triggers = triggers;
	}

	@Override
	public int getMapId() {
		return mapId;
	}

	@Override
	public String getDate() {
		return subId;
	}

	@Override
	public Trigger[] getTriggers(TriggerType type) {
		return triggers.get(type);
	}

	@Override
	public String toString() {
		return "BotMapImpl [mapId=" + mapId + ", subId=" + subId + ", triggers=" + triggers + "]";
	}

}
