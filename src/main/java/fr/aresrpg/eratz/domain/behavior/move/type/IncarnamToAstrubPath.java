package fr.aresrpg.eratz.domain.behavior.move.type;

import fr.aresrpg.eratz.domain.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ability.move.Navigation;
import fr.aresrpg.eratz.domain.behavior.Behavior;
import fr.aresrpg.eratz.domain.behavior.BehaviorStopReason;
import fr.aresrpg.eratz.domain.player.Perso;

/**
 * 
 * @since
 */
public class IncarnamToAstrubPath extends Behavior {

	/**
	 * @param perso
	 */
	public IncarnamToAstrubPath(Perso perso) {
		super(perso);
	}

	@Override
	public BehaviorStopReason start() {
		BaseAbility ability = getPerso().getAbilities().getBaseAbility();
		Navigation na = getPerso().getNavigation();
		ability.closeGui(); // close si jamais qqn nous défi ou autre
		na.moveToCell(459, true).moveToCell(447, true).moveToCell(463, true).moveRight(3).moveToCell(376, true).moveRight(2).moveDown().moveRight();
		ability.speakToNpc(-1);
		ability.npcTalkChoice(3840, 3373);
		return BehaviorStopReason.FINISHED;
	}

}
