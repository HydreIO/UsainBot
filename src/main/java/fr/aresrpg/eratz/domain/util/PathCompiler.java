package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.util.exception.MapNotDiscoveredException;
import fr.aresrpg.eratz.domain.util.functionnal.CellPathFinder;
import fr.aresrpg.eratz.domain.util.functionnal.IntMapSupplier;
import fr.aresrpg.eratz.infra.map.DestinationImpl;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger.TeleportType;
import fr.aresrpg.tofumanchou.domain.data.enums.*;
import fr.aresrpg.tofumanchou.domain.util.Validators;

import java.util.List;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.Collectors;

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
	 * @param canUsePopo
	 *            a bipredicate to know if the player can use a popo from a node
	 * @throws MapNotDiscoveredException
	 *             when the origin or dest map is unknown for the system
	 * @throws NullPointerException
	 *             when the path is not found
	 */
	public static List<TeleporterTrigger> compilPath(int originCell, int originMap, int destMap, IntPredicate knowZaap,
		BiPredicate<PotionType, BotNode> canUsePopo) throws MapNotDiscoveredException, NullPointerException {
		BotMap origin = MapsManager.getMap(originMap);
		BotMap dest = MapsManager.getMap(destMap);
		if (origin == null) throw new MapNotDiscoveredException(originMap);
		if (dest == null) throw new MapNotDiscoveredException(destMap);
		BotNode firstNode = new BotNode(origin.getMap().getX(), origin.getMap().getY(), 0, new TeleporterTrigger(-1, TeleportType.MAP_TP, new DestinationImpl(origin.getMapId(), originCell))); // on s'en fou de la premiere car elle sera exclu du path
		int xto = dest.getMap().getX();
		int yto = dest.getMap().getY();
		CellPathFinder defaultFinder = CellPathFinder.defaultFinder(Pathfinding::getNeighbors);
		Function<BotNode, BotNode[]> neighbors = Neighbors.findMapNeighbors(IntMapSupplier.defaultSupplier(), knowZaap, defaultFinder, canUsePopo);
		List<BotNode> path = Pathfinding.getPath(firstNode, xto, yto, nd -> nd.getId() == destMap, Validators.insideDofusMap(), neighbors);
		Objects.requireNonNull(path,
				"The map is unreachable ! You need to discover a path at least one time before using it automatically\n origin:" + originCell + ", originMap:" + originMap + ", destMap:" + destMap);
		return path.stream().map(n -> {
			TeleporterTrigger trigger = n.getTrigger();
			if (trigger.getTeleportType() == TeleportType.ZAAP)
				trigger.setDest(new DestinationImpl(n.getId(), Zaap.getWithMap(n.getId()).findCellId(MapsManager.getMap(n.getId()).getMap().getCells())));
			if (trigger.getTeleportType() == TeleportType.ZAAPI)
				trigger.setDest(new DestinationImpl(n.getId(), Zaapi.getWithMap(n.getId()).findCellId(MapsManager.getMap(n.getId()).getMap().getCells())));
			return trigger;
		}).collect(Collectors.toList());
	}

}
