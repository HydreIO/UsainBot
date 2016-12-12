package fr.aresrpg.eratz.domain.player;

import fr.aresrpg.eratz.domain.dofus.map.BotMap;

/**
 * 
 * @since
 */
public class MapInfo {

	private final Perso perso;
	private BotMap map;
	private int cellId;

	public MapInfo(Perso perso) {
		this.perso = perso;
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
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

}
