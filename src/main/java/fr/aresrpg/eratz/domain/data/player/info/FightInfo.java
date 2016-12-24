package fr.aresrpg.eratz.domain.data.player.info;

import fr.aresrpg.dofus.structures.game.FightSpawn;
import fr.aresrpg.eratz.domain.data.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.behavior.fight.FightBehavior;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @since
 */
public class FightInfo extends Info {

	private Fight currentFight;
	private List<FightSpawn> fightsOnMap = new ArrayList<>();
	private FightBehavior currentFightBehavior;
	private boolean waitForGroup;
	private int currentFightTeam;

	public FightInfo(Perso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {
		currentFight = null;
		currentFightBehavior = null;
	}

	/**
	 * @return the fightsOnMap
	 */
	public List<FightSpawn> getFightsOnMap() {
		return fightsOnMap;
	}

	/**
	 * @param fightsOnMap
	 *            the fightsOnMap to set
	 */
	public void setFightsOnMap(List<FightSpawn> fightsOnMap) {
		this.fightsOnMap = fightsOnMap;
	}

	/**
	 * @return the waitForGroup
	 */
	public boolean isWaitForGroup() {
		return waitForGroup;
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

	public void notifyFightStart() {
		if (getCurrentFightBehavior() == null) throw new NullPointerException("Le behavior est null");
		Executors.FIXED.execute(getCurrentFightBehavior()::start);
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
