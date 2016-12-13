package fr.aresrpg.eratz.domain.player;

/**
 * 
 * @since
 */
public abstract class Info {

	private Perso perso;

	/**
	 * @param perso
	 */
	public Info(Perso perso) {
		this.perso = perso;
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	@Override
	public String toString() {
		return "Info [perso=" + perso + "]";
	}

}
