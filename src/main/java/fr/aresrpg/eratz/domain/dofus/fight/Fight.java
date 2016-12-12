package fr.aresrpg.eratz.domain.dofus.fight;

import fr.aresrpg.dofus.structures.game.FightType;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.structures.map.Mob;
import fr.aresrpg.eratz.domain.dofus.mob.Invocation;
import fr.aresrpg.eratz.domain.player.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @since
 */
public class Fight {

	private final int beginCellId1;
	private final int beginCellId2;
	private FightType type;
	private Cell[] placeTeam0;
	private Cell[] placeTeam1;
	private Set<Player> team0 = new HashSet<>();
	private Set<Player> team1 = new HashSet<>();
	private Set<Mob> mobs = new HashSet<>();
	private Set<Invocation> invocs = new HashSet<>();
	private boolean isBlocked;
	private boolean isSpecBlocked;
	private boolean ended;
	private Player currentTurn;

	public Fight(int cellid1, int cellid2) {
		this.beginCellId1 = cellid1;
		this.beginCellId2 = cellid2;
	}

	public boolean hasPlayer(Player p) {
		return getTeam0().contains(p) || getTeam1().contains(p);
	}

	/**
	 * @return the type
	 */
	public FightType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(FightType type) {
		this.type = type;
	}

	/**
	 * @return the placeTeam0
	 */
	public Cell[] getPlaceTeam0() {
		return placeTeam0;
	}

	/**
	 * @param placeTeam0
	 *            the placeTeam0 to set
	 */
	public void setPlaceTeam0(Cell[] placeTeam0) {
		this.placeTeam0 = placeTeam0;
	}

	/**
	 * @return the placeTeam1
	 */
	public Cell[] getPlaceTeam1() {
		return placeTeam1;
	}

	/**
	 * @param placeTeam1
	 *            the placeTeam1 to set
	 */
	public void setPlaceTeam1(Cell[] placeTeam1) {
		this.placeTeam1 = placeTeam1;
	}

	/**
	 * @return the team0
	 */
	public Set<Player> getTeam0() {
		return team0;
	}

	/**
	 * @param team0
	 *            the team0 to set
	 */
	public void setTeam0(Set<Player> team0) {
		this.team0 = team0;
	}

	/**
	 * @return the team1
	 */
	public Set<Player> getTeam1() {
		return team1;
	}

	/**
	 * @param team1
	 *            the team1 to set
	 */
	public void setTeam1(Set<Player> team1) {
		this.team1 = team1;
	}

	/**
	 * @return the mobs
	 */
	public Set<Mob> getMobs() {
		return mobs;
	}

	/**
	 * @param mobs
	 *            the mobs to set
	 */
	public void setMobs(Set<Mob> mobs) {
		this.mobs = mobs;
	}

	/**
	 * @return the invocs
	 */
	public Set<Invocation> getInvocs() {
		return invocs;
	}

	/**
	 * @param invocs
	 *            the invocs to set
	 */
	public void setInvocs(Set<Invocation> invocs) {
		this.invocs = invocs;
	}

	/**
	 * @return the isBlocked
	 */
	public boolean isBlocked() {
		return isBlocked;
	}

	/**
	 * @param isBlocked
	 *            the isBlocked to set
	 */
	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	/**
	 * @return the isSpecBlocked
	 */
	public boolean isSpecBlocked() {
		return isSpecBlocked;
	}

	/**
	 * @param isSpecBlocked
	 *            the isSpecBlocked to set
	 */
	public void setSpecBlocked(boolean isSpecBlocked) {
		this.isSpecBlocked = isSpecBlocked;
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

	/**
	 * @return the beginCellId1
	 */
	public int getBeginCellId1() {
		return beginCellId1;
	}

	/**
	 * @return the beginCellId2
	 */
	public int getBeginCellId2() {
		return beginCellId2;
	}

}
