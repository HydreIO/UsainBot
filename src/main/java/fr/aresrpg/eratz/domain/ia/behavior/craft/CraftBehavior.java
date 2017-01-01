package fr.aresrpg.eratz.domain.ia.behavior.craft;

import fr.aresrpg.eratz.domain.data.player.BotPerso;
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
	public CraftBehavior(BotPerso perso, int quantity) {
		super(perso);
		this.quantity = quantity;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

}
