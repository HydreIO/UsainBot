package fr.aresrpg.eratz.domain.behavior.craft;

import fr.aresrpg.eratz.domain.behavior.Behavior;
import fr.aresrpg.eratz.domain.dofus.item.Items;
import fr.aresrpg.eratz.domain.player.Perso;

/**
 * 
 * @since
 */
public abstract class CraftBehavior extends Behavior {

	private int quantity;

	/**
	 * @param perso
	 */
	public CraftBehavior(Perso perso, int quantity) {
		super(perso);
		this.quantity = quantity;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	public abstract Items getItemToCraft();

}
