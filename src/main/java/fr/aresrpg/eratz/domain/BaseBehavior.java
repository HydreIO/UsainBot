package fr.aresrpg.eratz.domain;

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
