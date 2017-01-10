package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.dofus.util.Pathfinding.PathValidator;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.util.exception.MapNotDiscoveredException;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

/**
 * 
 * @since
 */
public class PathCompiler {

	public static void compilPath(int originMap, int destMap) throws MapNotDiscoveredException {
		BotMap origin = MapsManager.getMap(originMap);
		BotMap dest = MapsManager.getMap(destMap);
		if (origin == null) throw new MapNotDiscoveredException(originMap);
		if (dest == null) throw new MapNotDiscoveredException(destMap);
		
	}

	private static boolean pathExist(int originCell, int destCell, ManchouMap map) {
		return Pathfinding.getCellPath(originCell, destCell, map.getProtocolCells(), map.getWidth(), map.getHeight(), Pathfinding::getNeighbors, PathValidator.alwaysTrue()) != null;
	}

}
