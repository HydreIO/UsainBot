package fr.aresrpg.eratz.domain.util.functionnal;

import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.dofus.util.Pathfinding.PathValidator;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

import java.util.function.Function;

@FunctionalInterface
public interface CellPathFinder {
	boolean pathExist(int originCell, int destCell, ManchouMap map);

	/**
	 * @return a valid predicate if a valid path exist on a map between to cellId
	 */
	public static <T extends Node> CellPathFinder defaultFinder(Function<T, T[]> neighbors) {
		return (originCell, destCell,
			map) -> Pathfinding.getCellPath(originCell, destCell, map.getProtocolCells(), map.getWidth(), map.getHeight(), neighbors, PathValidator.alwaysTrue()) != null;
	}

}