package fr.aresrpg.eratz.domain.ia.behavior.harvest.type.bucheron;

import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.behavior.harvest.HarvestBehavior;

/**
 * 
 * @since
 */
public class NoyerHarvestBehavior extends HarvestBehavior {

	/**
	 * @param perso
	 * @param quantity
	 */
	public NoyerHarvestBehavior(Perso perso) {
		super(perso);
	}

	@Override
	public void init() {
		useExperimentalIA();

		addRessource(Interractable.NOYER);

		experimentPos(6, 14);
		experimentPos(5, 15);
		experimentPos(5, 16);
		experimentPos(5, 17);
		experimentPos(5, 18);
		experimentPos(6, 16);
		experimentPos(6, 17);
		experimentPos(6, 18);
		experimentPos(3, 20);
		experimentPos(3, 21);
		experimentPos(3, 22);
		experimentPos(1, 22);
		experimentPos(3, 31);
		experimentPos(4, 30);
		experimentPos(4, 31);
		experimentPos(4, 32);
		experimentPos(5, 29);
		experimentPos(6, 29);
		experimentPos(6, 30);
		experimentPos(7, 30);
		experimentPos(8, 30);
		experimentPos(9, 30);
	}

}
