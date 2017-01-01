package fr.aresrpg.eratz.domain.listener;

import fr.aresrpg.commons.domain.event.Listener;
import fr.aresrpg.dofus.structures.Skills;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.util.Maps;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.util.InterractUtil;
import fr.aresrpg.tofumanchou.domain.Manchou;
import fr.aresrpg.tofumanchou.domain.data.enums.DofusMobs;
import fr.aresrpg.tofumanchou.domain.event.player.MapJoinEvent;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.*;

import java.awt.Point;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class MapListener implements Listener {

	private static MapListener instance;
	private Set<Point> path = new HashSet<>();
	private Set<Interractable> ressources = new HashSet<>();
	private Set<DofusMobs> mobs = new HashSet<>();

	private boolean changeMap;
	private boolean harvest;
	private boolean fightSelectedMobs;
	private boolean joinFight;
	private boolean fightAllMob;
	private boolean canGoIndoor;

	public MapListener() {
		instance = this;
		Manchou.registerEvent(this);
	}

	public void onMap(MapJoinEvent e) { // veriff que c'est pas un fight
		if (e.getMap().isEnded()) Executors.SCHEDULED.schedule(() -> this.onMap(BotFather.getPerso(e.getClient()), (ManchouMap) e.getMap()), 1, TimeUnit.SECONDS);
	}

	private void onMap(BotPerso perso, ManchouMap map) {
		if (!canGoIndoor && !map.isOutdoor()) {
			ManchouCell cl = perso.getPerso().getNearestTeleporters()[0];
			perso.getPerso().moveToCell(cl.getId(), true, true, false);
			return;
		}
		if (harvest) {
			ManchouJob job = perso.getPerso().getJob();
			ManchouCell near = null;
			int persocell = perso.getPerso().getCellId();
			ManchouCell persocellc = map.getCells()[persocell];
			int x = persocellc.getX();
			int y = persocellc.getY();
			for (ManchouCell c : perso.getPerso().getMap().getCells()) {
				if (!c.isInterractable()) continue;
				Interractable i = c.getInterractable();
				if (c.isRessource() && ressources.contains(i)) {
					Skills skill = InterractUtil.getSkillFor(i, job.getType());
					if (skill == null) continue;
					Node[] neighbors = Pathfinding.getNeighbors(new Node(c.getX(), c.getY()));
					for (Node n : neighbors) {
						if (!Maps.isInMap(n.getX(), n.getY(), map.getWidth(), map.getHeight())) continue;
						ManchouCell manchouCell = map.getCells()[Maps.getId(n.getX(), n.getY(), map.getWidth(), map.getHeight())];
						if (!manchouCell.isWalkeable() || manchouCell.isTeleporter() || manchouCell.hasMobOn()) continue;
						Cell[] protocolCells = map.getProtocolCells();
						List<Point> cpath = Pathfinding.getCellPath(x, y, manchouCell.getX(), manchouCell.getY(), protocolCells, map.getWidth(), map.getHeight(), true,
								point -> perso.getPerso().canGoOnCellAvoidingMobs(Maps.getId(point.x, point.y, map.getWidth(), map.getHeight())));
						if (cpath == null) continue;
						float time = Pathfinding.getPathTime(cpath, protocolCells, map.getWidth(), map.getHeight(), false) * 30;
						Executors.SCHEDULED.schedule(() -> perso.getPerso().interract(skill, c.getId()), (long) time, TimeUnit.MILLISECONDS);
					}
				}
			}
		}
	}

	/**
	 * @return the instance
	 */
	public static MapListener getInstance() {
		return instance;
	}

	/**
	 * @return the path
	 */
	public Set<Point> getPath() {
		return path;
	}

	/**
	 * @return the ressources
	 */
	public Set<Interractable> getRessources() {
		return ressources;
	}

	/**
	 * @return the mobs
	 */
	public Set<DofusMobs> getMobs() {
		return mobs;
	}

	/**
	 * @return the changeMap
	 */
	public boolean isChangeMap() {
		return changeMap;
	}

	/**
	 * @return the harvest
	 */
	public boolean isHarvest() {
		return harvest;
	}

	/**
	 * @return the fightSelectedMobs
	 */
	public boolean isFightSelectedMobs() {
		return fightSelectedMobs;
	}

	/**
	 * @return the joinFight
	 */
	public boolean isJoinFight() {
		return joinFight;
	}

	/**
	 * @return the fightAllMob
	 */
	public boolean isFightAllMob() {
		return fightAllMob;
	}

	/**
	 * @return the canGoIndoor
	 */
	public boolean isCanGoIndoor() {
		return canGoIndoor;
	}

	/**
	 * @param changeMap
	 *            the changeMap to set
	 */
	public void setChangeMap(boolean changeMap) {
		this.changeMap = changeMap;
	}

	/**
	 * @param harvest
	 *            the harvest to set
	 */
	public void setHarvest(boolean harvest) {
		this.harvest = harvest;
	}

	/**
	 * @param fightSelectedMobs
	 *            the fightSelectedMobs to set
	 */
	public void setFightSelectedMobs(boolean fightSelectedMobs) {
		this.fightSelectedMobs = fightSelectedMobs;
	}

	/**
	 * @param joinFight
	 *            the joinFight to set
	 */
	public void setJoinFight(boolean joinFight) {
		this.joinFight = joinFight;
	}

	/**
	 * @param fightAllMob
	 *            the fightAllMob to set
	 */
	public void setFightAllMob(boolean fightAllMob) {
		this.fightAllMob = fightAllMob;
	}

	/**
	 * @param canGoIndoor
	 *            the canGoIndoor to set
	 */
	public void setCanGoIndoor(boolean canGoIndoor) {
		this.canGoIndoor = canGoIndoor;
	}

}
