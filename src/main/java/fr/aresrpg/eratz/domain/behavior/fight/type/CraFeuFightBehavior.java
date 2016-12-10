package fr.aresrpg.eratz.domain.behavior.fight.type;

import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.structures.map.Mob;
import fr.aresrpg.eratz.domain.ability.fight.FightAbility;
import fr.aresrpg.eratz.domain.behavior.fight.FightBehavior;
import fr.aresrpg.eratz.domain.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.dofus.player.Spell;
import fr.aresrpg.eratz.domain.dofus.player.Spells;
import fr.aresrpg.eratz.domain.player.Perso;

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
		FightAbility fa = getPerso().getFightAbility();
		tryHuman(); // si enabled le bot va attendre un peu avant de commencer son tour
		if (is90()) playTurn90();
		else playTurnBase();
		fa.endTurn();
	}

	private void playTurnBase() {
		FightAbility fa = getPerso().getFightAbility();

	}

	private void playTurn90() {
		FightAbility fa = getPerso().getFightAbility();
		Spells boost = getPerso().getLvl() > 99 ? Spells.MAITRISE_ARC : Spells.TIR_ELOIGNEE;
		fa.launchSpell(boost.get(getPerso()), getPerso().getCellid());
		fa.launchSpell(Spells.TIR_PUISSANT.get(getPerso()), getPerso().getCellid());

	}

	private int getRandomCellAtPoForMob(Mob m, Spell s) {
		Cell[] cells = getPerso().getCurrentMap().getDofusMap().getCells();
	}

	private Mob getNearestMob() {
		Fight fi = getPerso().getCurrentFight();
		for (Mob m : fi.getMobs()) {}
	}

	private int getCost(Cell cell) {
	}

	private boolean is90() {
		return getPerso().getLvl() >= 90;
	}

	@Override
	public int getBeginCellId() {
		// TODO ajouter des selector de placement en fonction du type de mob etc
		return 0;
	}

}
