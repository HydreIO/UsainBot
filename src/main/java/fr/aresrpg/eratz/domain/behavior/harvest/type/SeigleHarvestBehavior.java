/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.behavior.harvest.type;

import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.eratz.domain.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ability.move.Navigation;
import fr.aresrpg.eratz.domain.behavior.harvest.HarvestBehavior;
import fr.aresrpg.eratz.domain.dofus.map.Zaap;
import fr.aresrpg.eratz.domain.dofus.ressource.Ressources;
import fr.aresrpg.eratz.domain.player.Perso;

import java.util.Set;

/**
 * 
 * @since
 */
public class SeigleHarvestBehavior extends HarvestBehavior {

	private Set<Ressources> res = ArrayUtils.asSet(Ressources.AVOINE);

	/**
	 * @param perso
	 */
	public SeigleHarvestBehavior(Perso perso, int quantity) {
		super(perso, quantity);
	}

	@Override
	public Ressources getTypesToHarvest() {
		return Ressources.SEIGLE;
	}

	@Override
	public void start() {
		BaseAbility ab = getPerso().getBaseAbility();
		Navigation na = getPerso().getNavigation();
		ab.goToZaap(Zaap.PLAINE_ROCHEUSE);
		na.moveLeft(6).moveDown();
		if (!harvestMap()) return;
		na.moveDown(4);
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveDown();
		if (!harvestMap()) return;
		na.moveDown();
		if (!harvestMap()) return;
		na.moveLeft(5);
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveUp();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveUp();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveLeft();
		if (!harvestMap()) return;
		na.moveUp();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveRight();
		if (!harvestMap()) return;
		na.moveRight();
		harvestMap();
	}

}
