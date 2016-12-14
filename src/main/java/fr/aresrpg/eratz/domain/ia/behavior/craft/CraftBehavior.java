package fr.aresrpg.eratz.domain.ia.behavior.craft;

import fr.aresrpg.eratz.domain.data.ItemsData;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;

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

	public abstract ItemsData getItemToCraft();

}
