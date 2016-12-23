package fr.aresrpg.eratz.domain.ia.behavior.fight.type;

import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.behavior.fight.FightBehavior;

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
		return -1;
	}

	@Override
	public void playTurn() {
		tryHuman();
		getPerso().getAbilities().getFightAbility().endTurn();
	}

}
