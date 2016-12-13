package fr.aresrpg.eratz.domain.player;

import fr.aresrpg.eratz.domain.dofus.map.BotMap;

/**
 * 
 * @since
 */
public class MapInfo extends Info {

	private BotMap map;
	private int cellId;

	public MapInfo(Perso perso) {
		super(perso);
	}

	/**
	 * @return the map
	 */
	public BotMap getMap() {
		return map;
	}

	/**
	 * @param map
	 *            the map to set
	 */
	public void setMap(BotMap map) {
		this.map = map;
	}

	/**
	 * @return the cellId
	 */
	public int getCellId() {
		return cellId;
	}

	/**
	 * @param cellId
	 *            the cellId to set
	 */
	public void setCellId(int cellId) {
		this.cellId = cellId;
	}

	@Override
	public String toString() {
		return "MapInfo [map=" + map + ", cellId=" + cellId + ", " + super.toString() + "]";
	}

}
