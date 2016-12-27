/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.ia.behavior.harvest.type.paysan;

import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.behavior.harvest.HarvestBehavior;

/**
 * 
 * @since
 */
public class WheatHarvestBehavior extends HarvestBehavior {

	/**
	 * @param perso
	 */
	public WheatHarvestBehavior(Perso perso) {
		super(perso);
	}

	@Override
	public void init() {
		addRessource(Interractable.BLE);

		joinCoords(-18, -47);
		switchMove();
		moveLeft();
		moveUp();
		moveLeft();
		moveUp();
		moveUp();
		moveLeft();
		moveDown();
		moveLeft();
		moveLeft();
		moveDown();
		moveRight();
		moveRight();
		moveRight();
		moveDown();
		moveLeft();
		moveLeft();
		moveLeft();
		moveDown();
		moveRight();
		moveRight();
		moveRight();
		moveRight();
		moveDown();
		moveLeft();
		moveLeft();
		moveLeft();
		moveLeft();
		moveDown();
		moveRight();
		moveRight();
		moveDown();
		moveRight();
		moveDown();
		moveDown();
		moveDown();
		moveRight();
		moveUp();
		moveUp();
		moveRight();
		moveDown();
	}

}
