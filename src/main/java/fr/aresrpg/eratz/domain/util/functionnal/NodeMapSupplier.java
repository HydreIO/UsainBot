package fr.aresrpg.eratz.domain.util.functionnal;

import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.util.BotNode;

@FunctionalInterface
public interface NodeMapSupplier {
	BotMap get(BotNode node);

	public static NodeMapSupplier defaultSupplier() {
		return node -> MapsManager.getMap(node.getX(), node.getY());
	}
}