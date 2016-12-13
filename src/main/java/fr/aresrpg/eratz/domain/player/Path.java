package fr.aresrpg.eratz.domain.player;

import fr.aresrpg.eratz.domain.behavior.craft.CraftBehavior;
import fr.aresrpg.eratz.domain.behavior.craft.type.WheatFlourCraftBehavior;
import fr.aresrpg.eratz.domain.behavior.harvest.HarvestBehavior;
import fr.aresrpg.eratz.domain.behavior.harvest.type.*;

/**
 * 
 * @since
 */
public enum Path {

	BLE,
	AVOINE,
	SEIGLE,
	ORGE,
	FARINE_BLE,;

	public HarvestBehavior getHarvestBehavior(Perso perso, int quantiy) {
		switch (this) {
			case AVOINE:
				return new AvoineHarvestBehavior(perso, quantiy);
			case BLE:
				return new WheatHarvestBehavior(perso, quantiy);
			case ORGE:
				return new OrgeHarvestBehavior(perso, quantiy);
			case SEIGLE:
				return new SeigleHarvestBehavior(perso, quantiy);
			default:
				return null;
		}
	}

	public CraftBehavior getCraftBehavior(Perso perso, int quantity) {
		switch (this) {
			case FARINE_BLE:
				return new WheatFlourCraftBehavior(perso, quantity);
			default:
				return null;
		}
	}

}
