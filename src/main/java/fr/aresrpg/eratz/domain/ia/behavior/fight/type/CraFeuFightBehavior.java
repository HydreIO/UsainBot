/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.ia.behavior.fight.type;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.dofus.protocol.game.movement.MovementAction;
import fr.aresrpg.dofus.structures.game.FightEntity;
import fr.aresrpg.dofus.util.Pair;
import fr.aresrpg.eratz.domain.TheBotFather;
import fr.aresrpg.eratz.domain.data.dofus.player.Spells;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.object.Spell;
import fr.aresrpg.eratz.domain.ia.ability.fight.FightAbility;
import fr.aresrpg.eratz.domain.ia.behavior.fight.FightBehavior;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class CraFeuFightBehavior extends FightBehavior {

	/**
	 * @param perso
	 */
	public CraFeuFightBehavior(Perso perso) {
		super(perso);
	}

	@Override
	public void playTurn() {
		FightAbility fa = getPerso().getAbilities().getFightAbility();
		tryHuman(); // si enabled le bot va attendre un peu avant de commencer son tour
		if (is90()) playTurn90();
		else playTurnBase();
		fa.endTurn();
	}

	private void playTurnBase() {
		FightAbility fa = getPerso().getAbilities().getFightAbility();
		Spell tirel = getPerso().getStatsInfos().getSpells().get(Spells.TIR_ELOIGNEE);
		Spell tirep = getPerso().getStatsInfos().getSpells().get(Spells.TIR_PUISSANT);
		if (tirel != null) tirel.decrementRelance();
		if (tirep != null) tirep.decrementRelance();
		fa.launchSpell(tirel, 5, getPerso().getFightInfos().getCurrentFight().getPositionOf(getPerso().getId()));
		if (getPerso().getStatsInfos().getPA() >= 8) fa.launchSpell(tirep, 5, getPerso().getMapInfos().getCellId()); // TODO verif les relance
		Pair<MovementAction, FightEntity> nearestEnnemy = getNearestEnnemy();
		if (nearestEnnemy == null) {
			TheBotFather.LOGGER.severe("Aucun ennemi trouvé..");
			return;
		}
		Spell spell = getPerso().getStatsInfos().getSpells().get(Spells.FLECHE_MAGIQUE);
		if (spell == null) throw new NullPointerException("Spell not found");
		MovementAction ac = nearestEnnemy.getFirst();
		int missingMaxPoFor = getMissingMaxPoFor(spell, ac.getCellId());
		TheBotFather.LOGGER.error("missingMaxPoFor = " + missingMaxPoFor);
		if (missingMaxPoFor > 0) runToMob(nearestEnnemy, true, missingMaxPoFor);
		else if (!isSafeFromMobs()) runAwayFromMobs();
		TheBotFather.LOGGER.error("isSafeFromMobs ? " + isSafeFromMobs());
		if (hasMaxPoFor(spell, ac.getCellId())) fa.launchSpell(spell, 0, ac.getCellId());
		Threads.uSleep(1, TimeUnit.SECONDS);
	}

	private void playTurn90() {
		FightAbility fa = getPerso().getAbilities().getFightAbility();

	}

	private boolean is90() {
		return getPerso().getStatsInfos().getLvl() >= 90;
	}

	@Override
	public int getBeginCellId() {
		// TODO ajouter des selector de placement en fonction du type de mob etc
		return -1;
	}

}
