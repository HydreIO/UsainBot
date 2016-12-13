/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.behavior.harvest.type;

import fr.aresrpg.eratz.domain.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ability.move.Navigation;
import fr.aresrpg.eratz.domain.behavior.BehaviorStopReason;
import fr.aresrpg.eratz.domain.behavior.harvest.HarvestBehavior;
import fr.aresrpg.eratz.domain.dofus.ressource.Interractable;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.player.Roads;

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
	public BehaviorStopReason start() {
		BaseAbility ab = getPerso().getBaseAbility();
		Navigation na = getPerso().getNavigation();
		BehaviorStopReason reason;
		Roads.nearestRoad(getPerso()).takeRoad(getPerso()); // go to zaap astrub

		na.moveLeft(5);
		reason = harvestMap();
		if (reason != finish)
			if (!harvestMap()) return;
		na.moveUp();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveUp();
		if (!harvestMap()) return;
		na.moveUp();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveDown();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveDown();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveDown();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveDown();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveDown().moveLeft();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveDown();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveDown();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveDown();
		if (!harvestMap()) return;
		na.moveDown();
		if (!harvestMap()) return;
		na.moveDown();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveUp();
		if (!harvestMap()) return;
		na.moveUp();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveDown();
		harvestMap();
	}

}
