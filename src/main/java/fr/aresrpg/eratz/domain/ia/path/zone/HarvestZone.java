package fr.aresrpg.eratz.domain.ia.path.zone;

import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.path.Paths;
import fr.aresrpg.eratz.domain.ia.path.ZoneCompiler;
import fr.aresrpg.eratz.domain.util.UtilFunc;
import fr.aresrpg.tofumanchou.domain.data.MapsData;
import fr.aresrpg.tofumanchou.domain.data.MapsData.MapDataBean;
import fr.aresrpg.tofumanchou.infra.data.ManchouCell;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public abstract class HarvestZone implements Zone {

	private Queue<Integer> maps = new LinkedList<>();
	private Interractable[] ressources;
	private BotMap[] allMaps;
	private final Supplier<BotPerso> position;
	private final boolean playerJob;

	public HarvestZone(Supplier<BotPerso> playerPosition, boolean playerJob, Interractable... ressources) {
		this.ressources = ressources;
		this.position = playerPosition;
		this.playerJob = playerJob;
		compile();
		refill();
	}

	public boolean isPlayerJob() {
		return playerJob;
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

	public Interractable[] getRessources() {
		return ressources;
	}

	private void refill() {
		Arrays.stream(allMaps).filter(m -> {
			BotPerso perso = position.get();
			for (ManchouCell cell : m.getMap().getCells()) {
				Interractable interractable = cell.getInterractable();
				if (interractable == null || !ArrayUtils.contains(interractable, ressources)) continue;
				if (!playerJob) return true;
				if (perso.getPerso().getJob().hasLevelToUse(perso.getUtilities().getSkillFor(interractable, playerJob))) return true;
			}
			return false;
		}).sorted(UtilFunc.distanceToPlayer(position)).mapToInt(BotMap::getMapId).forEach(maps::add);
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

	protected abstract Paths getType();

}
