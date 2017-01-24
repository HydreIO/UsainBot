package fr.aresrpg.eratz.domain.data.player.info;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.structures.stat.Stat;
import fr.aresrpg.dofus.util.*;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.dofus.util.Pathfinding.PathValidator;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.tofumanchou.domain.data.Spell;
import fr.aresrpg.tofumanchou.domain.data.entity.Entity;
import fr.aresrpg.tofumanchou.domain.data.entity.mob.Mob;
import fr.aresrpg.tofumanchou.domain.data.entity.mob.MobGroup;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Player;
import fr.aresrpg.tofumanchou.domain.util.Validators;
import fr.aresrpg.tofumanchou.infra.data.*;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public class FightUtilities extends Info {

	public FightUtilities(BotPerso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {

	}

	/**
	 * Trouve toute les cells situées au commencement d'une ligne de mob de range définie
	 * 
	 * @param spellRange
	 *            la range du sort
	 * @param safeForAllies
	 *            si le ligne de mob ne doit pas contenir d'alliés
	 * @return une list de pair "cell de départ de la ligne de mob" -
	 */
	public List<Pair<ManchouCell, Integer>> getCellsForLineZoneSpell(int spellRange, boolean safeForAllies) {
		Map<Integer, List<ManchouCell>> byX = new HashMap<>();
		Map<Integer, List<ManchouCell>> byY = new HashMap<>();
		getPerso().getPerso().getMap().getEntities().values().stream().filter(e -> !e.isDead() && !isAlly(e)).forEach(e -> {
			ManchouMap map = getPerso().getPerso().getMap();
			ManchouCell cell = map.getCells()[e.getCellId()];
			List<ManchouCell> listX = byX.get(cell.getX());
			List<ManchouCell> listY = byY.get(cell.getY());
			if (listX == null) byX.put(cell.getX(), listX = new ArrayList<>());
			if (listY == null) byY.put(cell.getY(), listY = new ArrayList<>());
			listX.add(cell);
			listY.add(cell);
		});
		List<List<ManchouCell>> sameX = new ArrayList<>();
		List<List<ManchouCell>> sameY = new ArrayList<>();
		Predicate<List> filter = l -> l.size() > 1;
		byX.values().stream().filter(filter).forEach(sameX::add);
		byY.values().stream().filter(filter).forEach(sameY::add);
		return null; // TODO WIP
	}

	/**
	 * Trouve les cell englobant au moins 2mobs dans la range définie
	 * 
	 * @param spellRange
	 *            la range
	 * @param free
	 *            si la cellule doit être libre
	 * @param safeForAllies
	 *            si la cell ne doit toucher aucun alliés
	 * @return une Pair cell - nombre de mob touché
	 */
	public List<Pair<ManchouCell, Integer>> getCellsForZoneSpell(int spellRange, boolean free, boolean safeForAllies) {
		List<FightNode> cells = new ArrayList<FightNode>() {
			@Override
			public boolean add(FightNode e) {
				if (contains(e)) {
					get(indexOf(e)).incrementPrice();
					return false;
				}
				return super.add(e);
			}
		};
		ManchouMap map = getPerso().getPerso().getMap();
		for (Entity e : getPerso().getPerso().getMap().getEntities().values()) {
			if (e.isDead() || e instanceof MobGroup || isAlly(e)) continue;
			ManchouCell mobcell = map.getCells()[e.getCellId()];
			Set<ManchouCell> cellsAroundCell = getCellsAroundCell(mobcell.getId(), spellRange);
			if (free) cellsAroundCell.removeIf(ManchouCell::hasEntityOn);
			cellsAroundCell.stream().map(c -> new FightNode(c.getId())).forEach(cells::add);
		}
		if (safeForAllies) {
			for (Entity e : getPerso().getPerso().getMap().getEntities().values()) {
				if (e.getLife() < 1 || !isAlly(e)) continue;
				Set<ManchouCell> cellsAroundCell = getCellsAroundCell(e.getCellId(), spellRange);
				cells.removeIf(cellsAroundCell.stream().map(c -> new FightNode(c.getId())).collect(Collectors.toSet())::contains);
			}
		}
		cells.removeIf(FightNode::valueUnder1);
		cells.sort(Comparator.comparingInt(FightNode::getAmount).reversed());
		return cells.stream().map(n -> new Pair<ManchouCell, Integer>(map.getCells()[n.id], n.amount)).collect(Collectors.toList());
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
	public ManchouCell getCellNearMob(final Entity m, final int distToPlayer, boolean free) {
		final Set<ManchouCell> aroundP = getCellsAroundPlayer(distToPlayer);
		ManchouCell nearest = null;
		int dist = Integer.MAX_VALUE;
		for (final ManchouCell cell : aroundP) {
			if (!cell.isWalkeable(false) || !cell.isLineOfSight()) continue;
			if (free && cell.hasEntityOn()) continue;
			final int distance = cell.distanceManathan(m.getCellId());
			if (nearest == null || distance < dist) {
				nearest = cell;
				dist = distance;
			}
		}
		return nearest;
	}

	public int getMissingMaxPoFor(final Spell spell, final int targetCell) {
		int maxPo = spell.getMaxPo() + getPerso().getPerso().getStat(Stat.PO).getTotal();
		if (maxPo < 0) maxPo = 0;
		final int cell = getPerso().getPerso().getCellId();
		final int width = getPerso().getPerso().getMap().getWidth();
		final int height = getPerso().getPerso().getMap().getHeight();
		return Maps.distanceManathan(cell, targetCell, width, height) - maxPo;
	}

	public int getMissingMinPoFor(final Spell spell, final int targetCell) {
		final int minPo = spell.getMinPo();
		final int cell = getPerso().getPerso().getCellId();
		final int width = getPerso().getPerso().getMap().getWidth();
		final int height = getPerso().getPerso().getMap().getHeight();
		return minPo - Maps.distanceManathan(cell, targetCell, width, height);
	}

	public int getMaxPoFor(Spell spell) {
		return spell.getMaxPo() + getPerso().getPerso().getStat(Stat.PO).getTotal();
	}

	public boolean hasMinPoFor(final Spell spell, final int targetCell) {
		final int minPo = spell.getMinPo();
		final int cell = getPerso().getPerso().getCellId();
		final int width = getPerso().getPerso().getMap().getWidth();
		final int height = getPerso().getPerso().getMap().getHeight();
		return Maps.distanceManathan(cell, targetCell, width, height) >= minPo;
	}

	public boolean hasMaxPoFor(final Spell spell, final int targetCell) {
		final int maxPo = spell.getMaxPo() + getPerso().getPerso().getStat(Stat.PO).getTotal();
		final int cell = getPerso().getPerso().getCellId();
		final int width = getPerso().getPerso().getMap().getWidth();
		final int height = getPerso().getPerso().getMap().getHeight();
		LOGGER.error("[playerCell:" + cell + "][targetCell:" + targetCell + "] distance = " + Maps.distanceManathan(cell, targetCell, width, height) + " maxpo = " + maxPo);
		return Maps.distanceManathan(cell, targetCell, width, height) <= maxPo;
	}

	public void runToMob(final Entity m, final boolean safely, int pmToUse) {
		final ManchouCell cellNearMob = safely ? getCellNearAndSafeFromMob(m, pmToUse, true) : getCellNearMob(m, pmToUse, true);
		if (cellNearMob != null)
			runTo(cellNearMob.getId());
		else LOGGER.error("cellNearMob est null wtf ! ent:" + m);
	}

	public boolean runAwayFromMobs() {
		try {
			ManchouCell c = getCellAwayFromMob(getPerso().getPerso().getPm());
			LOGGER.debug("pm player = " + getPerso().getPerso().getPm());
			if (c != null) {
				LOGGER.severe("cell away from mob = " + c.getId());
				runTo(c.getId());
				return true;
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
		return false;
	}

	/**
	 * Retourne true si le joueur est a une distance safe des mobs
	 * 
	 * @param deep
	 *            le nombre de tour a l'avance (en gros pour une valeure de deux sa multiplie les pm des mobs par deux)
	 * @return
	 */
	public boolean isSafeFromMobs(int deep) {
		if (deep < 2) deep = 1;
		int team = getPerso().getPerso().getTeam();
		for (Entity e : getPerso().getPerso().getMap().getEntities().values()) {
			if (e.isDead() || isAlly(e)) continue;
			int dist = Maps.distanceManathan(getPerso().getPerso().getCellId(), e.getCellId(), getPerso().getPerso().getMap().getWidth(), getPerso().getPerso().getMap().getHeight()) - 1;//-1 car pas besoin d'arriver sur la cell
			if (dist <= e.getPm() * deep) return false;
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	public void runTo(final int cell) {
		final int width = getPerso().getPerso().getMap().getWidth();
		final int height = getPerso().getPerso().getMap().getHeight();
		final int cellId = getPerso().getPerso().getCellId();
		if (cell == cellId) {
			LOGGER.debug("déja sur la cell");
			return;
		}
		int dist = Maps.distanceManathan(cellId, cell, width, height);
		final PathValidator canGo = (x1, y1, x2, y2) -> {
			final int id = Maps.getIdRotated(x2, y2, width, height);
			ManchouCell manchouCell = getPerso().getPerso().getMap().getCells()[id];
			return !manchouCell.hasLivingEntityOn() && manchouCell.isWalkeable(false) && manchouCell.isLineOfSight();
		};
		final List<Node> cellPath = Pathfinding.getCellPath(cellId, cell, getPerso().getPerso().getMap().getProtocolCells(), width, height, Pathfinding::getNeighborsWithoutDiagonals, canGo);
		LOGGER.warning("Trying to move from " + cellId + " to " + cell + " path=" + cellPath);
		if (cellPath == null) throw new NullPointerException("PATH INVALID -_-");
		getPerso().getPerso().move(cellPath);
	}

	public boolean pathValidFor(int cell) {
		final int width = getPerso().getPerso().getMap().getWidth();
		final int height = getPerso().getPerso().getMap().getHeight();
		final int cellId = getPerso().getPerso().getCellId();
		final PathValidator canGo = (x1, y1, x2, y2) -> {
			final int id = Maps.getIdRotated(x2, y2, width, height);
			ManchouCell manchouCell = getPerso().getPerso().getMap().getCells()[id];
			return !manchouCell.hasLivingEntityOn() && manchouCell.isWalkeable(false);
		};
		return Pathfinding.getCellPath(cellId, cell, getPerso().getPerso().getMap().getProtocolCells(), width, height, Pathfinding::getNeighborsWithoutDiagonals, canGo) != null;
	}

	/**
	 * Trouve une case inateignable par un mob mais le plus proche possible de lui
	 * 
	 * @param distToPlayer
	 *            limite de distance depuis le joueur
	 * @return la case ou null si non trouvée
	 */
	public ManchouCell getCellNearAndSafeFromMob(final Entity m, final int distToPlayer, boolean free) {
		final Set<ManchouCell> aroundP = getCellsAroundPlayer(distToPlayer);
		final Iterator<ManchouCell> it = aroundP.iterator();
		while (it.hasNext()) {
			final ManchouCell cell = it.next();
			if (free && (!cell.isWalkeable(false) || cell.hasLivingEntityOn())) it.remove();
			if (cell.distanceManathan(m.getCellId()) <= m.getPm()) it.remove();
		}
		ManchouCell found = null;
		int dist = Integer.MAX_VALUE;
		for (final ManchouCell cell : aroundP) {
			final int distance = cell.distanceManathan(m.getCellId());
			if (found == null || distance < dist) {
				found = cell;
				dist = distance;
			}
		}
		return found;
	}

	/**
	 * Trouve toute les case safe autour du joueur par rapport a un mob
	 * 
	 * @param m
	 *            le mob
	 * @param distToPlayer
	 *            distance max
	 * @return les cases ou un set vide si aucune case trouvée
	 */
	public Set<ManchouCell> getCellsSafeFromMob(final Entity m, final int distToPlayer) {
		final Set<ManchouCell> aroundP = getCellsAroundPlayer(distToPlayer);
		final Iterator<ManchouCell> it = aroundP.iterator();
		while (it.hasNext()) {
			final ManchouCell cell = it.next();
			if (cell.hasLivingEntityOn() || !cell.isWalkeable(false) || cell.distanceManathan(m.getCellId()) <= m.getPm() || !pathValidFor(cell.getId())) it.remove();
		}
		return aroundP;
	}

	/**
	 * Trouve une case a distance X du joueur ayant la ligne de vue pour toucher une cell
	 * 
	 * @param distToPlayer
	 *            distance max du joueur
	 * @param targetCell
	 *            ennemy
	 * @param range
	 *            portee du sort
	 * @param line
	 *            lancer en ligne
	 * @return une case accessible pouvant viser la cible
	 */
	public ManchouCell getCellToTargetMob(int distToPlayer, int targetCell, int range, boolean line) {
		Set<ManchouCell> cellsAroundPlayer = getCellsAroundPlayer(distToPlayer);
		for (ManchouCell c : cellsAroundPlayer) {
			if (c.hasEntityOn() || !c.isWalkeable(false)) continue;
			if (line && !c.isOnSameLineOrCollumn(getPerso().getPerso().getMap().getCells()[targetCell])) continue;
			List<Integer> acc = getAccessibleCells(c.getId(), range);
			if (acc.contains(targetCell)) return c;
		}
		return null;
	}

	public Entity getWeakestEnnemy() {
		Entity weak = null;
		for (Entity e : getPerso().getPerso().getMap().getEntities().values()) {
			if (e.isDead() || isAlly(e)) continue;
			if (weak == null || e.getLife() < weak.getLife()) weak = e;
		}
		return weak;
	}

	public Entity getNearestEnnemy() {
		int dist = Integer.MAX_VALUE;
		Entity near = null;
		for (final Entity m : getPerso().getPerso().getMap().getEntities().values()) {
			if (m.isDead() || isAlly(m)) continue;
			if (near == null) near = m;
			final int distance = Maps.distanceManathan(getPerso().getPerso().getCellId(), m.getCellId(), getPerso().getPerso().getMap().getWidth(), getPerso().getPerso().getMap().getHeight());//pas besoin du -1 car on cherche le plus pres
			if (distance < dist) {
				dist = distance;
				near = m;
			}
		}
		return near;
	}

	public boolean isAlly(Entity ent) {
		int team = getPerso().getPerso().getTeam();
		if (ent instanceof Perso) {
			ManchouPerso pers = (ManchouPerso) ent;
			return pers.getTeam() == team;
		} else if (ent instanceof Player) {
			ManchouPlayerEntity pl = (ManchouPlayerEntity) ent;
			return pl.getTeam() == team;
		} else if (ent instanceof Mob) {
			ManchouMob mm = (ManchouMob) ent;
			return mm.getTeam() == team;
		} else return false;
	}

	/**
	 * Trouve toutes les case safe from mobs
	 * 
	 * @param distToPlayer
	 *            limite de distance depuis le joueur
	 * @return les cases
	 */
	public Set<ManchouCell> getCellsAwayFromMobs(final int distToPlayer) {
		return getPerso().getPerso().getMap().getEntities().values().stream().map(e -> getCellsSafeFromMob(e, distToPlayer)).reduce((a, b) -> {
			a.addAll(b);
			return a;
		}).orElseGet(HashSet::new);
	}

	/**
	 * Trouve une case la plus éloignée possible des mobs
	 * 
	 * @param distToPlayer
	 *            limite de distance depuis le joueur
	 * @return la case ou null si non trouvée
	 */
	public ManchouCell getCellAwayFromMob(final int distToPlayer) {
		final Set<ManchouCell> aroundP = getCellsAroundPlayer(distToPlayer);
		Map<ManchouCell, Integer> cost = new HashMap<>();
		for (ManchouCell c : aroundP) {
			if (c.hasLivingEntityOn() || !c.isWalkeable(false) || !c.isLineOfSight() || !pathValidFor(c.getId())) continue;
			int pts = 0;
			for (Entity e : getPerso().getPerso().getMap().getEntities().values()) {
				if (e.getLife() < 1 || isAlly(e)) continue;
				int cl = e.getCellId();
				pts += c.distanceManathan(cl);
			}
			cost.put(c, pts);
		}
		ManchouCell far = null;
		int maxpts = 0;
		for (Entry<ManchouCell, Integer> i : cost.entrySet()) {
			if (i.getValue() > maxpts) {
				far = i.getKey();
				maxpts = i.getValue();
			}
		}
		return far;
	}

	/**
	 * Trouve toute les cells a distance X du joueur qui sont inaccessible au niveau de la ligne de vue a partir de la cell en parametre
	 * 
	 * @param distToPlayer
	 *            la distance max du joueur
	 * @param cell
	 *            la cell
	 * @return les cells inaccessibles
	 */
	public Set<ManchouCell> getCellInaccessibleFrom(int distToPlayer, int cell) {
		Set<ManchouCell> cellsAroundPlayer = getCellsAroundPlayer(distToPlayer);
		List<Integer> accessibleCells = getAccessibleCells(cell, 64);
		cellsAroundPlayer.removeIf(c -> accessibleCells.contains(c.getId()));
		return cellsAroundPlayer;
	}

	/**
	 * Trouve toute les cells a distance x du joueur
	 * 
	 * @param dist
	 *            la distance
	 * @param accessible
	 *            si le path pour acceder au cells doit etre valide et d'une taille inférieur ou égale à la distance
	 * @return les cells
	 */
	public Set<ManchouCell> getCellsAroundPlayer(final int dist) {
		final Set<ManchouCell> around = new HashSet<>();
		int cell = getPerso().getPerso().getCellId();
		ManchouMap map = getPerso().getPerso().getMap();
		Arrays.stream(getPerso().getPerso().getMap().getCells())
				.filter(c -> {
					if (c.distanceManathan(cell) > dist) return false;
					List<Node> cellPath = Pathfinding.getCellPath(cell, c.getId(), map.getProtocolCells(), map.getWidth(), map.getHeight(), Pathfinding::getNeighborsWithoutDiagonals,
							Validators.freeCell(map));
					return cellPath != null && cellPath.size() <= dist + 1;
				})
				.forEach(around::add);
		return around;
	}

	public Set<ManchouCell> getCellsAroundCell(final int cell, int dist) {
		final Set<ManchouCell> around = new HashSet<>();
		Arrays.stream(getPerso().getPerso().getMap().getCells())
				.filter(c -> Maps.distanceManathan(cell, c.getId(), getPerso().getPerso().getMap().getWidth(), getPerso().getPerso().getMap().getHeight()) <= dist)
				.forEach(around::add);
		return around;
	}

	public List<Integer> getAccessibleCells(int range) {
		return ShadowCasting.getAccesibleCells(getPerso().getPerso().getCellId(), range, getPerso().getPerso().getMap().serialize(), getPerso().getPerso().getMap().cellAccessible().negate()).stream()
				.map(Cell::getId).collect(Collectors.toList());
	}

	public List<Integer> getAccessibleCells(int origin, int range) {
		return ShadowCasting.getAccesibleCells(origin, range, getPerso().getPerso().getMap().serialize(), getPerso().getPerso().getMap().cellAccessible().negate()).stream().map(Cell::getId)
				.collect(Collectors.toList());
	}

	public static class FightNode {
		int id;
		int amount;

		public FightNode(int id) {
			this.id = id;
		}

		void incrementPrice() {
			amount++;
		}

		/**
		 * @return the amount
		 */
		public int getAmount() {
			return amount;
		}

		boolean valueUnder1() {
			return amount < 1;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) return true;
			if (obj == null) return false;
			return obj instanceof FightNode && ((FightNode) obj).id == id;
		}

		@Override
		public int hashCode() {
			return id;
		}
	}

}
