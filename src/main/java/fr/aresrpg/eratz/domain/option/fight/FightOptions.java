package fr.aresrpg.eratz.domain.option.fight;

import fr.aresrpg.eratz.domain.player.Perso;

/**
 * 
 * @since
 */
public class FightOptions {

	private Perso perso;
	private boolean waitForGroup;

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

}
