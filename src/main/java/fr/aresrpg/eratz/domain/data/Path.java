package fr.aresrpg.eratz.domain.data;

import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.behavior.craft.CraftBehavior;
import fr.aresrpg.eratz.domain.ia.behavior.craft.type.WheatFlourCraftBehavior;
import fr.aresrpg.eratz.domain.ia.behavior.harvest.HarvestBehavior;
import fr.aresrpg.eratz.domain.ia.behavior.harvest.type.bucheron.*;
import fr.aresrpg.eratz.domain.ia.behavior.harvest.type.paysan.WheatHarvestBehavior;

/**
 * 
 * @since
 */
public enum Path {

	BLE,
	FARINE_BLE,
	FRENE,
	BUCHERON_ASTRUB,
	BUCHERON_AMAKNA,
	NOYER,;

	public HarvestBehavior getHarvestBehavior(BotPerso perso) {
		switch (this) {
			case BLE:
				return new WheatHarvestBehavior(perso);
			case FRENE:
				return new FreneHarvestBehavior(perso);
			case BUCHERON_ASTRUB:
				return new BucheronAstrubHarvestBehavior(perso);
			case NOYER:
				return new NoyerHarvestBehavior(perso);
			case BUCHERON_AMAKNA:
				return new BucheronAmaknaHarvestBehavior(perso);
			default:
				return null;
		}
	}

	public CraftBehavior getCraftBehavior(BotPerso perso, int quantity) {
		switch (this) {
			case FARINE_BLE:
				return new WheatFlourCraftBehavior(perso, quantity);
			default:
				return null;
		}
	}

}
