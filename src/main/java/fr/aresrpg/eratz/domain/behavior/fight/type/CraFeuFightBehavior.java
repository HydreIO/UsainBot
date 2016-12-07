package fr.aresrpg.eratz.domain.behavior.fight.type;

import fr.aresrpg.eratz.domain.ability.fight.FightAbility;
import fr.aresrpg.eratz.domain.behavior.fight.FightBehavior;
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
		fa.endTurn();
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
