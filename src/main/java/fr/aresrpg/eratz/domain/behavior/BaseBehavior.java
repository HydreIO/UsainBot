/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.behavior;

import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.player.Player;

/**
 * 
 * @since
 */
public abstract class BaseBehavior extends Behavior {

	/**
	 * @param perso
	 */
	public BaseBehavior(Perso perso) {
		super(perso);
	}

	public abstract boolean acceptDefi(Player pl);

	public abstract boolean acceptEchange(Player pl);

	public abstract boolean acceptGroup(Player pl);

	public abstract boolean acceptCommand(Player pl);

	public abstract boolean needToRegen();

	public abstract boolean needToSit();

	public abstract boolean needToDeco();

}
