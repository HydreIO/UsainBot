package fr.aresrpg.eratz.domain.behavior.harvest;

import fr.aresrpg.eratz.domain.behavior.Behavior;
import fr.aresrpg.eratz.domain.dofus.ressource.Ressources;

import java.util.Set;

/**
 * 
 * @since
 */
public interface HarvestBehavior extends Behavior {

	Set<Ressources> getTypesToHarvest();

	boolean needToDepositAtBank();

}
