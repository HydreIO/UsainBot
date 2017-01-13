package fr.aresrpg.eratz.domain.ia.path.harvest.bucheron;

import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.eratz.domain.ia.path.Path;

/**
 * 
 * @since
 */
public class BucheronBombuOlivioletPath extends Path {

	@Override
	protected void init() {
		addRessource(Interractable.BOMBU);
		addRessource(Interractable.OLIVIOLET);
		addMap(3460);
		addMap(3499);
		addMap(3500);
		addMap(3501);
		addMap(3502);
		addMap(3504);
		addMap(3505);
		addMap(3506);
		addMap(3507);
		addMap(3508);
		addMap(3509);
		addMap(3470);
		addMap(3431);
		/*
		 * addCoord(-26, 13);
		 * addCoord(-27, 15);
		 * addCoord(-27, 14);
		 * addCoord(-27, 13);
		 * addCoord(-27, 12);
		 * addCoord(-27, 11);
		 * addCoord(-27, 10);
		 * addCoord(-27, 9);
		 * addCoord(-27, 8);
		 */
	}

}
