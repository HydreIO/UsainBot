package fr.aresrpg.eratz.domain.data.player.info;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.dofus.structures.item.DofusItems;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.util.BotNode;
import fr.aresrpg.eratz.domain.util.PathCompiler;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger;
import fr.aresrpg.tofumanchou.domain.data.enums.*;
import fr.aresrpg.tofumanchou.domain.util.BenchTime;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

import java.util.*;
import java.util.function.BiPredicate;

/**
 * 
 * @since
 */
public class MapUtilities extends Info {

	/**
	 * @param perso
	 */
	public MapUtilities(BotPerso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {
	}

	public Queue<TeleporterTrigger> searchPath(BotMap destination) {
		LOGGER.success("Compiling path..");
		BenchTime t = new BenchTime();
		Queue<TeleporterTrigger> path = new LinkedList<>();
		ManchouMap mMap = getPerso().getPerso().getMap();
		BotMap current = MapsManager.getOrCreateMap(mMap);
		BiPredicate<PotionType, BotNode> usePopo = (type, node) -> {
			if (node.getId() != current.getMapId()) return false;
			switch (type) {
				case RAPPEL:
					return getPerso().getUtilities().getQuantityInInventoryOf(DofusItems.POTION_DE_RAPPEL) > 0;
				case BONTA:
					return getPerso().getUtilities().getQuantityInInventoryOf(DofusItems.POTION_DE_CITE_BONTA) > 0;
				case BRAKMAR:
					return getPerso().getUtilities().getQuantityInInventoryOf(DofusItems.POTION_DE_CITE_BRAKMAR) > 0;
				default:
					return false;
			}
		};
		if (!current.equals(destination)) {
			List<TeleporterTrigger> compilPath = PathCompiler.compilPath(getPerso().getPerso().getCellId(), mMap.getMapId(), destination.getMapId(),
					i -> getPerso().getPerso().getZaaps().contains(Zaap.getWithMap(i)), usePopo);
			compilPath.remove(0);
			compilPath.forEach(path::add);
		}
		LOGGER.success("Path found ! [" + t.getAsLong() + "ms] | " + path);
		return path;
	}

}
