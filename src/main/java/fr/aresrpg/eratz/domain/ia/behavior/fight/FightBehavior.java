package fr.aresrpg.eratz.domain.ia.behavior.fight;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.protocol.game.movement.MovementAction;
import fr.aresrpg.dofus.structures.game.FightEntity;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.structures.stat.Stat;
import fr.aresrpg.dofus.util.*;
import fr.aresrpg.eratz.domain.TheBotFather;
import fr.aresrpg.eratz.domain.data.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.data.dofus.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.info.MapInfo;
import fr.aresrpg.eratz.domain.data.player.object.Spell;
import fr.aresrpg.eratz.domain.ia.ability.fight.FightAbility;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;
import fr.aresrpg.eratz.domain.util.config.Variables;

import java.awt.Point;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * 
 * @since
 */
public abstract class FightBehavior extends Behavior {

	private Humeur humeur;

	/**
	 * @param perso
	 */
	public FightBehavior(final Perso perso) {
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
	public void setHumeur(final Humeur humeur) {
		this.humeur = humeur;
	}

	/**
	 * @return the fight
	 */
	public Fight getFight() {
		return getPerso().getFightInfos().getCurrentFight();
	}

	protected Cell getBestCellForZoneSpell(final int distToPlayer) {
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
	protected Cell getCellNearMob(final Pair<MovementAction, FightEntity> m, final int distToPlayer) {
		final Set<Cell> aroundP = getCellsAroundPlayer(distToPlayer);
		Cell nearest = null;
		int dist = Integer.MAX_VALUE;
		for (final Cell cell : aroundP) {
			final int distance = cell.distanceManathan(m.getFirst().getCellId());
			if (nearest == null || distance < dist) nearest = cell;
			dist = distance;
		}
		return nearest;
	}

	protected int getMissingMaxPoFor(final Spell spell, final int targetCell) {
		int maxPo = spell.getMaxPo() + getPerso().getStatsInfos().getStat(Stat.PO).getTotal();
		if (maxPo < 1) maxPo = 1;
		final int cell = getPerso().getFightInfos().getCurrentFight().getPositionOf(getPerso().getId());
		final DofusMap map = getPerso().getMapInfos().getMap().getDofusMap();
		final int width = map.getWidth();
		return Maps.distanceManathan(cell, targetCell, width) - maxPo;
	}

	protected int getMissingMinPoFor(final Spell spell, final int targetCell) {
		final int minPo = spell.getMinPo();
		final int cell = getPerso().getFightInfos().getCurrentFight().getPositionOf(getPerso().getId());
		final DofusMap map = getPerso().getMapInfos().getMap().getDofusMap();
		final int width = map.getWidth();
		return minPo - Maps.distanceManathan(cell, targetCell, width);
	}

	protected boolean hasMinPoFor(final Spell spell, final int targetCell) {
		final int minPo = spell.getMinPo();
		final int cell = getPerso().getFightInfos().getCurrentFight().getPositionOf(getPerso().getId());
		final DofusMap map = getPerso().getMapInfos().getMap().getDofusMap();
		final int width = map.getWidth();
		return Maps.distanceManathan(cell, targetCell, width) >= minPo;
	}

	protected boolean hasMaxPoFor(final Spell spell, final int targetCell) {
		final int maxPo = spell.getMaxPo() + getPerso().getStatsInfos().getStat(Stat.PO).getTotal();
		final int cell = getPerso().getFightInfos().getCurrentFight().getPositionOf(getPerso().getId());
		final DofusMap map = getPerso().getMapInfos().getMap().getDofusMap();
		final int width = map.getWidth();
		TheBotFather.LOGGER.error("[playerCell:" + cell + "][targetCell:" + targetCell + "] distance = " + Maps.distanceManathan(cell, targetCell, width) + " maxpo = " + maxPo);
		return Maps.distanceManathan(cell, targetCell, width) <= maxPo;
	}

	protected void runToMob(final Pair<MovementAction, FightEntity> m, final boolean safely, int pmToUse) {
		final int pm = getPerso().getStatsInfos().getPM();
		if (pmToUse > pm) pmToUse = pm;
		final Cell cellNearMob = safely ? getCellNearAndSafeFromMob(m, pmToUse) : getCellNearMob(m, pmToUse);
		final DofusMap dofusMap = getPerso().getMapInfos().getMap().getDofusMap();
		final int width = dofusMap.getWidth();
		runTo(cellNearMob.getId());
	}

	protected void runAwayFromMobs() {
		Cell c = getCellAwayFromMob(getPerso().getStatsInfos().getPM());
		TheBotFather.LOGGER.severe("cell away from mob = " + c);
		if (c != null) runTo(c.getId());

	}

	protected boolean isSafeFromMobs() {
		for (Pair<MovementAction, FightEntity> m : getPerso().getFightInfos().getCurrentFight().getOpponents(getPerso().getId()).values())
			if (Maps.distanceManathan(m.getFirst().getCellId(), getPerso().getFightInfos().getCurrentFight().getPositionOf(getPerso().getId()),
					getPerso().getMapInfos().getMap().getDofusMap().getWidth()) - 1 <= m.getSecond().getPm())
				return false;
		return true;
	}

	protected void runTo(final int cell) {
		final BotMap map = getPerso().getMapInfos().getMap();
		final int width = map.getDofusMap().getWidth();
		final int cellId = getPerso().getFightInfos().getCurrentFight().getPositionOf(getPerso().getId());
		final int x = Maps.getX(cell, width);
		final int y = Maps.getY(cell, width);
		final int xto = Maps.getX(cellId, width);
		final int yto = Maps.getY(cellId, width);
		final Predicate<Point> cantGoOnCell = p -> {
			final int id = Maps.getId(p.x, p.y, width);
			return getPerso().getFightInfos().getCurrentFight().hasEntityOn(id);
		};
		final List<Point> cellPath = Pathfinding.getCellPath(x, y, xto, yto, map.getDofusMap().getCells(), width, false, cantGoOnCell.negate());
		TheBotFather.LOGGER.error("Trying to move x=" + x + ", y=" + y + " ,xto=" + xto + ", yto=" + yto + ", cells=" + Arrays.toString(map.getDofusMap().getCells()));
		TheBotFather.LOGGER.warning("Trying to move from " + cellId + " to " + cell + " path=" + cellPath);
		getPerso().getNavigation().moveToCell(cellPath, cell, false);
	}

	/**
	 * Trouve une case inateignable par un mob mais le plus proche possible de lui
	 * 
	 * @param distToPlayer
	 *            limite de distance depuis le joueur
	 * @return la case ou null si non trouvée
	 */
	protected Cell getCellNearAndSafeFromMob(final Pair<MovementAction, FightEntity> m, final int distToPlayer) {
		final Set<Cell> aroundP = getCellsAroundPlayer(distToPlayer);
		final Iterator<Cell> it = aroundP.iterator();
		while (it.hasNext()) {
			final Cell cell = it.next();
			if (cell.distanceManathan(m.getFirst().getCellId()) <= m.getSecond().getPm()) it.remove();
		}
		Cell found = null;
		final int dist = Integer.MAX_VALUE;
		for (final Cell cell : aroundP) {
			final int distance = cell.distanceManathan(m.getFirst().getCellId());
			if (found == null || distance < dist) found = cell;
		}
		return found;
	}

	protected Pair<MovementAction, FightEntity> getNearestEnnemy() {
		int dist = Integer.MAX_VALUE;
		Pair<MovementAction, FightEntity> near = null;
		final MapInfo mapInfos = getPerso().getMapInfos();
		for (final Pair<MovementAction, FightEntity> m : getPerso().getFightInfos().getCurrentFight().getOpponents(getPerso().getId()).values()) {
			final MovementAction action = m.getFirst();
			if (near == null) near = m;
			final int distance = Maps.distanceManathan(getPerso().getFightInfos().getCurrentFight().getPositionOf(getPerso().getId()), action.getCellId(), mapInfos.getMap().getDofusMap().getWidth());
			if (distance < dist) {
				dist = distance;
				near = m;
			}
		}
		return near;
	}

	/**
	 * Trouve une case la plus éloignée possible des mobs
	 * 
	 * @param distToPlayer
	 *            limite de distance depuis le joueur
	 * @return la case ou null si non trouvée
	 */
	protected Cell getCellAwayFromMob(final int distToPlayer) {
		final Set<Cell> aroundP = getCellsAroundPlayer(distToPlayer);
		Map<Cell, Integer> cost = new HashMap<>();
		for (Cell c : aroundP) {
			if (getFight().hasEntityOn(c.getId())) continue;
			int pts = 0;
			for (Pair<MovementAction, FightEntity> i : getFight().getOpponents(getPerso().getId()).values()) {
				int cl = i.getFirst().getCellId();
				pts += c.distanceManathan(cl);
			}
			cost.put(c, pts);
		}
		Cell far = null;
		int maxpts = 0;
		for (Entry<Cell, Integer> i : cost.entrySet()) {
			if (i.getValue() > maxpts) far = i.getKey();
		}
		return far;
	}

	protected Set<Cell> getCellsAroundPlayer(final int dist) {
		final Set<Cell> around = new HashSet<>();
		final DofusMap map = getPerso().getMapInfos().getMap().getDofusMap();
		int positionOf = getPerso().getFightInfos().getCurrentFight().getPositionOf(getPerso().getId());
		Arrays.stream(map.getCells()).filter(c -> Maps.distanceManathan(positionOf, c.getId(), map.getWidth()) <= dist).forEach(around::add);
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
		final FightAbility fa = getPerso().getAbilities().getFightAbility();
		if (getBeginCellId() != -1) fa.setPosition(getBeginCellId());
		if (getPerso().hasGroup()) fa.blockToGroup(true);
		else fa.blockCombat(true);
		waitCanStartFight();
		getPerso().getAbilities().getFightAbility().beReady(true);
		final Fight f = getPerso().getFightInfos().getCurrentFight();
		TheBotFather.LOGGER.severe("saaaaaaaaaaaaaaaaaaaaaaaaaaaart");
		while (!getPerso().getFightInfos().getCurrentFight().isEnded()) {
			Threads.uSleep(50, TimeUnit.MILLISECONDS); // gentil cpu ! pas cramer !
			if (f.getCurrentTurn() != null && f.getCurrentTurn().getFirst().getId() == getPerso().getId()) playTurn();
		}
		TheBotFather.LOGGER.severe("SORTIE");
		return BehaviorStopReason.FINISHED;
	}

	public abstract void playTurn();

	public void tryHuman() {
		if (humanFight()) Threads.uSleep(Randoms.nextBetween(2, 4), TimeUnit.SECONDS);
	}

	protected void waitCanStartFight() {
		while (!getPerso().getFightInfos().canStartCombat()) // attente de pouvoir start le combat
			Threads.uSleep(50, TimeUnit.MILLISECONDS); // gentil cpu ! pas cramer !
	}

	public static enum Humeur {
		AGRESSIVE, // rush mob
		DEFENSIVE, // wait mob
		FUYARD, // back off
		TROUILLARD, // regroupe avec la team
		INDEPENDANT // s'éloigne des autres
	}

}
