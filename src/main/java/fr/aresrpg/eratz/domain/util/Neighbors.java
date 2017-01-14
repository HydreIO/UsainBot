package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.map.Destination;
import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;
import fr.aresrpg.eratz.domain.util.functionnal.*;
import fr.aresrpg.eratz.infra.map.DestinationImpl;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger.TeleportType;
import fr.aresrpg.tofumanchou.domain.data.enums.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.IntStream;

/**
 * 
 * @since
 */
public class Neighbors {

	/**
	 * Find all neighbors for a map (if the map contain a zaap then all known zaap's map's ids are added in the result, same for zaapi of a city)
	 * 
	 * @param idToMap
	 *            a function to get a map from a mapid
	 * @param nodeToMap
	 *            a function to get a map from a node
	 * @param knowZaap
	 *            a predicate to know if a zaap mapid is knowed by the player
	 * @param canUseCell
	 *            a CellPathPredicate which take the origin cell, the dest cell and the map as arguments to know if a trigger is reachable (for exemple if a teleporter is in the other side of a river)
	 * @param canUsePotion
	 *            a predicate to know if potions can be used from a node
	 * @return the neighbors
	 */
	public static Function<BotNode, BotNode[]> findMapNeighbors(IntMapSupplier idToMap, IntPredicate knowZaap, CellPathFinder pathPredicate, BiPredicate<PotionType, BotNode> canUsePotion) {
		return n -> {
			BotMap map = idToMap.get(n.getId());
			if (map == null) return new BotNode[0];
			Set<Trigger> triggers = map.getTriggers(TriggerType.TELEPORT);
			BotNode[] nodes = new BotNode[0];
			if (canUsePotion.test(PotionType.RAPPEL, n))
				nodes = ArrayUtils.addLast(new BotNode(4, -19, 10, new TeleporterTrigger(-1, TeleportType.POTION_ASTRUB, new DestinationImpl(7411, 311))), nodes);
			if (canUsePotion.test(PotionType.BONTA, n))
				nodes = ArrayUtils.addLast(new BotNode(-33, -57, 10, new TeleporterTrigger(-1, TeleportType.POTION_BONTA, new DestinationImpl(6159, 211))), nodes);
			if (canUsePotion.test(PotionType.BRAKMAR, n))
				nodes = ArrayUtils.addLast(new BotNode(-23, 38, 10, new TeleporterTrigger(-1, TeleportType.POTION_BRAK, new DestinationImpl(6167, 183))), nodes);
			if (triggers != null)
				for (Trigger t : triggers) {
				TeleporterTrigger tp = (TeleporterTrigger) t;
				int cellid = tp.getTeleportType() == TeleportType.MAP_TP ? t.getCellId() : map.getMap().getCells()[t.getCellId()].getRandomNeighborCell(map.getMap(), false, new ArrayList<>());
				if (cellid == -1 || !pathPredicate.pathExist(n.getTrigger().getDest().getCellId(), cellid, map.getMap())) continue;
				switch (tp.getTeleportType()) {
					case MAP_TP:
						Destination dest = tp.getDest();
						BotMap destmap = idToMap.get(dest.getMapId());
						if (destmap == null) continue;
						nodes = ArrayUtils.addLast(new BotNode(destmap.getMap().getX(), destmap.getMap().getY(), 0, tp), nodes);
						continue;
					case ZAAP:
						nodes = ArrayUtils.concat(nodes,
								IntStream.of(Zaap.getMapsIds()).filter(i -> {
									boolean know = knowZaap.test(i);
									return know;
								}).mapToObj(idToMap::get).filter(Objects::nonNull)
										.map(m -> m.toZaapNode(tp.getCellId(), m.getMapId()))
										.map(NodePricer.zaapPrice())
										.toArray(BotNode[]::new));
						continue;
					case ZAAPI:
						nodes = ArrayUtils.concat(nodes,
								IntStream.of(Zaapi.getMapsIds(City.getWithY(map.getMap().getY()))).mapToObj(idToMap::get).filter(Objects::nonNull)
										.map(m -> m.toZaapiNode(tp.getCellId(), m.getMapId()))
										.map(NodePricer.zaapiPrice())
										.toArray(BotNode[]::new));
						continue;
					default:
						break;
				}

			}
			return nodes;
		};
	}

}
