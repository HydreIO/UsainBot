package fr.aresrpg.eratz.domain.behavior.craft;

import fr.aresrpg.eratz.domain.behavior.Behavior;
import fr.aresrpg.eratz.domain.dofus.item.Items;
import fr.aresrpg.eratz.domain.player.Perso;

/**
 * 
 * @since
 */
public abstract class CraftBehavior extends Behavior {

	/**
	 * @param perso
	 */
	public CraftBehavior(Perso perso) {
		super(perso);
	}

	public abstract Items getItemToCraft();

}
