package fr.aresrpg.eratz.infra.map;

import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @since
 */
public class BotMapImpl implements BotMap {

	private int mapId;
	private long time;
	private Map<TriggerType, Trigger[]> triggers = new HashMap<>();
	private ManchouMap map;

	public BotMapImpl(int mapId, long time, Map<TriggerType, Trigger[]> triggers, DofusMap map) {
		this.mapId = mapId;
		this.time = time;
		this.triggers = triggers;
		this.map = ManchouMap.fromDofusMap(map);
	}

	public BotMapImpl(int mapId, long time, Map<TriggerType, Trigger[]> triggers, ManchouMap map) {
		this.mapId = mapId;
		this.time = time;
		this.triggers = triggers;
		this.map = map;
	}

	@Override
	public void setTriggers(TriggerType type, Trigger[] triggers) {
		this.triggers.put(type, triggers);
	}

	/**
	 * @return the mapId
	 */
	public int getMapId() {
		return mapId;
	}

	/**
	 * @param mapId
	 *            the mapId to set
	 */
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * @return the triggers
	 */
	public Map<TriggerType, Trigger[]> getTriggers() {
		return triggers;
	}

	/**
	 * @param triggers
	 *            the triggers to set
	 */
	public void setTriggers(Map<TriggerType, Trigger[]> triggers) {
		this.triggers = triggers;
	}

	/**
	 * @return the map
	 */
	public ManchouMap getMap() {
		return map;
	}

	/**
	 * @param map
	 *            the map to set
	 */
	public void setMap(ManchouMap map) {
		this.map = map;
	}

	@Override
	public Trigger[] getTriggers(TriggerType type) {
		return triggers.get(type);
	}

	@Override
	public String toString() {
		return "BotMapImpl [mapId=" + mapId + ", time=" + time + ", triggers=" + triggers + "]";
	}

	@Override
	public long getTimeMs() {
		return time;
	}

}
