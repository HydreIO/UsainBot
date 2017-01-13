package fr.aresrpg.eratz.domain.ia.path.harvest.bucheron;

import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.eratz.domain.ia.path.Path;

/**
 * 
 * @since
 */
public class BucheronAmaknaPath extends Path {

	@Override
	protected void init() {
		addRessource(Interractable.CHATAIGNIER);
		addRessource(Interractable.MERISIER);
		addRessource(Interractable.CHENE);
		addRessource(Interractable.NOYER);
		addRessource(Interractable.CHARME);
		addRessource(Interractable.IF);
		addRessource(Interractable.ERABLE);
		addRessource(Interractable.ORME);
		addRessource(Interractable.EBENE);
	}

}
