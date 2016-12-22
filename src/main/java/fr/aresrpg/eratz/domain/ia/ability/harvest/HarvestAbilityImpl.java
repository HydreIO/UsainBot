package fr.aresrpg.eratz.domain.ia.ability.harvest;

import fr.aresrpg.dofus.structures.Skills;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.object.Ressource;

/**
 * 
 * @since
 */
public class HarvestAbilityImpl implements HarvestAbility {

	private Perso perso;

	public HarvestAbilityImpl(Perso perso) {
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

	@Override
	public void harvest(Ressource r, Skills skill) {
		getPerso().getAbilities().getBaseAbility().interract(skill, r.getCell().getId());
	}

}
