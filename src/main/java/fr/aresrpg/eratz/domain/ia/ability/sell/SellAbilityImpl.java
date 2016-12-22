package fr.aresrpg.eratz.domain.ia.ability.sell;

import fr.aresrpg.eratz.domain.data.player.Perso;

/**
 * 
 * @since
 */
public class SellAbilityImpl implements SellAbility {

	private Perso perso;

	public SellAbilityImpl(Perso perso) {
		this.perso = perso;
	}

	@Override
	public void shutdown() {
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

}
