/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.ia.behavior.fight.type;

import fr.aresrpg.eratz.domain.data.dofus.player.Spells;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.ability.fight.FightAbility;
import fr.aresrpg.eratz.domain.ia.behavior.fight.FightBehavior;

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

	}

	private void playTurn90() {
		FightAbility fa = getPerso().getAbilities().getFightAbility();
		Spells boost = getPerso().getStatsInfos().getLvl() > 99 ? Spells.MAITRISE_ARC : Spells.TIR_ELOIGNEE;
		fa.launchSpell(boost.get(getPerso()), getPerso().getMapInfos().getCellId());
		fa.launchSpell(Spells.TIR_PUISSANT.get(getPerso()), getPerso().getMapInfos().getCellId());

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
