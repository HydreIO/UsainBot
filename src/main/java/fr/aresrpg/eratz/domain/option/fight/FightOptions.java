package fr.aresrpg.eratz.domain.option.fight;

import fr.aresrpg.eratz.domain.player.Perso;

/**
 * 
 * @since
 */
public class FightOptions {

	private Perso perso;
	private boolean waitForGroup;
	private int currentFightTeam;

	public FightOptions(Perso perso) {
		this.perso = perso;
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	public boolean canStartCombat() {
		if (getPerso().getCurrentFight() == null) throw new IllegalStateException("Le combat est null !");
		return !waitForGroup || getPerso().allGroupIsInFight(getPerso().getCurrentFight());
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
		return "FightOptions [perso=" + perso + ", waitForGroup=" + waitForGroup + ", currentFightTeam=" + currentFightTeam + "]";
	}

}
