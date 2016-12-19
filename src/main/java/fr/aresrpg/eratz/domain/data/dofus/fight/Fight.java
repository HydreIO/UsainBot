package fr.aresrpg.eratz.domain.data.dofus.fight;

import fr.aresrpg.dofus.protocol.game.movement.*;
import fr.aresrpg.dofus.structures.game.FightType;

import java.util.*;

/**
 * 
 * @since
 */
public class Fight {

	private final int swordCell1;
	private final int swordCell2;
	private FightType type;
	private int[] placeTeam0;
	private int[] placeTeam1;
	private Set<MovementPlayer> team0 = new HashSet<>();
	private Set<MovementPlayer> team1 = new HashSet<>();
	private Set<MovementMonster> mobs = new HashSet<>();
	private Set<MovementInvocation> invocs = new HashSet<>();
	private boolean isBlocked;
	private boolean isSpecBlocked;
	private boolean isGroupBlocked;
	private boolean isHelpNeeded;
	private boolean ended;
	private MovementAction currentTurn;

	public Fight(int swordCell1, int swordCell2) {
		this.swordCell1 = swordCell1;
		this.swordCell2 = swordCell2;
	}

	public void moveEntity(int entityId, int cellId) {
		movePlayer(entityId, cellId);
		moveMonster(entityId, cellId);
		moveInvoc(entityId, cellId);
	}

	public void movePlayer(int id, int cellid) {
		for (MovementPlayer m : team0)
			if (m.getId() == id) m.setCell(cellid);
		for (MovementPlayer m : team1)
			if (m.getId() == id) m.setCell(cellid);
	}

	public void moveMonster(int id, int cellid) {
		for (MovementMonster m : mobs)
			if (m.getId() == id) m.setCellId(cellid);
	}

	public void moveInvoc(int id, int cellid) {
		for (MovementInvocation m : invocs)
			if (m.getId() == id) m.setCellId(cellid);
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

	public Set<MovementPlayer> getTeam(int index) {
		if (index == 0) return getTeam0();
		else if (index == 1) return getTeam1();
		throw new IllegalArgumentException("Il n'y a que 2 teams !");
	}

	/**
	 * @return the placeTeam0
	 */
	public int[] getPlaceTeam0() {
		return placeTeam0;
	}

	/**
	 * @param placeTeam0
	 *            the placeTeam0 to set
	 */
	public void setPlaceTeam0(int[] placeTeam0) {
		this.placeTeam0 = placeTeam0;
	}

	/**
	 * @return the placeTeam1
	 */
	public int[] getPlaceTeam1() {
		return placeTeam1;
	}

	/**
	 * @param placeTeam1
	 *            the placeTeam1 to set
	 */
	public void setPlaceTeam1(int[] placeTeam1) {
		this.placeTeam1 = placeTeam1;
	}

	/**
	 * @return the team0
	 */
	public Set<MovementPlayer> getTeam0() {
		return team0;
	}

	/**
	 * @param team0
	 *            the team0 to set
	 */
	public void setTeam0(Set<MovementPlayer> team0) {
		this.team0 = team0;
	}

	/**
	 * @return the team1
	 */
	public Set<MovementPlayer> getTeam1() {
		return team1;
	}

	/**
	 * @param team1
	 *            the team1 to set
	 */
	public void setTeam1(Set<MovementPlayer> team1) {
		this.team1 = team1;
	}

	/**
	 * @return the mobs
	 */
	public Set<MovementMonster> getMobs() {
		return mobs;
	}

	/**
	 * @param mobs
	 *            the mobs to set
	 */
	public void setMobs(Set<MovementMonster> mobs) {
		this.mobs = mobs;
	}

	/**
	 * @return the invocs
	 */
	public Set<MovementInvocation> getInvocs() {
		return invocs;
	}

	/**
	 * @param invocs
	 *            the invocs to set
	 */
	public void setInvocs(Set<MovementInvocation> invocs) {
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
	 * @return the isGroupBlocked
	 */
	public boolean isGroupBlocked() {
		return isGroupBlocked;
	}

	/**
	 * @param isGroupBlocked
	 *            the isGroupBlocked to set
	 */
	public void setGroupBlocked(boolean isGroupBlocked) {
		this.isGroupBlocked = isGroupBlocked;
	}

	/**
	 * @return the isHelpNeeded
	 */
	public boolean isHelpNeeded() {
		return isHelpNeeded;
	}

	/**
	 * @param isHelpNeeded
	 *            the isHelpNeeded to set
	 */
	public void setHelpNeeded(boolean isHelpNeeded) {
		this.isHelpNeeded = isHelpNeeded;
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
	public MovementAction getCurrentTurn() {
		return currentTurn;
	}

	/**
	 * @param currentTurn
	 *            the currentTurn to set
	 */
	public void setCurrentTurn(MovementAction currentTurn) {
		this.currentTurn = currentTurn;
	}

	/**
	 * @return the swordCell1
	 */
	public int getSwordCell1() {
		return swordCell1;
	}

	/**
	 * @return the swordCell2
	 */
	public int getSwordCell2() {
		return swordCell2;
	}

	@Override
	public String toString() {
		return "Fight [swordCell1=" + swordCell1 + ", swordCell2=" + swordCell2 + ", type=" + type + ", placeTeam0=" + Arrays.toString(placeTeam0) + ", placeTeam1=" + Arrays.toString(placeTeam1)
				+ ", team0=" + team0 + ", team1=" + team1 + ", mobs=" + mobs + ", invocs=" + invocs + ", isBlocked=" + isBlocked + ", isSpecBlocked=" + isSpecBlocked + ", isGroupBlocked="
				+ isGroupBlocked + ", isHelpNeeded=" + isHelpNeeded + ", ended=" + ended + ", currentTurn=" + currentTurn + "]";
	}

}
