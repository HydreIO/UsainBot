package fr.aresrpg.eratz.domain.ia.fight.behavior;

import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.util.Pair;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.tofumanchou.domain.data.Spell;
import fr.aresrpg.tofumanchou.domain.data.entity.Entity;
import fr.aresrpg.tofumanchou.domain.data.enums.Spells;
import fr.aresrpg.tofumanchou.infra.data.ManchouCell;
import fr.aresrpg.tofumanchou.infra.data.ManchouSpell;

import java.util.List;
import java.util.Map;

/**
 * 
 * @since
 */
public class CraLowBehavior extends FightBehavior {

	private final ManchouSpell tir_eloigne;
	private final ManchouSpell fleche_magique;
	private final ManchouSpell tir_puissant;
	private final ManchouSpell tir_critique;
	private final ManchouSpell fleche_recul;
	private final ManchouSpell oeuil_taupe;
	private final ManchouSpell fleche_glacee;
	private final ManchouSpell fleche_enflammee;

	/**
	 * @param perso
	 */
	public CraLowBehavior(BotPerso perso) {
		super(perso);
		Map<Spells, Spell> spells = perso.getPerso().getSpells();
		tir_eloigne = (ManchouSpell) spells.get(Spells.TIR_ELOIGNEE);
		fleche_magique = (ManchouSpell) spells.get(Spells.FLECHE_MAGIQUE);
		tir_puissant = (ManchouSpell) spells.get(Spells.TIR_PUISSANT);
		tir_critique = (ManchouSpell) spells.get(Spells.TIR_CRITIQUE);
		fleche_recul = (ManchouSpell) spells.get(Spells.FLECHE_DE_RECUL);
		oeuil_taupe = (ManchouSpell) spells.get(Spells.OEUIL_DE_TAUPE);
		fleche_glacee = (ManchouSpell) spells.get(Spells.FLECHE_GLACEE);
		fleche_enflammee = (ManchouSpell) spells.get(Spells.FLECHE_ENFLAMMEE);
	}

	@Override
	public void turn() {
		boost();
		Entity nearestEnnemy = util().getNearestEnnemy();
		Entity weakestEnnemy = util().getWeakestEnnemy();
		if (isCac(weakestEnnemy)) liberate(weakestEnnemy);
		else if (isCac(nearestEnnemy)) liberate(nearestEnnemy);
		else if (hasCacEntities()) liberate(getCacEntities().get(0));
		else if (tryZone()) ;
		else if (isAccessible(fleche_magique, weakestEnnemy)) agressive(weakestEnnemy);
		else if (isAccessible(fleche_magique, nearestEnnemy)) agressive(nearestEnnemy);
		else {
			ManchouCell cellToTargetWeakest = util().getCellToTargetMob(pm(), weakestEnnemy.getCellId(), util().getMaxPoFor(fleche_magique), false);
			if (cellToTargetWeakest == null) {
				ManchouCell cellToTargetNearest = util().getCellToTargetMob(pm(), nearestEnnemy.getCellId(), util().getMaxPoFor(fleche_magique), false);
				if (cellToTargetNearest == null) util().runToMob(nearestEnnemy, true, pm());
				else agressive(nearestEnnemy, cellToTargetNearest.getId());
			} else agressive(weakestEnnemy, cellToTargetWeakest.getId());
		}
		if (!util().isSafeFromMobs(2)) runAway();
	}

	private boolean tryZone() {
		if (oeuil_taupe.getRelance() != 0 || !hasPaToLaunch(oeuil_taupe)) return false;
		List<Pair<Cell, Integer>> cellsForZoneSpell = util().getCellsForZoneSpell(oeuil_taupe.getMaxPo(), false, true);
		for (Pair<Cell, Integer> pair : cellsForZoneSpell) {
			if (isAccessible(oeuil_taupe, pair.getFirst().getId())) {
				useSpell(oeuil_taupe, pair.getFirst().getId());
				return true;
			}
			ManchouCell cellToTargetMob = util().getCellToTargetMob(pm(), pair.getFirst().getId(), util().getMaxPoFor(oeuil_taupe), false);
			if (cellToTargetMob == null) continue;
			run(cellToTargetMob.getId());
			useSpell(oeuil_taupe, pair.getFirst().getId());
			return true;
		}
		return false;
	}

	private void liberate(Entity e) {
		if (!canLaunch(fleche_recul, e.getCellId())) return;
		useSpell(fleche_recul, e.getCellId());
		completeWithIceArrow(e);
	}

	private void agressive(Entity e) {
		if (!canLaunch(fleche_magique, e.getCellId())) return;
		useSpell(fleche_magique, e.getCellId());
		completeWithIceArrow(e);
	}

	private void agressive(Entity e, int cell) {
		if (!hasPaToLaunch(fleche_magique)) return;
		run(cell);
		if (!canLaunch(fleche_magique, cell)) return;
		useSpell(fleche_magique, e.getCellId());
		completeWithIceArrow(e);
	}

	private void completeWithIceArrow(Entity e) {
		ManchouCell cellToTargetMob = util().getCellToTargetMob(pm(), e.getCellId(), util().getMaxPoFor(fleche_glacee), false);
		if (cellToTargetMob == null) return;
		run(cellToTargetMob.getId());
		if (!canLaunch(fleche_glacee, e.getCellId())) return;

	}

	private void runAway() {
		if (!tryToHide()) tryToRunAway();
	}

	private void boost() {
		if (canLaunch(tir_eloigne, playerCell())) useSpell(tir_eloigne, playerCell());
		if (canLaunch(tir_puissant, playerCell())) useSpell(tir_puissant, playerCell());
		if (canLaunch(tir_critique, playerCell())) useSpell(tir_critique, playerCell());
	}

	@Override
	public void decrementRelance() {
		tir_eloigne.decrementRelance();
		tir_puissant.decrementRelance();
		tir_critique.decrementRelance();
		oeuil_taupe.decrementRelance();
	}

}
