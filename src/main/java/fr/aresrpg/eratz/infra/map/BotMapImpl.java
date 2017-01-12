package fr.aresrpg.eratz.infra.map;

import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

import java.util.*;

/**
 * 
 * @since
 */
public class BotMapImpl implements BotMap {

	private int mapId;
	private long time;
	private Map<TriggerType, Set<Trigger>> triggers = new HashMap<>();
	private ManchouMap map;

	public BotMapImpl(int mapId, long time, Map<TriggerType, Set<Trigger>> triggers, DofusMap map) {
		this.mapId = mapId;
		this.time = time;
		this.triggers = triggers;
		this.map = ManchouMap.fromDofusMap(map);
	}

	public BotMapImpl(int mapId, long time, Map<TriggerType, Set<Trigger>> triggers, ManchouMap map) {
		this.mapId = mapId;
		this.time = time;
		this.triggers = triggers;
		this.map = map;
	}

	public void fillTriggers() {
		for (TriggerType t : TriggerType.values())
			triggers.put(t, new HashSet<>());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		return obj instanceof BotMap && ((BotMap) obj).getMapId() == mapId;
	}

	@Override
	public int hashCode() {
		return mapId;
	}

	/**
	 * This method remove all zaap and zaapi from Teleporters triggers to only keep them as Interractable trigger ! <br>
	 * In DB we only want Map_tp as teleporters and we keep only the "interractable instance" of zaap and zaapi
	 */
	public void removeDuplicatedTriggers() {
		Set<Trigger> tp = triggers.get(TriggerType.TELEPORT);
		Set<Trigger> inter = triggers.get(TriggerType.INTERRACTABLE);
		if (tp != null && inter != null)
			tp.removeIf(inter::contains);
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
	public Map<TriggerType, Set<Trigger>> getTriggers() {
		return triggers;
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
	public Set<Trigger> getTriggers(TriggerType type) {
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
