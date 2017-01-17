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
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Player;
import fr.aresrpg.tofumanchou.infra.data.*;

import java.util.*;
import java.util.Map.Entry;
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

	public Cell getBestCellForZoneSpell(final int distToPlayer) {
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
	public ManchouCell getCellNearMob(final Entity m, final int distToPlayer, boolean free) {
		final Set<ManchouCell> aroundP = getCellsAroundPlayer(distToPlayer);
		ManchouCell nearest = null;
		int dist = Integer.MAX_VALUE;
		for (final ManchouCell cell : aroundP) {
			if (!cell.isWalkeable()) continue;
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
		if (maxPo < 1) maxPo = 1;
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
		final int pm = getPerso().getPerso().getPm();
		if (pmToUse > pm) pmToUse = pm;
		final ManchouCell cellNearMob = safely ? getCellNearAndSafeFromMob(m, pmToUse, true) : getCellNearMob(m, pmToUse, true);
		runTo(cellNearMob.getId());
	}

	public void runAwayFromMobs() {
		try {
			ManchouCell c = getCellAwayFromMob(getPerso().getPerso().getPm());
			LOGGER.debug("pm player = " + getPerso().getPerso().getPm());
			if (c != null) {
				LOGGER.severe("cell away from mob = " + c.getId());
				runTo(c.getId());
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	public boolean isSafeFromMobs() {
		int team = getPerso().getPerso().getTeam();
		for (Entity e : getPerso().getPerso().getMap().getEntities().values()) {
			if (e instanceof Perso) continue;
			else if (e instanceof Player) {
				ManchouPlayerEntity pl = (ManchouPlayerEntity) e;
				if (pl.getTeam() == team) continue;
				int dist = Maps.distanceManathan(getPerso().getPerso().getCellId(), pl.getCellId(), getPerso().getPerso().getMap().getWidth(), getPerso().getPerso().getMap().getHeight()) - 1;//-1 car pas besoin d'arriver sur la cell
				if (dist <= pl.getPm()) return false;
			} else if (e instanceof Mob) {
				ManchouMob m = (ManchouMob) e;
				if (m.getTeam() == team) continue;
				int dist = Maps.distanceManathan(getPerso().getPerso().getCellId(), m.getCellId(), getPerso().getPerso().getMap().getWidth(), getPerso().getPerso().getMap().getHeight()) - 1;//-1 car pas besoin d'arriver sur la cell
				if (dist <= m.getPm()) return false;
			}
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
			return !manchouCell.hasEntityOn() && manchouCell.isWalkeable() && manchouCell.isLineOfSight();
		};
		final List<Node> cellPath = Pathfinding.getCellPath(cellId, cell, getPerso().getPerso().getMap().getProtocolCells(), width, height, Pathfinding::getNeighborsWithoutDiagonals, canGo);
		// perso.setPm(perso.getPm() - dist); // TEMP REMOVE PM car on attend pas que le serv nous le dise pour pouvoir finir notre tour, de tt façon il reset apres
		LOGGER.warning("Trying to move from " + cellId + " to " + cell + " path=" + cellPath);
		if (cellPath == null) throw new NullPointerException("PATH INVALID -_-");
		getPerso().getPerso().move(cellPath);
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
			if (free && !cell.isWalkeable()) it.remove();
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
			if (cell.hasEntityOn() || !cell.isWalkeable() || cell.distanceManathan(m.getCellId()) <= m.getPm()) it.remove();
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
			if (c.hasEntityOn() || !c.isWalkeable()) continue;
			if (line && !c.isOnSameLineOrCollumn(getPerso().getPerso().getMap().getCells()[targetCell])) continue;
			List<Integer> acc = getAccessibleCells(c.getId(), range);
			if (acc.contains(targetCell)) return c;
		}
		return null;
	}

	public Entity getNearestEnnemy() {
		int dist = Integer.MAX_VALUE;
		Entity near = null;
		for (final Entity m : getPerso().getPerso().getMap().getEntities().values()) {
			if (m.getLife() < 1) continue;
			// continue if allies
			if (m instanceof Perso) continue;
			else if (m instanceof Player) {
				ManchouPlayerEntity pl = (ManchouPlayerEntity) m;
				if (pl.getTeam() == getPerso().getPerso().getTeam()) continue;
			} else if (m instanceof Mob) {
				ManchouMob mm = (ManchouMob) m;
				if (mm.getTeam() == getPerso().getPerso().getTeam()) continue;
			}
			if (near == null) near = m;
			final int distance = Maps.distanceManathan(getPerso().getPerso().getCellId(), m.getCellId(), getPerso().getPerso().getMap().getWidth(), getPerso().getPerso().getMap().getHeight());//pas besoin du -1 car on cherche le plus pres
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
	public ManchouCell getCellAwayFromMob(final int distToPlayer) {
		final Set<ManchouCell> aroundP = getCellsAroundPlayer(distToPlayer);
		Map<ManchouCell, Integer> cost = new HashMap<>();
		int team = getPerso().getPerso().getTeam();
		for (ManchouCell c : aroundP) {
			if (c.hasEntityOn() || !c.isWalkeable()) continue;
			int pts = 0;
			for (Entity e : getPerso().getPerso().getMap().getEntities().values()) {
				// continue if allies
				if (e instanceof Perso) continue;
				else if (e instanceof Player) {
					ManchouPlayerEntity pl = (ManchouPlayerEntity) e;
					if (pl.getTeam() == team) continue;
				} else if (e instanceof Mob) {
					ManchouMob m = (ManchouMob) e;
					if (m.getTeam() == team) continue;
				}
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

	public Set<ManchouCell> getCellsAroundPlayer(final int dist) {
		final Set<ManchouCell> around = new HashSet<>();
		Arrays.stream(getPerso().getPerso().getMap().getCells())
				.filter(c -> Maps.distanceManathan(getPerso().getPerso().getCellId(), c.getId(), getPerso().getPerso().getMap().getWidth(), getPerso().getPerso().getMap().getHeight()) <= dist)
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
}
