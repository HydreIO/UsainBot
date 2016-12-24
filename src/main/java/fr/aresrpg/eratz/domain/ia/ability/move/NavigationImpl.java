package fr.aresrpg.eratz.domain.ia.ability.move;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.dofus.protocol.game.actions.GameActions;
import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction;
import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction.PathFragment;
import fr.aresrpg.dofus.protocol.game.client.GameActionACKPacket;
import fr.aresrpg.dofus.protocol.game.client.GameClientActionPacket;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.util.Maps;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.eratz.domain.data.dofus.map.Zaap;
import fr.aresrpg.eratz.domain.data.dofus.map.Zaapi;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.util.BotThread;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

import java.awt.Point;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @since
 */
public class NavigationImpl implements Navigation {

	private Perso perso;
	private BotThread botThread = new BotThread();
	private boolean teleporting;

	public NavigationImpl(Perso perso) {
		this.perso = perso;
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
		return Pathfinding.getPath(
				Maps.getX(getCurrentPos(), getMap().getWidth()),
				Maps.getY(getCurrentPos(), getMap().getWidth()),
				Maps.getX(cellid, getMap().getWidth()),
				Maps.getY(cellid, getMap().getWidth()), getMap().getCells(), getMap().getWidth(), true);
	}

	@Override
	public Navigation moveToCell(int cellid, boolean teleport) {
		List<Point> p = searchPath(cellid);
		if (p == null) {
			LOGGER.info("Le chemin est introuvable !");
			LOGGER.info("Position = " + getCurrentPos());
			getPerso().getBotInfos().setBlockedOn(getCurrentPos());
			return this;
		}
		getPerso().getBotInfos().setBlockedOn(-1);
		float time = Pathfinding.getPathTime(p, getMap().getCells(), getMap().getWidth(), false);
		getPerso().getDebugView().setPath(p);
		teleporting = teleport;
		try {
			List<PathFragment> shortpath = Pathfinding.makeShortPath(p, getMap().getWidth());
			getPerso().getAccount().getRemoteConnection().send(new GameClientActionPacket(GameActions.MOVE, new GameMoveAction().setPath(shortpath)));
			Executors.SCHEDULED.schedule(() -> {
				if (getPerso().getAccount().isBotOnline()) getPerso().sendPacketToServer(new GameActionACKPacket().setActionId(0)); // pour eviter de se faire ban en MITM on envoi rien si c pas le bot seul
				if (!teleporting)
					getBotThread().unpause();
			} , (long) (time * 60), TimeUnit.MILLISECONDS);
			getBotThread().pause(Thread.currentThread());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
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
				if (l.getMovement() == movement || l.getLayerObject1Num() == object1num)
					return id;
			}
		return -1;
	}
}
