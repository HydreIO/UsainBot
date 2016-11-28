/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 *  
 * Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.behavior;

import fr.aresrpg.eratz.domain.player.Player;

/**
 * 
 * @since
 */
public interface BaseBehavior {

	boolean acceptDefi(Player pl);

	boolean acceptEchange(Player pl);

	boolean acceptGroup(Player pl);

	boolean acceptCommand(Player pl);

}
