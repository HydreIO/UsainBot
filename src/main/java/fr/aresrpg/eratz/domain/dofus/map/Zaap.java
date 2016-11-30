package fr.aresrpg.eratz.domain.dofus.map;

/**
 * 
 * @since
 */
public enum Zaap {

	ASTRUB(311, 7411, 4, -19),
	BONTA(170, 4263, -32, -58),
	BRAKMAR(597, 5295, -25, 40),
	SCARAFEUILLE(323, 1242, -1, 24),
	PLAINE_ROCHEUSE(165, 3250, -14, -47),
	MILIFUTAIE(186, 528, 5, 7);

	private int cellId;
	private int mapId;
	private int x, z;

	private Zaap(int cell, int map, int x, int z) {
		this.cellId = cell;
		this.mapId = map;
		this.x = x;
		this.z = z;
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
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the z
	 */
	public int getZ() {
		return z;
	}

}
