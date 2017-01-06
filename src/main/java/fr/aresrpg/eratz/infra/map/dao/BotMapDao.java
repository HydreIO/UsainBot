package fr.aresrpg.eratz.infra.map.dao;

/**
 * 
 * @since
 */
public class BotMapDao {

	private int mapid;
	private String date;
	private TriggerDao[] triggers;

	/**
	 * @param mapid
	 * @param date
	 * @param triggers
	 */
	public BotMapDao(int mapid, String date, TriggerDao[] triggers) {
		super();
		this.mapid = mapid;
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
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
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

}
