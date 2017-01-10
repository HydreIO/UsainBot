package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.map.Destination;
import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger;

import java.util.function.*;

/**
 * 
 * @since
 */
public class Neighbors {

	public static Function<Node, Node[]> findMapNeighbors(Predicate<Integer> knowZaap) {
		return n -> {
			BotMap map = MapsManager.getMap(n.getX(), n.getY());
			if (map == null) return new Node[0];
			Trigger[] triggers = map.getTriggers(TriggerType.TELEPORT);
			Node[] nodes = new Node[0];
			for(Trigger t : triggers) {
				TeleporterTrigger tp = (TeleporterTrigger) t;
				Destination dest = tp.getDest();
				BotMap destmap = MapsManager.getMap(dest.getMapId());
				switch (tp.getDest().g) {
					default:
						break;
				}
			}
		};
	}

}
