package fr.aresrpg.eratz.domain.data.dofus.map;

/**
 * 
 * @since
 */
public enum Zaap {

	ASTRUB(311, 7411, 0),
	BONTA(170, 4263, 0),
	BRAKMAR(597, 5295, 0),
	SCARAFEUILLE(323, 1242, 0),
	PLAINE_ROCHEUSE(165, 3250, 0),
	MILIFUTAIE(186, 528, 0);

	private int cellId;
	private int mapId;
	private int zaapId;

	private Zaap(int cell, int map, int zaapid) {
		this.cellId = cell;
		this.mapId = map;
		this.zaapId = zaapid;
	}

	/**
	 * @return the cellId
	 */
	public int getCellId() {
		return cellId;
	}

	/**
	 * @return the mapId
	 */
	public int getMapId() {
		return mapId;
	}

	/**
	 * @return the zaapId
	 */
	public int getZaapId() {
		return zaapId;
	}

}
