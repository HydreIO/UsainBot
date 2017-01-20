package fr.aresrpg.eratz.domain.ia.harvest;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.event.EventBus;
import fr.aresrpg.commons.domain.event.Subscriber;
import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.structures.Skills;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.dofus.util.*;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.tofumanchou.domain.data.enums.Smiley;
import fr.aresrpg.tofumanchou.domain.event.entity.EntityMoveEvent;
import fr.aresrpg.tofumanchou.domain.event.player.PersoMoveEndEvent;
import fr.aresrpg.tofumanchou.domain.util.Validators;
import fr.aresrpg.tofumanchou.infra.data.ManchouCell;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

import java.util.*;

/**
 * 
 * @since
 */
public class Harvesting extends Info {

	private final Interractable[] ressources;
	private boolean playerJob;
	private boolean listen;
	private Subscriber subscriber;

	private ManchouMap map;
	private int cellid;
	private Skills skill;

	/**
	 * sometimes the ressource have multiple skill so just put playerJob to TRUE to filter with the bot current job
	 * 
	 * @param perso
	 * @param playerJob
	 * @param ressources
	 */
	public Harvesting(BotPerso perso, boolean playerJob, Interractable... ressources) {
		super(perso);
		this.ressources = ressources;
		this.playerJob = playerJob;
		subscriber = EventBus.getBus(PersoMoveEndEvent.class).subscribe(this::listen, 0);
	}

	@Override
	protected void finalize() throws Throwable {
		LOGGER.debug("HARVESTING FINILIZE");
		EventBus.getBus(EntityMoveEvent.class).unsubscribe(subscriber);
		super.finalize();
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
		if (!getPerso().isOnline()) return false;
		this.map = getPerso().getPerso().getMap();
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
		this.skill = getPerso().getUtilities().getSkillFor(pair.getSecond(), playerJob);
		Objects.requireNonNull(skill);
		this.cellid = pair.getFirst();
		boolean same = cell == getPerso().getPerso().getCellId();
		getPerso().getUtilities().setCurrentHarvest(cellid);
		if (same) harv();
		else {
			getPerso().getPerso().moveToCell(cell, true, true);
			listen = true;
		}
		return true;
	}

	private void listen(PersoMoveEndEvent e) {
		if (!listen || e.getPerso() != getPerso().getPerso()) return;
		listen = false;
		harv();
	}

	private void harv() {
		if (!map.getCells()[cellid].isRessourceSpawned()) return;
		if (Randoms.nextBool()) getPerso().getPerso().sendSmiley(Smiley.getRandomTrollSmiley());
		getPerso().getPerso().interract(skill, cellid);
		LOGGER.debug("harvesting " + cellid);
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
			if (manchouCell.hasMobGroupOn() || manchouCell.isTeleporter()) continue;
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
			if (!cell.isRessourceSpawned() || type == null || !ArrayUtils.contains(type, ressources) || exclude.contains(cell.getId()))
				continue;
			if (playerJob && !getPerso().getPerso().getJob().hasLevelToUse(getPerso().getUtilities().getSkillFor(type, playerJob))) continue;
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
