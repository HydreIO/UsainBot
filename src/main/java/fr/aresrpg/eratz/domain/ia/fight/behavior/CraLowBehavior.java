package fr.aresrpg.eratz.domain.ia.fight.behavior;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.log.AnsiColors.AnsiColor;
import fr.aresrpg.dofus.util.Pair;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.tofumanchou.domain.data.Spell;
import fr.aresrpg.tofumanchou.domain.data.entity.Entity;
import fr.aresrpg.tofumanchou.domain.data.enums.Spells;
import fr.aresrpg.tofumanchou.infra.data.ManchouCell;
import fr.aresrpg.tofumanchou.infra.data.ManchouSpell;

import java.util.*;

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

	private boolean looped;

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
		Entity weakestEnnemy = util().getWeakestEnnemy();
		List<Entity> ennemiesAccess = null;
		LOGGER.debug(AnsiColor.BLUE + "weakest = " + weakestEnnemy);
		if (isCac(weakestEnnemy)) {
			LOGGER.debug("WEAKEST CAC");
			liberate(weakestEnnemy);
		} else if (hasCacEntities()) {
			LOGGER.debug("ENTITIES CAC " + getCacEntities());
			liberate(getCacEntities().get(0));
		} else if (tryZone()) {
			;
		} else if (isAccessible(fleche_magique, weakestEnnemy)) {
			LOGGER.debug("weak is accessible pr fleche magique");
			agressive(weakestEnnemy);
		} else if (!(ennemiesAccess = ennemiesAccessibles(fleche_magique)).isEmpty()) {
			LOGGER.debug(AnsiColor.BLUE + "ennemies = " + ennemiesAccess);
			LOGGER.debug("near is accessible pr fleche magique");
			agressive(ennemiesAccess.iterator().next());
		} else {
			LOGGER.debug("else !");
			ManchouCell cellToTargetWeakest = util().getCellToTargetMob(pm(), weakestEnnemy.getCellId(), util().getMaxPoFor(fleche_magique), false);
			if (cellToTargetWeakest == null) {
				LOGGER.debug("cellToTarget WEAK est null");
				Set<Entity> ennemies = ennemies();
				for (Entity ene : ennemies) {
					ManchouCell cellToTargetNearest = util().getCellToTargetMob(pm(), ene.getCellId(), util().getMaxPoFor(fleche_magique), false);
					if (cellToTargetNearest == null) continue;
					else agressive(ene, cellToTargetNearest.getId());
					break;
				}
				util().runToMob(weakestEnnemy, true, pm());
			} else agressive(weakestEnnemy, cellToTargetWeakest.getId());
		}
		if (pa() >= 3 && !looped) {
			looped = true;
			turn();
			return;
		}
		looped = false;
		if (!util().isSafeFromMobs(2)) runAway();
		LOGGER.debug("turn ended !");
	}

	private boolean tryZone() {
		LOGGER.debug("try zone !");
		if (oeuil_taupe.getRelance() != 0 || !hasPaToLaunch(oeuil_taupe)) return false;
		LOGGER.debug("test oeuil de taupe");
		List<Pair<ManchouCell, Integer>> cellsForZoneSpell = util().getCellsForZoneSpell(oeuil_taupe.getRange(), false, true);
		for (Pair<ManchouCell, Integer> pair : cellsForZoneSpell) {
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
		if (!canLaunch(fleche_glacee, e.getCellId())) return;
		ManchouCell cellToTargetMob = util().getCellToTargetMob(pm(), e.getCellId(), util().getMaxPoFor(fleche_glacee), false);
		if (cellToTargetMob == null) return;
		run(cellToTargetMob.getId());
		useSpell(fleche_glacee, e.getCellId());
	}

	private void runAway() {
		LOGGER.debug("run away");
		if (!tryToHide()) {
			LOGGER.debug("Cant hide, run away");
			tryToRunAway();
		}
	}

	private void boost() {
		if (canLaunch(tir_eloigne, playerCell())) {
			LOGGER.debug("CAN LAUNCH tir éloigné");
			useSpell(tir_eloigne, playerCell());
		}
		if (canLaunch(tir_puissant, playerCell())) {
			LOGGER.debug("CAN LAUNCH tir puissant");
			useSpell(tir_puissant, playerCell());
		}
		if (canLaunch(tir_critique, playerCell())) {
			LOGGER.debug("CAN LAUNCH tir critique");
			useSpell(tir_critique, playerCell());
		}
	}

	@Override
	public void decrementRelance() {
		if (tir_eloigne != null) tir_eloigne.decrementRelance();
		if (tir_puissant != null) tir_puissant.decrementRelance();
		if (tir_critique != null) tir_critique.decrementRelance();
		if (oeuil_taupe != null) oeuil_taupe.decrementRelance();
	}

}
