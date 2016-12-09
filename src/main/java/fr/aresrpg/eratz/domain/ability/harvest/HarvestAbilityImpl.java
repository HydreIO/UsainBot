package fr.aresrpg.eratz.domain.ability.harvest;

import fr.aresrpg.eratz.domain.dofus.map.Ressource;
import fr.aresrpg.eratz.domain.player.Perso;

/**
 * 
 * @since
 */
public class HarvestAbilityImpl implements HarvestAbility {

	private Perso perso;

	public HarvestAbilityImpl(Perso perso) {
		this.perso = perso;
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	@Override
	public void harvest(Ressource r) {
		// TODO

	}

}
