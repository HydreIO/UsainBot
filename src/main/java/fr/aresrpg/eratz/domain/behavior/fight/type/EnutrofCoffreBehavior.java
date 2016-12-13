package fr.aresrpg.eratz.domain.behavior.fight.type;

import fr.aresrpg.eratz.domain.ability.fight.FightAbility;
import fr.aresrpg.eratz.domain.behavior.fight.FightBehavior;
import fr.aresrpg.eratz.domain.dofus.player.Spells;
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
		FightAbility fa = getPerso().getAbilities().getFightAbility();
		if (getPerso().getStatsInfos().getLvl() > 99) {
			fa.launchSpell(Spells.COFFRE_ANIME.get(getPerso()), fa.getSafeCellAwayFromMobs());
			tryHuman();
		}
		fa.endTurn();
	}

}
