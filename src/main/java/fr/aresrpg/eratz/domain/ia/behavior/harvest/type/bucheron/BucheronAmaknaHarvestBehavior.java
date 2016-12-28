/*******************************************************************************
 * BotFather (C) - Dofus 1.29 bot
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.ia.behavior.harvest.type.bucheron;

import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.behavior.harvest.HarvestBehavior;

/**
 * 
 * @since
 */
public class BucheronAmaknaHarvestBehavior extends HarvestBehavior {

	public BucheronAmaknaHarvestBehavior(Perso perso) {
		super(perso);
	}

	@Override
	public void init() {
		useExperimentalIA();

		addRessource(Interractable.CHATAIGNIER);
		//addRessource(Interractable.FRENE);
		addRessource(Interractable.MERISIER);
		addRessource(Interractable.CHENE);
		addRessource(Interractable.NOYER);
		addRessource(Interractable.CHARME);
		addRessource(Interractable.IF);
		addRessource(Interractable.ERABLE);
		addRessource(Interractable.ORME);
		addRessource(Interractable.EBENE);

		experimentPos(1, 2);
		experimentPos(1, 4);
		experimentPos(4, 4);
		experimentPos(4, 5);
		experimentPos(10, 5);
		experimentPos(10, 9);
		experimentPos(11, 9);
		experimentPos(12, 9);
		experimentPos(4, 7);
		experimentPos(5, 8);
		experimentPos(4, 9);
		experimentPos(0, 8);
		experimentPos(1, 8);
		experimentPos(2, 8);
		experimentPos(3, 8);
		experimentPos(0, 9);
		experimentPos(1, 9);
		experimentPos(2, 9);
		experimentPos(3, 9);
		experimentPos(4, 11);
		experimentPos(5, 11);
		experimentPos(9, 12);
		experimentPos(13, 13);
		experimentPos(13, 14);
		experimentPos(13, 15);
		experimentPos(1, 13);
		experimentPos(14, 19);
		experimentPos(14, 20);
		experimentPos(14, 21);
		experimentPos(13, 20);
		experimentPos(13, 21);
		experimentPos(10, 20);
		experimentPos(9, 21);
		experimentPos(6, 14);
		experimentPos(2, 15);
		experimentPos(5, 15);
		experimentPos(6, 15);
		experimentPos(1, 16);
		experimentPos(2, 16);
		experimentPos(3, 16);
		experimentPos(4, 16);
		experimentPos(5, 16);
		experimentPos(6, 16);
		experimentPos(5, 17);
		experimentPos(6, 17);
		experimentPos(7, 17);
		experimentPos(5, 18);
		experimentPos(6, 18);
		experimentPos(7, 18);
		experimentPos(7, 19);
		experimentPos(7, 20);
		experimentPos(3, 20);
		experimentPos(3, 21);
		experimentPos(4, 21);
		experimentPos(1, 22);
		experimentPos(3, 22);
		experimentPos(4, 22);
		experimentPos(3, 23);
		experimentPos(4, 29);
		experimentPos(5, 29);
		experimentPos(6, 29);
		experimentPos(7, 29);
		experimentPos(8, 29);
		experimentPos(9, 29);
		experimentPos(10, 29);
		experimentPos(0, 30);
		experimentPos(1, 30);
		experimentPos(2, 30);
		experimentPos(3, 30);
		experimentPos(4, 30);
		experimentPos(5, 30);
		experimentPos(6, 30);
		experimentPos(7, 30);
		experimentPos(8, 30);
		experimentPos(9, 30);
		experimentPos(2, 31);
		experimentPos(3, 31);
		experimentPos(4, 31);
		experimentPos(5, 31);
		experimentPos(6, 31);
		experimentPos(7, 31);
		experimentPos(8, 31);
		experimentPos(4, 32);
		experimentPos(1, 32);
		experimentPos(0, 32);
		experimentPos(-1, 32);
		experimentPos(-1, 33);
		experimentPos(0, 33);
	}

}
