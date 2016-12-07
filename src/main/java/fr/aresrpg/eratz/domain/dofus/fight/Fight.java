package fr.aresrpg.eratz.domain.dofus.fight;

import fr.aresrpg.dofus.structures.map.Mob;
import fr.aresrpg.eratz.domain.dofus.mob.Invocation;
import fr.aresrpg.eratz.domain.player.Player;

import java.util.Set;

/**
 * 
 * @since
 */
public class Fight {

	private Set<Player> team0;
	private Set<Player> team1;
	private Set<Mob> mobs;
	private Set<Invocation> invocs;
	private boolean isBlocked;
	private boolean isSpecBlocked;
	private boolean ended;
	private Player currentTurn;

	/**
	 * @return the currentTurn
	 */
	public Player getCurrentTurn() {
		return currentTurn;
	}

	/**
	 * @param currentTurn
	 *            the currentTurn to set
	 */
	public void setCurrentTurn(Player currentTurn) {
		this.currentTurn = currentTurn;
	}

	public boolean hasPlayer(Player p) {
		return getTeam0().contains(p) || getTeam1().contains(p);
	}

	/**
	 * @return the ended
	 */
	public boolean isEnded() {
		return ended;
	}

	/**
	 * @param ended
	 *            the ended to set
	 */
	public void setEnded(boolean ended) {
		this.ended = ended;
	}

	/**
	 * @param isBlocked
	 *            the isBlocked to set
	 */
	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	/**
	 * @param isSpecBlocked
	 *            the isSpecBlocked to set
	 */
	public void setSpecBlocked(boolean isSpecBlocked) {
		this.isSpecBlocked = isSpecBlocked;
	}

	/**
	 * @param invocs
	 *            the invocs to set
	 */
	public void setInvocs(Set<Invocation> invocs) {
		this.invocs = invocs;
	}

	/**
	 * @param mobs
	 *            the mobs to set
	 */
	public void setMobs(Set<Mob> mobs) {
		this.mobs = mobs;
	}

	/**
	 * @param team0
	 *            the team0 to set
	 */
	public void setTeam0(Set<Player> team0) {
		this.team0 = team0;
	}

	/**
	 * @param team1
	 *            the team1 to set
	 */
	public void setTeam1(Set<Player> team1) {
		this.team1 = team1;
	}

	/**
	 * @return the isBlocked
	 */
	public boolean isBlocked() {
		return isBlocked;
	}

	/**
	 * @return the isSpecBlocked
	 */
	public boolean isSpecBlocked() {
		return isSpecBlocked;
	}

	/**
	 * @return the team0
	 */
	public Set<Player> getTeam0() {
		return team0;
	}

	/**
	 * @return the team1
	 */
	public Set<Player> getTeam1() {
		return team1;
	}

	/**
	 * @return the invocs
	 */
	public Set<Invocation> getInvocs() {
		return invocs;
	}

	/**
	 * @return the mobs
	 */
	public Set<Mob> getMobs() {
		return mobs;
	}

}
