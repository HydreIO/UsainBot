package fr.aresrpg.eratz.domain.ia.path.zone;

import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.path.Paths;
import fr.aresrpg.eratz.domain.ia.path.ZoneCompiler;
import fr.aresrpg.eratz.domain.util.UtilFunc;
import fr.aresrpg.tofumanchou.infra.data.ManchouCell;

import java.util.*;
import java.util.function.Supplier;

/**
 * 
 * @since
 */
public abstract class HarvestZone implements Zone {

	private Queue<Integer> maps = new LinkedList<>();
	private Interractable[] ressources;
	private BotMap[] allMaps;
	private final Supplier<BotPerso> position;

	public HarvestZone(Supplier<BotPerso> playerPosition, Interractable... ressources) {
		this.ressources = ressources;
		this.position = playerPosition;
		compile();
		refill();
	}

	@Override
	public int getNextMap() {
		if (maps.isEmpty()) refill();
		return maps.poll();
	}

	public Interractable[] getRessources() {
		return ressources;
	}

	private void refill() {
		Arrays.stream(allMaps).filter(m -> {
			BotPerso perso = position.get();
			for (ManchouCell cell : m.getMap().getCells()) {
				Interractable interractable = cell.getInterractable();
				if (ArrayUtils.contains(interractable, ressources) && perso.getPerso().getJob().hasLevelToUse(perso.getUtilities().getSkillFor(interractable))) return true;
			}
			return false;
		}).sorted(UtilFunc.distanceToPlayer(position)).mapToInt(BotMap::getMapId).forEach(maps::add);
	}

	private void compile() {
		this.allMaps = ZoneCompiler.compilePath(getType(), this::isValid);
	}

	protected abstract boolean isValid(BotMap map);

	protected abstract Paths getType();

}
