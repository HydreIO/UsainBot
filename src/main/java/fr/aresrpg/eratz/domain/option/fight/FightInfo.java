package fr.aresrpg.eratz.domain.option.fight;

import fr.aresrpg.eratz.domain.behavior.fight.FightBehavior;
import fr.aresrpg.eratz.domain.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.player.Info;
import fr.aresrpg.eratz.domain.player.Perso;

/**
 * 
 * @since
 */
public class FightInfo extends Info {

	private Fight currentFight;
	private FightBehavior currentFightBehavior;
	private boolean waitForGroup;
	private int currentFightTeam;

	public FightInfo(Perso perso) {
		super(perso);
	}

	/**
	 * @return the currentFight
	 */
	public Fight getCurrentFight() {
		return currentFight;
	}

	/**
	 * @return the currentFightBehavior
	 */
	public FightBehavior getCurrentFightBehavior() {
		return currentFightBehavior;
	}

	/**
	 * @param currentFight
	 *            the currentFight to set
	 */
	public void setCurrentFight(Fight currentFight) {
		this.currentFight = currentFight;
	}

	/**
	 * @param currentFightBehavior
	 *            the currentFightBehavior to set
	 */
	public void setCurrentFightBehavior(FightBehavior currentFightBehavior) {
		this.currentFightBehavior = currentFightBehavior;
	}

	public boolean canStartCombat() {
		if (getCurrentFight() == null) throw new IllegalStateException("Le combat est null !");
		return !waitForGroup || getPerso().allGroupIsInFight(getCurrentFight());
	}

	/**
	 * @return the currentFightTeam
	 */
	public int getCurrentFightTeam() {
		return currentFightTeam;
	}

	/**
	 * @param currentFightTeam
	 *            the currentFightTeam to set
	 */
	public void setCurrentFightTeam(int currentFightTeam) {
		this.currentFightTeam = currentFightTeam;
	}

	/**
	 * @param waitForGroup
	 *            does the bot need to wait his groupe to start a fight
	 */
	public void setWaitForGroup(boolean waitForGroup) {
		this.waitForGroup = waitForGroup;
	}

	/**
	 * @return true if the player need to wait all his group to start a combat
	 */
	public boolean isWaitingForGroup() {
		return waitForGroup;
	}

	@Override
	public String toString() {
		return "FightInfo [currentFight=" + currentFight + ", currentFightBehavior=" + currentFightBehavior + ", waitForGroup=" + waitForGroup + ", currentFightTeam=" + currentFightTeam + ", "
				+ super.toString() + "]";
	}

}
