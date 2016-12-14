package fr.aresrpg.eratz.domain.data.dofus.player;

import fr.aresrpg.eratz.domain.data.dofus.map.City;

/**
 * 
 * @since
 */
public enum BotPopo {

	PAIN(0),
	RAPPEL(1),
	BONTA(2),
	BRAK(3);

	private int slot; // slot de l'inventaire rapide

	private BotPopo(int slot) {
		this.slot = slot;
	}

	/**
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}

	public static BotPopo byCity(City city) {
		if (city == City.BONTA) return BotPopo.BONTA;
		return BotPopo.BRAK;
	}

}
