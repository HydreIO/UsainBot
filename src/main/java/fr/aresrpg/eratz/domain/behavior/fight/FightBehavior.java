package fr.aresrpg.eratz.domain.behavior.fight;

import fr.aresrpg.dofus.structures.map.*;
import fr.aresrpg.dofus.util.Maps;
import fr.aresrpg.eratz.domain.behavior.Behavior;
import fr.aresrpg.eratz.domain.behavior.BehaviorStopReason;
import fr.aresrpg.eratz.domain.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.util.config.Variables;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public abstract class FightBehavior extends Behavior {

	private static Random r = new Random();
	private Fight fight;
	private Humeur humeur;

	/**
	 * @param perso
	 */
	public FightBehavior(Perso perso) {
		super(perso);
	}

	public boolean humanFight() {
		return Variables.HUMAN_FIGHT;
	}

	public abstract int getBeginCellId();

	/**
	 * @param humeur
	 *            the humeur to set
	 */
	public void setHumeur(Humeur humeur) {
		this.humeur = humeur;
	}

	/**
	 * @return the fight
	 */
	public Fight getFight() {
		return fight;
	}

	protected Cell getBestCellForZoneSpell(int distToPlayer) {
		return null;
	}

	/**
	 * Trouve une case proche du joueur la plus proche possible du mob
	 * 
	 * @param distToPlayer
	 *            limite depuis le joueur
	 * @param m
	 *            le mob
	 * @return la case ou null si non trouvée
	 */
	protected Cell getCellNearMob(Mob m, int distToPlayer) {
		Set<Cell> aroundP = getCellsAroundPlayer(distToPlayer);
		Cell nearest = null;
		int dist = Integer.MAX_VALUE;
		for (Cell cell : aroundP) {
			int distance = cell.distanceManathan(m.getCellId());
			if (nearest == null || distance < dist) nearest = cell;
			dist = distance;
		}
		return nearest;
	}

	/**
	 * Trouve une case inateignable par un mob mais le plus proche possible de lui
	 * 
	 * @param distToPlayer
	 *            limite de distance depuis le joueur
	 * @return la case ou null si non trouvée
	 */
	protected Cell getCellNearAndSafeFromMob(Mob m, int distToPlayer) {
		Set<Cell> aroundP = getCellsAroundPlayer(distToPlayer);
		Iterator<Cell> it = aroundP.iterator();
		while (it.hasNext()) {
			Cell cell = it.next();
			if (cell.distanceManathan(m.getCellId()) <= m.getPm()) it.remove();
		}
		Cell found = null;
		int dist = Integer.MAX_VALUE;
		for (Cell cell : aroundP) {
			int distance = cell.distanceManathan(m.getCellId());
			if (found == null || distance < dist) found = cell;
		}
		return found;
	}

	/**
	 * Trouve une case la plus éloignée possible des mobs
	 * 
	 * @param distToPlayer
	 *            limite de distance depuis le joueur
	 * @return la case ou null si non trouvée
	 */
	protected Cell getCellAwayFromMob(int distToPlayer) {
		Set<Cell> aroundP = getCellsAroundPlayer(distToPlayer);
		Map<Cell, Integer> cellWithDistance = new HashMap<>();
		DofusMap map = getPerso().getMapInfos().getMap().getDofusMap();
		getFight().getMobs().forEach(m -> aroundP.forEach(cell -> {
			int dist = cell.distanceManathan(m.getCellId());
			Integer in = cellWithDistance.get(cell);
			if (in == null || dist < in) cellWithDistance.put(cell, dist);
		}));
		Cell far = null;
		int dist = 0;
		for (Entry<Cell, Integer> entry : cellWithDistance.entrySet()) {
			int distance = entry.getValue();
			if (far == null || distance > dist) far = entry.getKey();
			dist = distance;
		}
		return far;
	}

	protected Set<Cell> getCellsAroundPlayer(int dist) {
		Set<Cell> around = new HashSet<>();
		DofusMap map = getPerso().getMapInfos().getMap().getDofusMap();
		Arrays.stream(map.getCells()).filter(c -> Maps.distanceManathan(getPerso().getMapInfos().getCellId(), c.getId(), map.getWidth()) <= dist).forEach(around::add);
		return around;
	}

	/**
	 * @return the humeur
	 */
	public Humeur getHumeur() {
		return humeur;
	}

	@Override
	public BehaviorStopReason start() {
		if (getPerso().getFightInfos().getCurrentFight() == null) throw new IllegalStateException("Le combat est null");
		if (getBeginCellId() != -1) getPerso().getAbilities().getFightAbility().goToCellBeforeStart(getBeginCellId());
		waitCanStartFight();
		getPerso().getAbilities().getFightAbility().beReady(true);
		while (!getPerso().getFightInfos().getCurrentFight().isEnded())
			if (getPerso().getFightInfos().getCurrentFight().getCurrentTurn() == getPerso()) playTurn();
		return BehaviorStopReason.FINISHED;
	}

	public abstract void playTurn();

	public void tryHuman() {
		if (humanFight()) botWait(r.nextInt(3000) + 1000, TimeUnit.MILLISECONDS);
	}

	protected void waitCanStartFight() {
		while (!getPerso().getFightInfos().canStartCombat()) // attente de pouvoir start le combat
			;
	}

	public static enum Humeur {
		AGRESSIVE, // rush mob
		DEFENSIVE, // wait mob
		FUYARD, // back off
		TROUILLARD, // regroupe avec la team
		INDEPENDANT // s'éloigne des autres
	}

}
