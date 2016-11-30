package fr.aresrpg.eratz.domain.behavior.craft;

import fr.aresrpg.dofus.structures.character.Item;
import fr.aresrpg.eratz.domain.behavior.BaseBehavior;
import fr.aresrpg.eratz.domain.player.Perso;

/**
 * 
 * @since
 */
public abstract class CraftBehavior extends BaseBehavior {

	/**
	 * @param perso
	 */
	public CraftBehavior(Perso perso) {
		super(perso);
	}

	public abstract Item getItemToCraft();

	public abstract int getQuantityToCraft();
}
