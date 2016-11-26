package fr.aresrpg.eratz.domain.dofus;

/**
 * 
 * @since
 */
public enum Server {

	ERATZ(601),
	HENUAL(602);

	private int id;

	private Server(int id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
}
