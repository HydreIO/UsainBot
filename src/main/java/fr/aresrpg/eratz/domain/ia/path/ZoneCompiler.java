package fr.aresrpg.eratz.domain.ia.path;

import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.tofumanchou.domain.util.PercentPrinter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 
 * @since
 */
public class ZoneCompiler {

	private static ZoneCompiler instance = new ZoneCompiler();
	private Map<Paths, BotMap[]> cache = new HashMap<>();

	private ZoneCompiler() {
	}

	public static BotMap[] compilePath(Paths path, Predicate<BotMap> validator) {
		BotMap[] maps = instance.cache.get(path);
		if (maps == null) {
			PercentPrinter p = new PercentPrinter(MapsManager.getMaps().size());
			instance.cache.put(path, maps = MapsManager.getMaps().values().stream().filter(map -> {
				p.incrementAndPrint("Compiling path (:" + path + ") " + PercentPrinter.FORMAT);
				return validator.test(map);
			}).toArray(BotMap[]::new));
		}
		return maps;
	}

}
