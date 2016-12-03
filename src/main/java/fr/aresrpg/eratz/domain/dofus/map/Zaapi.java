package fr.aresrpg.eratz.domain.dofus.map;

/**
 * 
 * @since
 */
public enum Zaapi {

	BRAKMAR_MILICE(184, City.BRAKMAR),
	BONTA_BANQUE(456, City.BONTA),
	BONTA_MILICE(254, City.BONTA);

	private int cellid;
	private City city;

	private Zaapi(int cellid, City city) {
		this.cellid = cellid;
		this.city = city;
	}

	/**
	 * @return the city
	 */
	public City getCity() {
		return city;
	}

	/**
	 * @return the cellid
	 */
	public int getCellid() {
		return cellid;
	}
}
