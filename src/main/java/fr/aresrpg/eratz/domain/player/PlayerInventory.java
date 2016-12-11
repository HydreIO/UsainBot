package fr.aresrpg.eratz.domain.player;

/**
 * 
 * @since
 */
public class PlayerInventory extends Inventory {

	private Perso perso;

	/**
	 * @param perso
	 */
	public PlayerInventory(Perso perso) {
		this.perso = perso;
	}

	@Override
	public String toString() {
		return "PlayerInventory [perso=" + perso + "]" + super.toString();
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

}
