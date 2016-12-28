package fr.aresrpg.eratz.domain.ia.ability.move;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.protocol.game.actions.GameActions;
import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction;
import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction.PathFragment;
import fr.aresrpg.dofus.protocol.game.client.GameActionACKPacket;
import fr.aresrpg.dofus.protocol.game.client.GameClientActionPacket;
import fr.aresrpg.dofus.protocol.game.movement.MovementMonsterGroup;
import fr.aresrpg.dofus.structures.PathDirection;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.util.Maps;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.data.Roads;
import fr.aresrpg.eratz.domain.data.Roads.MapRestriction;
import fr.aresrpg.eratz.domain.data.dofus.map.*;
import fr.aresrpg.eratz.domain.data.dofus.mob.AgressiveMobs;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.event.BotMoveEvent;
import fr.aresrpg.eratz.domain.util.BotThread;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

import java.awt.Point;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 *
 * @since
 */
public class NavigationImpl implements Navigation {

	private Perso perso;
	private BotThread botThread = new BotThread();
	private boolean teleporting;
	private boolean moving;
	private ScheduledFuture sch;
	private int trymove = 0;

	public NavigationImpl(Perso perso) {
		this.perso = perso;
	}

	private void init() {
		if (sch != null) return;
		sch = Executors.SCHEDULER.register(this::unblockBot, 5, TimeUnit.SECONDS);
	}

	@Override
	public void shutdown() {
		sch.cancel(true);
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	public DofusMap getMap() {
		return getPerso().getMapInfos().getMap().getDofusMap();
	}

	public int getCurrentPos() {
		return getPerso().getMapInfos().getCellId();
	}

	@Override
	public Navigation moveUp() {
		return moveToCell(getTeleporters(getMap())[0], true);
	}

	@Override
	public Navigation moveDown() {
		return moveToCell(getTeleporters(getMap())[2], true);
	}

	@Override
	public Navigation moveLeft() {
		return moveToCell(getTeleporters(getMap())[1], true);
	}

	@Override
	public Navigation moveRight() {
		return moveToCell(getTeleporters(getMap())[3], true);
	}

	private List<Point> searchPath(int cellid) {
		if (cellid == -1) return null;
		int width = getPerso().getMapInfos().getMap().getDofusMap().getWidth();
		return Pathfinding.getCellPath(
				Maps.getX(getCurrentPos(), getMap().getWidth()),
				Maps.getY(getCurrentPos(), getMap().getWidth()),
				Maps.getX(cellid, getMap().getWidth()),
				Maps.getY(cellid, getMap().getWidth()), getMap().getCells(), getMap().getWidth(), true, p -> canGoOnCellAvoidingMobs(Maps.getId(p.x, p.y, width)));
	}

	private boolean canGoOnCellAvoidingMobs(int cell) {
		CopyOnWriteArraySet<MovementMonsterGroup> mobs = getPerso().getMapInfos().getMap().getMobs();
		Cell c = getPerso().getMapInfos().getMap().getDofusMap().getCell(cell);
		if (c.isTeleporter()) return true;
		if (getPerso().getMapInfos().getMap().hasMobOn(cell)) return false;
		for (MovementMonsterGroup grp : mobs) {
			for (int type : grp.getEntitytype()) {
				int distanceAgro = AgressiveMobs.getDistanceAgro(type);
				if (distanceAgro == -1) continue;
				distanceAgro++; // increment car la distance manathan par de 0
				if (Maps.distanceManathan(grp.getCellId(), cell, getPerso().getMapInfos().getMap().getDofusMap().getWidth()) <= distanceAgro) return false;
			}
		}
		return true;
	}

	@Override
	public Navigation moveToCell(int cellid, boolean teleport) {
		return moveToCell(searchPath(cellid), cellid, teleport);
	}

	@Override
	public Navigation moveToCell(List<Point> p, int cellid, boolean teleport) {
		init();
		moving = true;
		if (teleport) Threads.uSleep(500, TimeUnit.MILLISECONDS); // antiban
		if (cellid == -1 || p == null) {
			LOGGER.info("Le chemin est introuvable !");
			LOGGER.info("Position = " + getCurrentPos());
			getPerso().getBotInfos().setBlockedOn(getCurrentPos());
			moving = false;
			return this;
		}
		getPerso().getBotInfos().setBlockedOn(-1);
		float time = Pathfinding.getPathTime(p, getMap().getCells(), getMap().getWidth(), false);
		getPerso().getDebugView().setPath(p);
		teleporting = teleport;
		try {
			List<PathFragment> shortpath = Pathfinding.makeShortPath(p, getMap().getWidth());
			if (shortpath == null) {
				getPerso().getBotInfos().setBlockedOn(getCurrentPos());
				moving = false;
				return this;
			}
			getPerso().getAccount().getRemoteConnection().send(new GameClientActionPacket(GameActions.MOVE, new GameMoveAction().setPath(shortpath)));
			new BotMoveEvent(getPerso().getId(), cellid, teleport).send();
			Executors.SCHEDULED.schedule(() -> {
				if (getPerso().getAccount().isBotOnline()) getPerso().sendPacketToServer(new GameActionACKPacket().setActionId(0)); // pour eviter de se faire ban en MITM on envoi rien si c pas le bot seul
				if (!teleporting)
					getBotThread().unpause();
			} , (long) (time * 30), TimeUnit.MILLISECONDS);
			getBotThread().pause();
		} catch (IOException e) {
			e.printStackTrace();
		}
		moving = false;
		return this;
	}

	public Cell findRandomCellExept(List<Cell> cells) {
		for (Cell cell : getPerso().getMapInfos().getMap().getDofusMap().getCells())
			if (!cells.contains(cell) && cell.getMovement() == 4) return cell;
		return null;
	}

	@Override
	public void moveToRandomCell() {
		Cell c = null;
		List<Cell> cells = new ArrayList<>();
		List<Point> path = null;
		int security = 0;
		do {
			c = findRandomCellExept(cells);
			if (c != null)
				path = searchPath(c.getId());
			if (++security > 300) break;
		} while (c == null || path == null);
		if (c == null || path == null) {
			LOGGER.severe("Impossible de bouger sur une cell random ! | aucune cell trouvée");
			return;
		}
		moveToCell(path, c.getId(), false);
	}

	public void moveToRandomNeightbourMap() {
		int nextInt = Randoms.nextInt(40);
		if (nextInt > 30) moveUp();
		else if (nextInt > 20) moveDown();
		else if (nextInt > 10) moveLeft();
		else moveRight();
	}

	@Override
	public Navigation takeZaap(Zaap destination) {
		return null;
	}

	@Override
	public Navigation takeZaapi(Zaapi destination) {
		return null;
	}

	@Override
	public void notifyMovementEnd() {
		getBotThread().unpause();
	}

	/**
	 * @return the botThread
	 */
	public BotThread getBotThread() {
		return botThread;
	}

	public boolean isTeleporting() {
		return teleporting;
	}

	public static int[] getTeleporters(DofusMap map) {
		int[] t = new int[4];
		Arrays.fill(t, -1);
		for (int i = 0; i < map.getHeight() * 2; i++) {
			int dHi = ((map.getHeight() - 1) * 2) - i;
			int dWi = (map.getWidth() * 2 - 1) - i;
			if (t[0] == -1)
				t[0] = getCaseWithMovement(map.getCells(), 2, 1030, 1030, i, dWi, i, i, map.getWidth());
			if (t[1] == -1)
				t[1] = getCaseWithMovement(map.getCells(), 2, 1030, 1030, i, i, i, dHi, map.getWidth());
			if (t[2] == -1)
				t[2] = getCaseWithMovement(map.getCells(), 2, 1030, 1030, i, dWi, dHi, dHi, map.getWidth());
			if (t[3] == -1)
				t[3] = getCaseWithMovement(map.getCells(), 2, 1030, 1030, dWi, dWi, i, dHi, map.getWidth());
		}
		return t;
	}

	public static int getCaseWithMovement(Cell[] cells, int movement, int xFrom, int xTo, int yFrom, int yTo, int width) {
		for (int x = xFrom; x <= xTo; x++)
			for (int y = yFrom; y <= yTo; y++) {
				if (cells[Maps.getId(x, y, width)].getMovement() == movement)
					return Maps.getId(x, y, width);
			}
		return -1;
	}

	public static int getCaseWithMovement(Cell[] cells, int movement, int object1num, int object2num, int xFrom, int xTo, int yFrom, int yTo, int width) {
		for (int x = xFrom; x <= xTo; x++)
			for (int y = yFrom; y <= yTo; y++) {
				int id = Maps.getId(x, y, width);
				Cell l = cells[id];
				if (l.getMovement() == movement || l.getLayerObject1Num() == object1num || l.getLayerObject2Num() == object2num)
					return id;
			}
		return -1;
	}

	public static int getFareastTeleporter(Cell[] cells) {
		Cell far = null;
		int dist = 0;
		for (Cell c : cells) {
			if (!c.isTeleporter()) continue;
			if (far == null) far = c;
			int di = c.distance(far);
			if (di > dist) {
				far = c;
				dist = di;
			}
		}
		return far == null ? -1 : far.getId();
	}

	@Override
	public void joinCoords(int x, int y) {
		BotMap map = perso.getMapInfos().getMap();
		Node coords = new Node(x, y);
		List<Point> path = Pathfinding.getMapPath(map.getX(), map.getY(), coords.getX(), coords.getY(), Roads::canMove);
		LOGGER.debug("path =" + path);
		if (path == null) {
			trymove++;
			LOGGER.severe("Impossible de rejoindre la pos " + coords + " ! Blocké en [" + map.getX() + "," + map.getY() + "]");
			if (trymove > 4) {
				LOGGER.severe("Le bot n'a pas réussi à trouver son chemin, abandon.");
				return;
			}
			Roads.resetRestrictions();
			LOGGER.severe("Nouvel éssai dans 5s..");
			Threads.uSleep(5, TimeUnit.SECONDS);
			moveToRandomNeightbourMap();
			joinCoords(x, y);
			return;
		}
		path.remove(0);
		for (Point p : path) {
			BotMap newmap = perso.getMapInfos().getMap();
			if (newmap.isOnPoint(p)) continue;
			PathDirection dir = Pathfinding.getDirectionForMap(newmap.getX(), newmap.getY(), (int) p.getX(), (int) p.getY());
			if (dir == null) {
				LOGGER.warning("Impossible de trouver la direction pour aller de [" + newmap.getX() + "," + newmap.getY() + "] vers [" + p.x + "," + p.y + "]");
				LOGGER.warning("Recherche du téléporteur le plus éloigné !");
				int tp = getFareastTeleporter(newmap.getDofusMap().getCells());
				if (tp == -1) {
					LOGGER.severe("Impossible de trouver un téléporteur éloigné !");
					return;
				}
				LOGGER.success("Teleporteur trouvé ! l'itinéraire va être recalculé");
				getPerso().getNavigation().moveToCell(tp, true);
				if (perso.getBotInfos().isBlockedOnACell()) Roads.getRestriction(new Point(newmap.getX(), newmap.getY())).setCantMove(dir); // on verif quand si on était bloqué ou pas
				joinCoords(x, y);
				return;
			}
			moveWithDirection(perso, dir); // si le bot ce block il sera débloqué au bout de 10s
			if (perso.getBotInfos().isBlockedOnACell()) { // et isblocked sera true comme ça sa déblock le bot et enregistre la pos pour ne plus se blocker
				MapRestriction res = Roads.getRestriction(new Point(newmap.getX(), newmap.getY()));
				res.setCantMove(dir);
				joinCoords(x, y);
				return;
			}
		}
	}

	private void unblockBot() {
		if (!moving || getPerso().isInFight()) return; // on déblock uniquement si le bot était en train de bouger
		if (getPerso().getBotInfos().getLastMove() + 15000 < System.currentTimeMillis()) { // bot blocké depuis + de 10s
			LOGGER.success("Le bot était bloqué ! déblocage..");
			getPerso().getBotInfos().setBlockedOn(getPerso().getMapInfos().getCellId()); // pour dire que le bot est bloqué
			getPerso().getNavigation().notifyMovementEnd(); // pour le laisser finir les actions qui attendait la fin du movement
		}
	}

	public void moveWithDirection(Perso perso, PathDirection dir) {
		switch (dir) {
			case DOWN:
			case DOWN_LEFT:
			case DOWN_RIGHT:
				perso.getNavigation().moveDown();
				return;
			case LEFT:
				perso.getNavigation().moveLeft();
				return;
			case RIGHT:
				perso.getNavigation().moveRight();
				return;
			case UP:
			case UP_LEFT:
			case UP_RIGHT:
				perso.getNavigation().moveUp();
				return;
		}
	}
}
