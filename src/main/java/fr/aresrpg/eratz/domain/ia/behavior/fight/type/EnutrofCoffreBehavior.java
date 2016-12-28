package fr.aresrpg.eratz.domain.ia.behavior.fight.type;

import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.ability.fight.FightAbility;
import fr.aresrpg.eratz.domain.ia.behavior.fight.FightBehavior;

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
		FightAbility fa = getPerso().getAbilities().getFightAbility();
		if (getPerso().getStatsInfos().getLvl() > 99) {
			//fa.launchSpell(Spells.COFFRE_ANIME, -1);
			tryHuman();
		}
		fa.endTurn();
	}

}
