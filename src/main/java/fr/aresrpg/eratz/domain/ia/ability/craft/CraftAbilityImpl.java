package fr.aresrpg.eratz.domain.ia.ability.craft;

import fr.aresrpg.eratz.domain.data.player.Perso;

/**
 * 
 * @since
 */
public class CraftAbilityImpl implements CraftAbility {

	private Perso perso;

	public CraftAbilityImpl(Perso perso) {
		this.perso = perso;
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	@Override
	public void placeItem(int id, int quantity) {
		// TODO

	}

	@Override
	public void removeItem(int index) {
		// TODO

	}

	@Override
	public void closeGui() {
		// TODO

	}

	@Override
	public void startCraft(int quantity) {
		// TODO

	}

	@Override
	public void cancelCraft() {
		// TODO

	}

}
