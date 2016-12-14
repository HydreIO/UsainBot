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
public class AvoineHarvestBehavior extends HarvestBehavior {

	/**
	 * @param perso
	 */
	public AvoineHarvestBehavior(Perso perso, int quantity) {
		super(perso, quantity);
	}

	@Override
	public Interractable getTypesToHarvest() {
		return Interractable.AVOINE;
	}

	@Override
	public void initMoves() {
	}

	/*
	 * @Override
	 * public void start() {
	 * BaseAbility ab = getPerso().getBaseAbility();
	 * Navigation na = getPerso().getNavigation();
	 * ab.goToZaap(Zaap.PLAINE_ROCHEUSE);
	 * na.moveLeft(5);
	 * if (!harvestMap()) return;
	 * na.moveDown(5);
	 * if (!harvestMap()) return;
	 * na.moveRight().moveDown();
	 * if (!harvestMap()) return;
	 * na.moveDown().moveLeft();
	 * if (!harvestMap()) return;
	 * na.moveLeft().moveUp();
	 * if (!harvestMap()) return;
	 * na.moveLeft();
	 * if (!harvestMap()) return;
	 * na.moveDown();
	 * if (!harvestMap()) return;
	 * na.moveLeft();
	 * if (!harvestMap()) return;
	 * na.moveLeft();
	 * if (!harvestMap()) return;
	 * na.moveLeft(2);
	 * if (!harvestMap()) return;
	 * na.moveUp();
	 * if (!harvestMap()) return;
	 * na.moveRight();
	 * if (!harvestMap()) return;
	 * na.moveRight();
	 * if (!harvestMap()) return;
	 * na.moveRight();
	 * if (!harvestMap()) return;
	 * na.moveUp();
	 * if (!harvestMap()) return;
	 * na.moveLeft();
	 * if (!harvestMap()) return;
	 * na.moveLeft();
	 * if (!harvestMap()) return;
	 * na.moveLeft();
	 * if (!harvestMap()) return;
	 * na.moveUp();
	 * if (!harvestMap()) return;
	 * na.moveRight();
	 * if (!harvestMap()) return;
	 * na.moveRight();
	 * if (!harvestMap()) return;
	 * na.moveRight();
	 * harvestMap();
	 * }
	 */

}
