package fr.aresrpg.eratz.infra.map.dao;

import java.util.Arrays;

/**
 * 
 * @since
 */
public class BotMapDao {

	private int mapid;
	private int x, y, width, height;
	private long date;
	private TriggerDao[] triggers;

	public BotMapDao(int mapid, int x, int y, int width, int height, long date, TriggerDao[] triggers) {
		this.mapid = mapid;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.date = date;
		this.triggers = triggers;
	}

	/**
	 * @return the mapid
	 */
	public int getMapid() {
		return mapid;
	}

	/**
	 * @param mapid
	 *            the mapid to set
	 */
	public void setMapid(int mapid) {
		this.mapid = mapid;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the date
	 */
	public long getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(long date) {
		this.date = date;
	}

	/**
	 * @return the triggers
	 */
	public TriggerDao[] getTriggers() {
		return triggers;
	}

	/**
	 * @param triggers
	 *            the triggers to set
	 */
	public void setTriggers(TriggerDao[] triggers) {
		this.triggers = triggers;
	}

	@Override
	public String toString() {
		return "BotMapDao [mapid=" + mapid + ", x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", date=" + date + ", triggers=" + Arrays.toString(triggers) + "]";
	}

}
