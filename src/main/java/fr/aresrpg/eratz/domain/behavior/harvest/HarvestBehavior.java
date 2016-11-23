/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 *  
 * Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.behavior.harvest;

import fr.aresrpg.eratz.domain.BaseBehavior;
import fr.aresrpg.eratz.domain.dofus.ressource.Ressource;
import fr.aresrpg.eratz.domain.player.Player;

/**
 * 
 * @since
 */
public interface HarvestBehavior extends BaseBehavior {

	@Override
	default boolean acceptDefi(Player pl) {
		return false;
	}

	void harvest(Ressource r);

}
