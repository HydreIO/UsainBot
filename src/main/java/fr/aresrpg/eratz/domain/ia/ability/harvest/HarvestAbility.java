/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.ia.ability.harvest;

import fr.aresrpg.eratz.domain.util.Closeable;
import fr.aresrpg.tofumanchou.domain.data.enums.Skills;
import fr.aresrpg.tofumanchou.infra.data.Ressource;

/**
 * 
 * @since
 */
public interface HarvestAbility extends Closeable {

	void harvest(Ressource r, Skills skill);

}
