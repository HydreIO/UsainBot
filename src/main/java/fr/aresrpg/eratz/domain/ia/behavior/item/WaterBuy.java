package fr.aresrpg.eratz.domain.ia.behavior.item;

import fr.aresrpg.commons.domain.util.exception.NotImplementedException;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;

public class WaterBuy extends Behavior {

	private int quantity;

	/**
	 * @param perso
	 */
	public WaterBuy(Perso perso, int quantity) {
		super(perso);
		this.quantity = quantity;
		moveDown();
		throw new NotImplementedException();
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	@Override
	public BehaviorStopReason start() {
		return null;
	}

}
