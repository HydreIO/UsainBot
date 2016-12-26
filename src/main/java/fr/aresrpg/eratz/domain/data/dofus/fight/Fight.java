package fr.aresrpg.eratz.domain.data.dofus.fight;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.dofus.protocol.game.movement.MovementAction;
import fr.aresrpg.dofus.structures.game.FightEntity;
import fr.aresrpg.dofus.structures.game.FightType;
import fr.aresrpg.dofus.util.Pair;
import fr.aresrpg.eratz.domain.data.AccountsManager;
import fr.aresrpg.eratz.domain.data.player.Perso;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
	private ConcurrentMap<Integer, Pair<MovementAction, FightEntity>> team0 = new ConcurrentHashMap<>();
	private ConcurrentMap<Integer, Pair<MovementAction, FightEntity>> team1 = new ConcurrentHashMap<>();
	private boolean isBlocked;
	private boolean isSpecBlocked;
	private boolean isGroupBlocked;
	private boolean isHelpNeeded;
	private boolean ended;
	private Pair<MovementAction, FightEntity> currentTurn;

	public Fight(int swordCell1, int swordCell2) {
		this.swordCell1 = swordCell1;
		this.swordCell2 = swordCell2;
	}

	public static Fight fromGame(FightType type, boolean isSpec, int timer, boolean duel) {
		Fight f = new Fight(-1, -1);
		f.type = type;
		return f;
	}

	public Pair<MovementAction, FightEntity> findEntity(int id) {
		Pair<MovementAction, FightEntity> a = team0.get(id);
		if (a == null) a = team1.get(id);
		return a;
	}

	public void entityMove(int id, int cellid) {
		findEntity(id).getFirst().setCellId(cellid);
	}

	public void removeEntity(int id) {
		team0.remove(id);
		team1.remove(id);
	}

	public void addEntity(MovementAction ent, int team) {
		if (ent.getId() > 1000) {
			Perso perso = AccountsManager.getInstance().getPerso(ent.getId());
			System.out.println("perso=" + perso + " \n team=" + team);
			if (perso != null) perso.getFightInfos().setCurrentFightTeam(team);
		}
		System.out.println("AJOUT DE L'ENTITY " + ent.getId() + " DANS LA TEAM " + team);
		ConcurrentMap<Integer, Pair<MovementAction, FightEntity>> mt = getTeam(team);
		Pair<MovementAction, FightEntity> entity = new Pair<>(ent, null);
		mt.put(ent.getId(), entity);
	}

	public void addEntity(FightEntity ent) {
		Pair<MovementAction, FightEntity> findEntity = findEntity(ent.getId());
		if (findEntity == null) {
			LOGGER.severe("The entity was not found in the fight ! " + ent);
			return;
		}
		findEntity.setSecond(ent);
	}

	/**
	 * @return the type
	 */
	public FightType getType() {
		return type;
	}

	/**
	 * @return the currentTurn
	 */
	public Pair<MovementAction, FightEntity> getCurrentTurn() {
		return currentTurn;
	}

	/**
	 * @param currentTurn
	 *            the currentTurn to set
	 */
	public void setCurrentTurn(Pair<MovementAction, FightEntity> currentTurn) {
		this.currentTurn = currentTurn;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(FightType type) {
		this.type = type;
	}

	public ConcurrentMap<Integer, Pair<MovementAction, FightEntity>> getTeam(int index) {
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
	public ConcurrentMap<Integer, Pair<MovementAction, FightEntity>> getTeam0() {
		return team0;
	}

	/**
	 * @param team0
	 *            the team0 to set
	 */
	public void setTeam0(ConcurrentMap<Integer, Pair<MovementAction, FightEntity>> team0) {
		this.team0 = team0;
	}

	/**
	 * @return the team1
	 */
	public ConcurrentMap<Integer, Pair<MovementAction, FightEntity>> getTeam1() {
		return team1;
	}

	/**
	 * @param team1
	 *            the team1 to set
	 */
	public void setTeam1(ConcurrentMap<Integer, Pair<MovementAction, FightEntity>> team1) {
		this.team1 = team1;
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
	 * @param currentTurn
	 *            the currentTurn to set
	 */
	public void setCurrentTurn(int entityId) {
		this.currentTurn = findEntity(entityId);
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
				+ ", team0=" + team0 + ", team1=" + team1 + ", isBlocked=" + isBlocked + ", isSpecBlocked=" + isSpecBlocked + ", isGroupBlocked=" + isGroupBlocked + ", isHelpNeeded=" + isHelpNeeded
				+ ", ended=" + ended + ", currentTurn=" + currentTurn + "]";
	}

}
