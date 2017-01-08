package fr.aresrpg.eratz.infra.map.dao;

import java.util.Arrays;

/**
 * 
 * @since
 */
public class BotMapDao {

	private int mapid;
	private long date;
	private int x;
	private int y;
	private int width;
	private int height;
	private int musicId;
	private int capabilities;
	private boolean outdoor;
	private int background;
	private TriggerDao[] triggers;
	private String datas;

	/**
	 * @param mapid
	 * @param date
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param musicId
	 * @param capabilities
	 * @param outdoor
	 * @param background
	 * @param triggers
	 * @param cells
	 */
	public BotMapDao(int mapid, long date, int x, int y, int width, int height, int musicId, int capabilities, boolean outdoor, int background, TriggerDao[] triggers, String cells) {
		this.mapid = mapid;
		this.date = date;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.musicId = musicId;
		this.capabilities = capabilities;
		this.outdoor = outdoor;
		this.background = background;
		this.triggers = triggers;
		this.datas = cells;
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
	 * @return the musicId
	 */
	public int getMusicId() {
		return musicId;
	}

	/**
	 * @param musicId
	 *            the musicId to set
	 */
	public void setMusicId(int musicId) {
		this.musicId = musicId;
	}

	/**
	 * @return the capabilities
	 */
	public int getCapabilities() {
		return capabilities;
	}

	/**
	 * @param capabilities
	 *            the capabilities to set
	 */
	public void setCapabilities(int capabilities) {
		this.capabilities = capabilities;
	}

	/**
	 * @return the outdoor
	 */
	public boolean isOutdoor() {
		return outdoor;
	}

	/**
	 * @param outdoor
	 *            the outdoor to set
	 */
	public void setOutdoor(boolean outdoor) {
		this.outdoor = outdoor;
	}

	/**
	 * @return the background
	 */
	public int getBackground() {
		return background;
	}

	/**
	 * @param background
	 *            the background to set
	 */
	public void setBackground(int background) {
		this.background = background;
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

	/**
	 * @return the cells
	 */
	public String getCells() {
		return datas;
	}

	/**
	 * @param cells
	 *            the cells to set
	 */
	public void setCells(String cells) {
		this.datas = cells;
	}

	@Override
	public String toString() {
		return "BotMapDao [mapid=" + mapid + ", date=" + date + ", x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", musicId=" + musicId + ", capabilities=" + capabilities
				+ ", outdoor=" + outdoor + ", background=" + background + ", triggers=" + Arrays.toString(triggers) + ", datas=" + datas + "]";
	}

}
