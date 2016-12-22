/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.ia.behavior.harvest.type;

import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.dofus.structures.Skills;
import fr.aresrpg.eratz.domain.data.dofus.ressource.Interractable;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.behavior.harvest.HarvestBehavior;

import java.util.Set;

/**
 * 
 * @since
 */
public class SeigleHarvestBehavior extends HarvestBehavior {

	private Set<Interractable> res = ArrayUtils.asSet(Interractable.AVOINE);

	/**
	 * @param perso
	 */
	public SeigleHarvestBehavior(Perso perso, int quantity) {
		super(perso, quantity);
	}

	@Override
	public Interractable getTypesToHarvest() {
		return Interractable.SEIGLE;
	}

	@Override
	public Skills getSkill() {
		return Skills.FAUCHER_SEIGLE;
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
	 * na.moveLeft(6).moveDown();
	 * if (!harvestMap()) return;
	 * na.moveDown(4);
	 * if (!harvestMap()) return;
	 * na.moveRight();
	 * if (!harvestMap()) return;
	 * na.moveRight();
	 * if (!harvestMap()) return;
	 * na.moveLeft();
	 * if (!harvestMap()) return;
	 * na.moveDown();
	 * if (!harvestMap()) return;
	 * na.moveDown();
	 * if (!harvestMap()) return;
	 * na.moveLeft(5);
	 * if (!harvestMap()) return;
	 * na.moveLeft();
	 * if (!harvestMap()) return;
	 * na.moveLeft();
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
	 * na.moveLeft();
	 * if (!harvestMap()) return;
	 * na.moveUp();
	 * if (!harvestMap()) return;
	 * na.moveRight();
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
