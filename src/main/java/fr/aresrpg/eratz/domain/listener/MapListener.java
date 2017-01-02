package fr.aresrpg.eratz.domain.listener;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.event.Listener;
import fr.aresrpg.commons.domain.event.Subscribe;
import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.dofus.structures.Orientation;
import fr.aresrpg.dofus.structures.Skills;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.util.Maps;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.Roads;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.state.BotState;
import fr.aresrpg.eratz.domain.event.PathEndEvent;
import fr.aresrpg.eratz.domain.util.ComparatorUtil;
import fr.aresrpg.eratz.domain.util.InterractUtil;
import fr.aresrpg.tofumanchou.domain.Manchou;
import fr.aresrpg.tofumanchou.domain.data.Account;
import fr.aresrpg.tofumanchou.domain.event.map.FrameUpdateEvent;
import fr.aresrpg.tofumanchou.domain.event.map.HarvestTimeReceiveEvent;
import fr.aresrpg.tofumanchou.domain.event.player.MapJoinEvent;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.*;

import java.awt.Point;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public class MapListener implements Listener {

	private static MapListener instance;

	private Map<Account, BotState> states = new HashMap<>();

	public MapListener() {
		instance = this;
		Manchou.registerEvent(this);
	}

	public BotState get(Account account) {
		BotState mapState = states.get(account);
		if (mapState == null) states.put(account, mapState = new BotState());
		return mapState;
	}

	@Subscribe
	public void onFrame(FrameUpdateEvent e) {
		BotState st = get(e.getClient());
		boolean finishedHarvest = e.getCell().getId() == st.currentRessource && e.getFrame() == 3;
		BotPerso perso = BotFather.getPerso(e.getClient());
		ManchouMap map = (ManchouMap) e.getClient().getPerso().getMap();
		if (finishedHarvest && !harvestRessource(BotFather.getPerso(e.getClient()), (ManchouMap) e.getClient().getPerso().getMap(), st)) {
			goToNextMap(perso, map, st);
		}
	}

	@Subscribe
	public void onRessourceStartHarvest(HarvestTimeReceiveEvent e) {
		BotState st = get(e.getClient());
		boolean steal = st.currentRessource == e.getCellId() && e.getClient().getPerso().getUUID() != e.getPlayer().getUUID();
		BotPerso perso = BotFather.getPerso(e.getClient());
		ManchouMap map = (ManchouMap) e.getClient().getPerso().getMap();
		if (steal && !harvestRessource(perso, map, st)) {
			st.needToGo = null;
			goToNextMap(perso, map, st);
		}
	}

	@Subscribe
	public void onMap(MapJoinEvent e) {
		if (e.getMap().isEnded()) Executors.SCHEDULED.schedule(() -> this.onMap(BotFather.getPerso(e.getClient()), (ManchouMap) e.getMap()), 1, TimeUnit.SECONDS);
	}

	private void onMap(BotPerso perso, ManchouMap map) {
		LOGGER.success("ON MAP ================= 1");
		BotState st = get(perso.getPerso().getAccount());
		LOGGER.success("ON MAP ================= 2");
		Roads.checkMap(map, perso); // enregistre les tp manquant etc
		LOGGER.success("ON MAP ================= 3 " + !st.canGoIndoor + " " + !map.isOutdoor());
		try {
			if (!st.canGoIndoor && !map.isOutdoor()) {
				LOGGER.success("ON MAP ================= 4");
				ManchouCell cl = perso.getPerso().getNearestTeleporters()[0];
				LOGGER.success("ON MAP ================= 5");
				perso.getPerso().moveToCell(cl.getId(), true, true, false);
				LOGGER.success("ON MAP ================= 6");
				return;
			}
			if (st.needToGo != null) {
				if (map.isOnCoords(st.needToGo.x, st.needToGo.y)) st.needToGo = null;
				else {
					goToNextMap(perso, map, st);
					return;
				}
			}
			if (st.harvest) {
				if (!harvestRessource(perso, map, st)) goToNextMap(perso, map, st);
				else return;
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	public void goToNextMap(BotPerso perso, ManchouMap map, BotState st) {
		if (st.path.isEmpty()) {
			new PathEndEvent(perso, st).send();
			return;
		}
		if (st.needToGo == null) {
			List<Point> list = st.path.stream().sorted((p1, p2) -> ComparatorUtil.comparingDistanceToPlayer(false, new Point(map.getX(), map.getY()), p1, p2)).collect(Collectors.toList());
			st.needToGo = list.remove(0);
		}
		Point next = st.needToGo;
		List<Point> path = Pathfinding.getMapPath(map.getX(), map.getY(), next.x, next.y, Roads::canMove);
		if (path == null) throw new NullPointerException("Unable to find path from [" + map.getX() + "," + map.getY() + "] to [" + next.x + "," + next.y + "]");
		Point point = path.get(1);
		Orientation dir = Pathfinding.getDirectionForMap(map.getX(), map.getY(), point.x, point.y);
		if (dir == null) throw new NullPointerException("Unable to find a direction to move from [" + map.getX() + "," + map.getY() + "] to [" + point.x + "," + point.y + "]");
		Pair<Long, ManchouCell> timeToTravel = perso.changeMapWithDirection(dir);
		if (timeToTravel.getFirst() == -1L) timeToTravel = perso.changeMap();
		if (timeToTravel.getFirst() == -1L) Executors.SCHEDULED.schedule(() -> goToNextMap(perso, map, st), 1, TimeUnit.MINUTES);// Retest des que les mobs bougent ! TODO
		ManchouCell celltogo = timeToTravel.getSecond();
		Executors.SCHEDULED.schedule(() -> {
			ManchouMap mm = perso.getPerso().getMap();
			if (mm.isOnCoords(point.x, point.y)) {
				System.out.println("IMPOSSIBLE D'USE CE TP, ON NOTE ET ON RETENTE");
				Roads.notifyCantUse(mm, celltogo.getId());
				goToNextMap(perso, map, st);
			}
		} , (long) (timeToTravel.getFirst() * 2), TimeUnit.MILLISECONDS);
	}

	private boolean harvestRessource(BotPerso perso, ManchouMap map, BotState st) { // return false si plus de ressource
		ManchouJob job = perso.getPerso().getJob();
		ManchouCell near = null;
		int persocell = perso.getPerso().getCellId();
		ManchouCell persocellc = map.getCells()[persocell];
		int x = persocellc.getX();
		int y = persocellc.getY();
		for (ManchouCell c : perso.getPerso().getMap().getCells()) {
			if (!c.isInterractable()) continue;
			Interractable i = c.getInterractable();
			if (c.isRessource() && st.ressources.contains(i)) {
				Skills skill = InterractUtil.getSkillFor(i, job.getType());
				if (skill == null || skill.getMinLvlToUse() > job.getLvl()) continue;
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
					st.currentRessource = c.getId();
					Executors.SCHEDULED.schedule(() -> perso.getPerso().interract(skill, c.getId()), (long) time, TimeUnit.MILLISECONDS);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @return the instance
	 */
	public static MapListener getInstance() {
		return instance;
	}

}
