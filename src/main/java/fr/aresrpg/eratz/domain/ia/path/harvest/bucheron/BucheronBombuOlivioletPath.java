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
		addCoord(-10, -22);
		addCoord(-9, -22);
		addCoord(-8, -22);
		addCoord(-8, -23);
		addCoord(-8, -24);
		addCoord(-8, -25);
		addCoord(-8, -26);
		addCoord(-8, -27);
		addCoord(-8, -29);
		addCoord(-8, -30);
		addCoord(-8, -31);
		addCoord(-8, -32);
		addCoord(-9, -32);

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
