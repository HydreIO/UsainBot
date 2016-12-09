package fr.aresrpg.eratz.domain.behavior.move.type;

import fr.aresrpg.eratz.domain.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ability.move.Navigation;
import fr.aresrpg.eratz.domain.behavior.move.PathBehavior;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.player.Player;

/**
 * 
 * @since
 */
public class IncarnamToAstrubPath extends PathBehavior {

	/**
	 * @param perso
	 */
	public IncarnamToAstrubPath(Perso perso) {
		super(perso);
	}

	@Override
	public PathBehavior getPathToReachCurrentPath() {
		return null;
	}

	@Override
	public void startPath() {
		BaseAbility ability = getPerso().getBaseAbility();
		Navigation na = getPerso().getNavigation();
		ability.closeGui();
		// close si jamais qqn nous défi ou autre
		/* na.moveToCell(459).moveToCell(447) */na.moveToCell(463 , true).moveRight(3).moveToCell(376, true).moveRight(2).moveDown().moveRight();
		ability.speakToNpc(-1).npcTalkChoice(3840, 3373);
	}

	@Override
	public boolean acceptDefi(Player p) {
		return false;
	}

	@Override
	public boolean acceptEchange(Player p) {
		return false;
	}

	@Override
	public boolean acceptGuilde(String pname) {
		return false;
	}

	@Override
	public boolean acceptGroup(String pname) {
		for (Player p : getPerso().getGroup())
			if (p.getPseudo().equalsIgnoreCase(pname)) return true;
		return false;
	}

}
