package fr.aresrpg.eratz.domain.data.dofus.fight;

import fr.aresrpg.dofus.protocol.game.movement.*;
import fr.aresrpg.dofus.structures.game.FightType;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

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
	private CopyOnWriteArraySet<MovementPlayer> team0 = new CopyOnWriteArraySet<>();
	private CopyOnWriteArraySet<MovementPlayer> team1 = new CopyOnWriteArraySet<>();
	private CopyOnWriteArraySet<MovementMonster> mobs = new CopyOnWriteArraySet<>();
	private CopyOnWriteArraySet<MovementInvocation> invocs = new CopyOnWriteArraySet<>();
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

	public static Fight fromGame(FightType type, boolean isSpec, int timer, boolean duel) {
		Fight f = new Fight(-1, -1);
		f.type = type;
		return f;
	}

	public void entityMove(int id, int cellid) {
		if (id > 1000) playerMove(id, cellid);
		else {
			monsterMove(id, cellid);
			invocMove(id, cellid);
		}
	}

	public void playerMove(int id, int cellid) {
		for (MovementPlayer i : team0)
			if (i.getId() == id) {
				i.setCellid(cellid);
				return;
			}
		for (MovementPlayer i : team1)
			if (i.getId() == id) {
				i.setCellid(cellid);
				return;
			}
	}

	public void monsterMove(int id, int cellid) {
		for (MovementMonster i : mobs)
			if (i.getId() == id) i.setCellId(cellid);
	}

	public void invocMove(int id, int cellid) {
		for (MovementInvocation i : invocs)
			if (i.getId() == id) i.setCellId(cellid);
	}

	public void entityUpdate(MovementAction action) {
		if (action instanceof MovementPlayer) updatePlayer((MovementPlayer) action);
		else if (action instanceof MovementMonster) updateMonster((MovementMonster) action);
		else if (action instanceof MovementInvocation) updateInvoc((MovementInvocation) action);
		else throw new IllegalArgumentException(action + " has not a valid type");
	}

	public void removeActor(int id) {
		if (id > 1000) removePlayer(id);
		else {
			removeMob(id);
			removeInvoc(id);
		}
	}

	public void removePlayer(int id) {
		team0.removeIf(p -> p.getId() == id);
		team1.removeIf(p -> p.getId() == id);
	}

	public void removeMob(int id) {
		mobs.removeIf(p -> p.getId() == id);
	}

	public void removeInvoc(int id) {
		invocs.removeIf(p -> p.getId() == id);
	}

	private void updatePlayer(MovementPlayer player) {
		if (team0.contains(player)) {
			team0.remove(player);
			team0.add(player);
		} else if (team1.contains(player)) {
			team1.remove(player);
			team1.add(player);
		}
	}

	public void updateMonster(MovementMonster mob) {
		mobs.remove(mob);
		mobs.add(mob);
	}

	public void updateInvoc(MovementInvocation npc) {
		invocs.remove(npc);
		invocs.add(npc);
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
	public CopyOnWriteArraySet<MovementPlayer> getTeam0() {
		return team0;
	}

	/**
	 * @param team0
	 *            the team0 to set
	 */
	public void setTeam0(CopyOnWriteArraySet<MovementPlayer> team0) {
		this.team0 = team0;
	}

	/**
	 * @return the team1
	 */
	public CopyOnWriteArraySet<MovementPlayer> getTeam1() {
		return team1;
	}

	/**
	 * @param team1
	 *            the team1 to set
	 */
	public void setTeam1(CopyOnWriteArraySet<MovementPlayer> team1) {
		this.team1 = team1;
	}

	/**
	 * @return the mobs
	 */
	public CopyOnWriteArraySet<MovementMonster> getMobs() {
		return mobs;
	}

	/**
	 * @param mobs
	 *            the mobs to set
	 */
	public void setMobs(CopyOnWriteArraySet<MovementMonster> mobs) {
		this.mobs = mobs;
	}

	/**
	 * @return the invocs
	 */
	public CopyOnWriteArraySet<MovementInvocation> getInvocs() {
		return invocs;
	}

	/**
	 * @param invocs
	 *            the invocs to set
	 */
	public void setInvocs(CopyOnWriteArraySet<MovementInvocation> invocs) {
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
