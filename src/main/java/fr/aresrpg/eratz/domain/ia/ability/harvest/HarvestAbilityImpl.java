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
	public void harvest(Ressource r) {
		Skills s = null;
		switch (r.getType()) {
			case BLE:
				s = Skills.FAUCHER_BLE;
				break;
			case ARGENT:
				s = Skills.COLLECTER_ARGENT;
				break;
			case AVOINE:
				s = Skills.FAUCHER_AVOINE;
				break;
			case BAMBOU:
				s = Skills.COUPER_BAMBOU;
				break;
			case BAMBOU_SACREE:
				s = Skills.COUPER_BAMBOU_SACRE;
				break;
			case BAMBOU_SOMBRE:
				s = Skills.COUPER_BAMBOU_SOMBRE;
				break;
		}
	}

}
