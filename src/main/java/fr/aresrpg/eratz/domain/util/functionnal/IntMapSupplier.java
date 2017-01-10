package fr.aresrpg.eratz.domain.util.functionnal;

import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;

@FunctionalInterface
public interface IntMapSupplier {
	BotMap get(int id);

	public static IntMapSupplier defaultSupplier() {
		return i -> MapsManager.getMap(i);
	}
}