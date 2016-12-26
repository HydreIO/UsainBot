package fr.aresrpg.eratz.domain.ia.behavior.harvest.type.bucheron;

import fr.aresrpg.dofus.structures.Skills;
import fr.aresrpg.eratz.domain.data.dofus.ressource.Interractable;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.behavior.harvest.HarvestBehavior;

/**
 * 
 * @since
 */
public class FreneHarvestBehavior extends HarvestBehavior {

	/**
	 * @param perso
	 * @param quantity
	 */
	public FreneHarvestBehavior(Perso perso, int quantity) {
		super(perso, quantity);
	}

	@Override
	public void initMoves() {
		useExperimentalIA();
		experimentPos(3, -24);
		experimentPos(3, -25);
		experimentPos(3, -26);
		experimentPos(3, -27);
		experimentPos(2, -28);
		experimentPos(2, -27);
		experimentPos(2, -26);
		experimentPos(2, -25);
		experimentPos(2, -24);
		experimentPos(2, -23);
		experimentPos(1, -23);
		experimentPos(1, -24);
		experimentPos(1, -25);
		experimentPos(1, -26);
		experimentPos(1, -27);
		experimentPos(1, -28);
		experimentPos(0, -27);
		experimentPos(0, -24);
		experimentPos(0, -23);
		experimentPos(1, -24);
		experimentPos(-1, -25);
		experimentPos(-1, -26);
		experimentPos(-1, -28);
		experimentPos(-2, -29);
		experimentPos(-2, -28);
		experimentPos(-2, -27);
		experimentPos(-2, -26);
		experimentPos(-2, -25);
		experimentPos(-2, -24);
	}

	@Override
	public Skills getSkill(Interractable type) {
		return Skills.COUPER_FRENE;
	}

	@Override
	public Interractable[] getTypesToHarvest() {
		return new Interractable[] { Interractable.FRENE };
	}

}
