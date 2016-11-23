/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 *  
 * Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.behavior.move;

import fr.aresrpg.eratz.domain.dofus.map.Map;
import fr.aresrpg.eratz.domain.dofus.mob.AgressiveMobs;

/**
 * 
 * @since
 */
public interface Navigation {

	Map getCurrentMap();

	Map getNextMap();

	void moveTo(Map map);

	void avoidMob(AgressiveMobs... type);

}
