package fr.aresrpg.eratz.domain.ia.path.zone.fight;

import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.path.Paths;
import fr.aresrpg.eratz.domain.ia.path.ZoneCompiler;
import fr.aresrpg.eratz.domain.ia.path.zone.Zone;
import fr.aresrpg.eratz.domain.util.UtilFunc;
import fr.aresrpg.tofumanchou.domain.data.MapsData;
import fr.aresrpg.tofumanchou.domain.data.MapsData.MapDataBean;
import fr.aresrpg.tofumanchou.domain.data.enums.DofusMobs;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public abstract class FightZone implements Zone {

	private Queue<Integer> maps = new LinkedList<>();
	private BotMap[] allMaps;
	private final Supplier<BotPerso> position;

	public FightZone(Supplier<BotPerso> playerPosition) {
		this.position = playerPosition;
		compile();
		refill();
	}

	@Override
	public int getNextMap() {
		if (allMaps.length == 1) return allMaps[0].getMapId();
		if (maps.isEmpty()) refill();
		return maps.poll();
	}

	protected boolean hasArea(BotMap map, int... ids) {
		MapDataBean data = MapsData.getData(map.getMapId());
		return ArrayUtils.contains(data.getAreaId(), ids);
	}

	protected boolean hasSubArea(BotMap map, int... ids) {
		MapDataBean data = MapsData.getData(map.getMapId());
		return ArrayUtils.contains(data.getSubareaId(), ids);
	}

	private void refill() {
		Arrays.stream(allMaps).sorted(UtilFunc.distanceToPlayer(position)).mapToInt(BotMap::getMapId).forEach(maps::add);
	}

	public void sort() {
		List<Integer> collect = maps.stream().map(MapsManager::getMap).sorted(UtilFunc.distanceToPlayer(position)).map(BotMap::getMapId).collect(Collectors.toList());
		maps.clear();
		maps.addAll(collect);
	}

	private void compile() {
		this.allMaps = ZoneCompiler.compilePath(getType(), this::isValid);
	}

	protected abstract boolean isValid(BotMap map);

	public abstract boolean avoid(DofusMobs mob);

	protected abstract Paths getType();

}
