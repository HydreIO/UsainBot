package fr.aresrpg.eratz.domain.behavior.harvest;

import fr.aresrpg.eratz.domain.behavior.Behavior;
import fr.aresrpg.eratz.domain.dofus.ressource.Ressources;
import fr.aresrpg.eratz.domain.player.Perso;

import java.util.Set;

/**
 * 
 * @since
 */
public abstract class HarvestBehavior extends Behavior {

	/**
	 * @param perso
	 */
	public HarvestBehavior(Perso perso) {
		super(perso);
	}

	public abstract Set<Ressources> getTypesToHarvest();

}
