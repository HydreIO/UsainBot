package fr.aresrpg.eratz.domain.data.dofus.map;

import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.behavior.craft.CraftBehavior;
import fr.aresrpg.eratz.domain.ia.behavior.craft.type.WheatFlourCraftBehavior;
import fr.aresrpg.eratz.domain.ia.behavior.harvest.HarvestBehavior;
import fr.aresrpg.eratz.domain.ia.behavior.harvest.type.bucheron.BucheronAstrubHarvestBehavior;
import fr.aresrpg.eratz.domain.ia.behavior.harvest.type.bucheron.FreneHarvestBehavior;
import fr.aresrpg.eratz.domain.ia.behavior.harvest.type.paysan.WheatHarvestBehavior;

/**
 * 
 * @since
 */
public enum Path {

	BLE,
	FARINE_BLE,
	FRENE,
	BUCHERON_ASTRUB;

	public HarvestBehavior getHarvestBehavior(Perso perso, int quantiy) {
		switch (this) {
			case BLE:
				return new WheatHarvestBehavior(perso, quantiy);
			case FRENE:
				return new FreneHarvestBehavior(perso, quantiy);
			case BUCHERON_ASTRUB:
				return new BucheronAstrubHarvestBehavior(perso, quantiy);
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
