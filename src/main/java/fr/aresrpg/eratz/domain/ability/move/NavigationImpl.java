package fr.aresrpg.eratz.domain.ability.move;

import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction;
import fr.aresrpg.dofus.protocol.game.client.GameActionACKPacket;
import fr.aresrpg.dofus.protocol.game.client.GameActionPacket;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.util.Maps;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.eratz.domain.dofus.map.Zaap;
import fr.aresrpg.eratz.domain.dofus.map.Zaapi;
import fr.aresrpg.eratz.domain.handler.bot.BotHandler;
import fr.aresrpg.eratz.domain.util.Threads;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 *
 * @since
 */
public class NavigationImpl implements Navigation {

	private final BotHandler botHandler;
	private DofusMap map;
	private int currentPos;
	private Thread waiter;

	public NavigationImpl(BotHandler botHandler) {
		this.botHandler = botHandler;
	}

	@Override
	public Navigation moveUp() {
		return moveToCell(getTeleporters(map)[0] , true);
	}


	@Override
	public Navigation moveDown() {
		return moveToCell(getTeleporters(map)[2] , true);
	}


	@Override
	public Navigation moveLeft() {
		return moveToCell(getTeleporters(map)[1] , true);
	}


	@Override
	public Navigation moveRight() {
		return moveToCell(getTeleporters(map)[3] , true);
	}


	@Override
	public Navigation moveToCell(int cellid , boolean changeMap) {
		List<Point> p = Pathfinding.getPath(
				Maps.getColumn(currentPos , map.getWidth())
				, Maps.getLine(currentPos , map.getWidth()) ,
				Maps.getColumn(cellid , map.getWidth()) ,
				Maps.getLine(cellid , map.getWidth()) , map.getCells() , map.getWidth());
		currentPos = cellid;
		try {
			this.botHandler.getConnection()
					.send(new GameActionPacket().setId(1).setAction(new GameMoveAction()
							.setPath(Pathfinding.makeShortPath(p , map.getWidth()))));
			if(changeMap) {
				Executors.SCHEDULED.schedule(() -> {
					try {
						botHandler.getConnection().send(new GameActionACKPacket().setActionId(0));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} , 3 , TimeUnit.SECONDS);
				lockAndWait();
			}
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

	public void setMap(DofusMap map) {
		this.map = map;
		if(waiter != null) {
			Threads.sleep(3 , TimeUnit.SECONDS);
			try {
				botHandler.getConnection().send(new GameActionACKPacket().setActionId(0));
			} catch (IOException e) {
				e.printStackTrace();
			}
			LockSupport.unpark(waiter);
		}
	}

	public void setCurrentPos(int currentPos) {
		this.currentPos = currentPos;
	}

	private void lockAndWait() {
		this.waiter = Thread.currentThread();
		LockSupport.park();
	}

	public static int[] getTeleporters(DofusMap map) {
		int[] t = new int[4];
		Arrays.fill(t , -1);
		for(int i = 0 ; i < map.getHeight() * 2 ; i++) {
			int dHi = ((map.getHeight()-1) * 2) - i;
			int dWi = (map.getWidth() * 2 - 1) - i;
			if(t[0] == -1)
				t[0] = getCaseWithMovement(map.getCells() , 2 , i , dWi , i , i , map.getWidth());
			if(t[1] == -1)
				t[1] = getCaseWithMovement(map.getCells() , 2 , i , i , i , dHi , map.getWidth());
			if(t[2] == -1)
				t[2] = getCaseWithMovement(map.getCells() , 2 , i , dWi , dHi , dHi  , map.getWidth());
			if(t[3] == -1)
				t[3] = getCaseWithMovement(map.getCells() , 2 , dWi , dWi , i , dHi , map.getWidth());
		}
		return t;
	}

	public static int getCaseWithMovement(Cell[] cells, int movement, int xFrom, int xTo, int yFrom, int yTo , int width){
		for(int x = xFrom ; x <= xTo ; x++)
			for(int y = yFrom ; y <= yTo ; y++) {
				if(cells[Maps.getId(x , y ,width)].getMovement() == movement)
					return Maps.getId(x , y ,width);
			}
		return -1;
	}
}
