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
	private long time;
	private Map<TriggerType, Trigger[]> triggers = new HashMap<>();
	private int x;
	private int y;
	private int width;
	private int height;

	public BotMapImpl(int mapId, long subId, Map<TriggerType, Trigger[]> triggers, int x, int y, int width, int height) {
		this.mapId = mapId;
		this.time = subId;
		this.triggers = triggers;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public long getTimeMs() {
		return time;
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
	 * @param mapId
	 *            the mapId to set
	 */
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public int getMapId() {
		return mapId;
	}

	@Override
	public Trigger[] getTriggers(TriggerType type) {
		return triggers.get(type);
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public String toString() {
		return "BotMapImpl [mapId=" + mapId + ", time=" + time + ", triggers=" + triggers + ", x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
	}
}
