/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.ia.behavior.harvest.type;

import fr.aresrpg.eratz.domain.data.dofus.ressource.Interractable;
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
	public WheatHarvestBehavior(Perso perso, int quantity) {
		super(perso, quantity);
	}

	@Override
	public Interractable getTypesToHarvest() {
		return Interractable.BLE;
	}

	@Override
	public void initMoves() {
		moveLeft(5);
		moveUp();
		moveLeft();
		moveUp(3);
		moveLeft(4);
		moveUp(12);
		moveLeft(5);
		moveUp();
		moveLeft(3);
		moveUp(11);
		moveLeft(5);
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
