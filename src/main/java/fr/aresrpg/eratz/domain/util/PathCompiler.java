package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.util.exception.MapNotDiscoveredException;
import fr.aresrpg.eratz.domain.util.functionnal.*;

import java.awt.Point;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntPredicate;

/**
 * 
 * @since
 */
public class PathCompiler {

	/**
	 * Attempt to find a path to reach the destination map
	 * 
	 * @param originCell
	 *            the player current cell (or starting cell)
	 * @param originMap
	 *            the starting map id
	 * @param destMap
	 *            the destination map id
	 * @param knowZaap
	 *            a predicate to know if the player know a zaap
	 * @throws MapNotDiscoveredException
	 *             when the origin or dest map is unknown for the system
	 * @throws NullPointerException
	 *             when the path is not found
	 */
	public static List<Point> compilPath(int originCell, int originMap, int destMap, IntPredicate knowZaap) throws MapNotDiscoveredException, NullPointerException {
		BotMap origin = MapsManager.getMap(originMap);
		BotMap dest = MapsManager.getMap(destMap);
		if (origin == null) throw new MapNotDiscoveredException(originMap);
		if (dest == null) throw new MapNotDiscoveredException(destMap);
		BotNode firstNode = new BotNode(origin.getMap().getX(), origin.getMap().getY(), originMap, originCell);
		int xto = dest.getMap().getX();
		int yto = dest.getMap().getY();
		Function<BotNode, BotNode[]> neighbors = Neighbors.findMapNeighbors(IntMapSupplier.defaultSupplier(), NodeMapSupplier.defaultSupplier(), knowZaap, CellPathPredicate.defaultPredicate());
		List<Point> path = Pathfinding.getPath(firstNode, xto, yto, Validators.insideDofusMap(), neighbors);
		Objects.requireNonNull(path, "The map is unreachable ! You need to discover a path at least one time before using it automaticaly");
		return path;
	}

}
