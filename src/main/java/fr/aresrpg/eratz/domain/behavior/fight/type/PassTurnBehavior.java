package fr.aresrpg.eratz.domain.behavior.fight.type;

import fr.aresrpg.eratz.domain.behavior.fight.FightBehavior;
import fr.aresrpg.eratz.domain.player.Perso;

/**
 * 
 * @since
 */
public class PassTurnBehavior extends FightBehavior {

	/**
	 * @param perso
	 */
	public PassTurnBehavior(Perso perso) {
		super(perso);
	}

	@Override
	public int getBeginCellId() {
		return 0;
	}

	@Override
	public void playTurn() {
		tryHuman();
		getPerso().getFightAbility().endTurn();
	}

}