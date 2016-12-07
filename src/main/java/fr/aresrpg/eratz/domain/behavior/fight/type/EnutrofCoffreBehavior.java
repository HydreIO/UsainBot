package fr.aresrpg.eratz.domain.behavior.fight.type;

import fr.aresrpg.eratz.domain.behavior.fight.FightBehavior;
import fr.aresrpg.eratz.domain.dofus.player.Spell;
import fr.aresrpg.eratz.domain.player.Perso;

/**
 * 
 * @since
 */
public class EnutrofCoffreBehavior extends FightBehavior {

	/**
	 * @param perso
	 */
	public EnutrofCoffreBehavior(Perso perso) {
		super(perso);
	}

	@Override
	public int getBeginCellId() {
		return 0;
	}

	@Override
	public void playTurn() {
		tryHuman();
		getPerso().getFightAbility().launchSpell(new Spell, getBeginCellId());
		getPerso().getFightAbility().endTurn();
	}

}
