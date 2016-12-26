/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.ia.behavior.harvest.type;

import fr.aresrpg.dofus.structures.Skills;
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
	public Skills getSkill() {
		return Skills.FAUCHER_BLE;
	}

	@Override
	public void initMoves() {
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
