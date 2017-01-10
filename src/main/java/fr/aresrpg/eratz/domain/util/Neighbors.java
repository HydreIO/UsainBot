package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.map.Destination;
import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger;
import fr.aresrpg.tofumanchou.domain.data.enums.*;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntPredicate;
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
	 *            a predicate to know if a trigger cell is reachable (for exemple if a teleporter is in the other side of a river)
	 * @return the neighbors
	 */
	public static Function<BotNode, BotNode[]> findMapNeighbors(IntMapSupplier idToMap, NodeMapSupplier nodeToMap, IntPredicate knowZaap, IntPredicate canUseCell) {
		return n -> {
			BotMap map = nodeToMap.get(n);
			if (map == null) return new BotNode[0];
			Trigger[] triggers = map.getTriggers(TriggerType.TELEPORT);
			BotNode[] nodes = new BotNode[0];
			if (triggers != null)
				for (Trigger t : triggers) {
				if (!canUseCell.test(t.getCellId())) continue;
				TeleporterTrigger tp = (TeleporterTrigger) t;
				switch (tp.getTeleportType()) {
					case MAP_TP:
						Destination dest = tp.getDest();
						BotMap destmap = idToMap.get(dest.getMapId());
						if (destmap == null) continue;
						nodes = ArrayUtils.addLast(new BotNode(destmap.getMap().getX(), destmap.getMap().getY(), destmap.getMapId()), nodes);
						continue;
					case ZAAP:
						nodes = ArrayUtils.concat(nodes,
								IntStream.of(Zaap.getMapsIds()).filter(knowZaap).mapToObj(idToMap::get).filter(Objects::nonNull).map(BotMap::toNode).map(NodePricer.zaapPrice())
										.toArray(BotNode[]::new));
						continue;
					case ZAAPI:
						nodes = ArrayUtils.concat(nodes,
								IntStream.of(Zaapi.getMapsIds(City.getWithY(map.getMap().getY()))).filter(knowZaap).mapToObj(idToMap::get).filter(Objects::nonNull).map(BotMap::toNode)
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

	@FunctionalInterface
	static interface NodePricer {

		int getPrice(BotNode node);

		static Function<BotNode, BotNode> zaapPrice() {
			return n -> {
				n.setCost(10);
				return n;
			};
		}

		static Function<BotNode, BotNode> zaapiPrice() {
			return n -> {
				n.setCost(2);
				return n;
			};
		}
	}

	@FunctionalInterface
	static interface IntMapSupplier {
		BotMap get(int id);
	}

	@FunctionalInterface
	static interface NodeMapSupplier {
		BotMap get(BotNode node);
	}

}
