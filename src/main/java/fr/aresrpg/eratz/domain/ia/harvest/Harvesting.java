package fr.aresrpg.eratz.domain.ia.harvest;

import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.structures.Skills;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.dofus.util.*;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.util.Validators;
import fr.aresrpg.tofumanchou.domain.data.enums.Smiley;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.ManchouCell;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class Harvesting extends Info {

	private final Interractable[] ressources;

	/**
	 * @param perso
	 */
	public Harvesting(BotPerso perso, Interractable... ressources) {
		super(perso);
		this.ressources = ressources;
	}

	@Override
	public void shutdown() {

	}

	/**
	 * Harvest nearest ressource
	 * 
	 * @return false is there is no more ressource on the map, true otherwise
	 */
	public boolean harvest() {
		ManchouMap map = getPerso().getPerso().getMap();
		Set<Integer> exclude = new HashSet<>();
		int cell = -1;
		Pair<Integer, Interractable> pair = null;
		while (cell == -1) {
			pair = getNearestSpawnedRessource(exclude);
			if (pair == null) return false;
			cell = getAccessiblePath(pair.getFirst());
			if (cell == -1) exclude.add(pair.getFirst());
			else break;
		}
		Skills skill = getPerso().getUtilities().getSkillFor(pair.getSecond());
		Objects.requireNonNull(skill);
		final int cellid = pair.getFirst();
		boolean same = cell == getPerso().getPerso().getCellId();
		Executors.SCHEDULED.schedule(() -> {
			if (!map.getCells()[cellid].isRessourceSpawned()) return;
			if (Randoms.nextBool()) getPerso().getPerso().sendSmiley(Smiley.getRandomTrollSmiley());
			getPerso().getUtilities().setCurrentHarvest(cellid);
			getPerso().getPerso().interract(skill, cellid);
		}, same ? 0 : getPerso().getPerso().moveToCell(cell, true, true), TimeUnit.MILLISECONDS);
		return true;
	}

	private int getAccessiblePath(int cell) {
		ManchouMap map = getPerso().getPerso().getMap();
		ManchouCell c = map.getCells()[cell];
		int cellId = getPerso().getPerso().getCellId();
		Node[] neighbors = Pathfinding.getNeighbors(new Node(c.getX(), c.getY()));
		for (Node n : neighbors) {
			int idrot = Maps.getIdRotated(n.getX(), n.getY(), map.getWidth(), map.getHeight());
			if (idrot < 0 || idrot > map.getCells().length) continue;
			ManchouCell manchouCell = map.getCells()[idrot];
			if (manchouCell.hasMobGroupOn()) continue;
			List<Node> cellPath = Pathfinding.getCellPath(cellId, manchouCell.getId(), map.getProtocolCells(), map.getWidth(), map.getHeight(),
					Pathfinding::getNeighbors, Validators.avoidingMobs(map));
			if (cellPath != null) return manchouCell.getId();
		}
		return -1;
	}

	private Pair<Integer, Interractable> getNearestSpawnedRessource(Set<Integer> exclude) {
		ManchouMap map = getPerso().getPerso().getMap();
		int cellId = getPerso().getPerso().getCellId();
		int dist = Integer.MAX_VALUE;
		int near = -1;
		Interractable found = null;
		for (ManchouCell cell : map.getCells()) {
			Interractable type = cell.getInterractable();
			if (!cell.isRessourceSpawned() || type == null || !ArrayUtils.contains(type, ressources) || exclude.contains(cell.getId())
					|| !getPerso().getPerso().getJob().hasLevelToUse(getPerso().getUtilities().getSkillFor(type)))
				continue;
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

}
