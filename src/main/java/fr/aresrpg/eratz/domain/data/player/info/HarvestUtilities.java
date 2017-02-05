package fr.aresrpg.eratz.domain.data.player.info;

import fr.aresrpg.commons.domain.condition.Option;
import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.dofus.structures.Skills;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.dofus.util.*;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.tofumanchou.domain.util.Validators;
import fr.aresrpg.tofumanchou.infra.data.ManchouCell;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

import java.util.*;

/**
 * 
 * @since
 */
public class HarvestUtilities extends Info {

	/**
	 * @param perso
	 */
	public HarvestUtilities(BotPerso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {
	}

	public Option<HarvestTarget> getAvailableRessource(boolean jobRequired, Interractable[] ressources) {
		Set<Integer> exclude = new HashSet<>();
		int cell = -1;
		Pair<Integer, Interractable> pair = null;
		while (cell == -1) {
			pair = getNearestSpawnedRessource(exclude, jobRequired, ressources);
			if (pair == null) return Option.none();
			cell = getAccessiblePath(pair.getFirst());
			if (cell == -1) exclude.add(pair.getFirst());
			else break;
		}
		Skills skill = getPerso().getUtilities().getSkillFor(pair.getSecond(), jobRequired);
		Objects.requireNonNull(skill);
		int cellid = pair.getFirst();
		getPerso().getUtilities().setCurrentHarvest(cellid);
		return Option.some(new HarvestTarget(cell, cellid, skill));
	}

	private int getAccessiblePath(int cell) {
		ManchouMap map = getPerso().getPerso().getMap();
		ManchouCell c = map.getCells()[cell];
		int cellId = getPerso().getPerso().getCellId();
		Node[] neighbors = Pathfinding.getNeighborsWithoutDiagonals(new Node(c.getX(), c.getY()));
		for (Node n : neighbors) {
			int idrot = Maps.getIdRotated(n.getX(), n.getY(), map.getWidth(), map.getHeight());
			if (idrot < 0 || idrot >= map.getCells().length) continue;
			ManchouCell manchouCell = map.getCells()[idrot];
			if (manchouCell.hasMobGroupOn() || manchouCell.isTeleporter()) continue;
			List<Node> cellPath = Pathfinding.getCellPath(cellId, manchouCell.getId(), map.getProtocolCells(), map.getWidth(), map.getHeight(),
					Pathfinding::getNeighbors, Validators.avoidingMobs(map));
			if (cellPath != null) return manchouCell.getId();
		}
		return -1;
	}

	private Pair<Integer, Interractable> getNearestSpawnedRessource(Set<Integer> exclude, boolean jobRequired, Interractable[] ressources) {
		ManchouMap map = getPerso().getPerso().getMap();
		int cellId = getPerso().getPerso().getCellId();
		int dist = Integer.MAX_VALUE;
		int near = -1;
		Interractable found = null;
		for (ManchouCell cell : map.getCells()) {
			Interractable type = cell.getInterractable();
			if (!cell.isRessourceSpawned() || type == null || !ArrayUtils.contains(type, ressources) || exclude.contains(cell.getId()))
				continue;
			if (jobRequired && !getPerso().getPerso().getJob().hasLevelToUse(getPerso().getUtilities().getSkillFor(type, jobRequired))) continue;
			if (near == -1) near = cell.getId();
			int di = cell.distance(cellId);
			if (di < dist) {
				near = cell.getId();
				dist = di;
				found = type;
			}
		}
		return near == -1 ? null : new Pair(near, found);
	}

	public static class HarvestTarget {
		public int cellToGo, cellToHarvest;
		public Skills skill;

		public HarvestTarget(int cellToGo, int cellToHarvest, Skills skill) {
			this.cellToGo = cellToGo;
			this.cellToHarvest = cellToHarvest;
			this.skill = skill;
		}

		@Override
		public String toString() {
			return "HarvestTarget [cellToGo=" + cellToGo + ", cellToHarvest=" + cellToHarvest + ", skill=" + skill + "]";
		}

	}

}
