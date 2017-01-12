package fr.aresrpg.eratz.domain.ia.navigation;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.dofus.util.Pathfinding.PathValidator;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.util.*;
import fr.aresrpg.eratz.domain.util.exception.MobOnPathException;
import fr.aresrpg.eratz.domain.util.exception.PathNotFoundException;
import fr.aresrpg.eratz.domain.util.functionnal.PathContext;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger;
import fr.aresrpg.tofumanchou.domain.data.enums.*;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;

/**
 * 
 * @since
 */
public class Navigator extends Info {

	private BotMap destination;

	private Queue<TeleporterTrigger> path = new LinkedList<>();

	/**
	 * @param perso
	 * @param destination
	 */
	public Navigator(BotPerso perso, BotMap destination) {
		super(perso);
		Objects.requireNonNull(destination);
		this.destination = destination;
	}

	public Navigator himSelf() {
		return this;
	}

	/**
	 * Search the path
	 */
	public Navigator compilePath() {
		path.clear();
		LOGGER.success("CALCUL DU PATH");
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
					i -> getPerso().getUtilities().hasZaap(Zaap.getWithMap(i)), usePopo);
			compilPath.remove(0);
			compilPath.forEach(path::add);
		}
		LOGGER.success("PATH CALCULÉ " + path);
		return this;
	}

	/**
	 * @return true if the perso has reached the destination
	 */
	public boolean isFinished() {
		return path.isEmpty();
	}

	public Navigator notifyMoved() {
		path.poll();
		return this;
	}

	public Navigator runToNext() {
		TeleporterTrigger next = path.peek();
		ManchouMap map = getPerso().getPerso().getMap();
		LOGGER.success("RUN TO NEXT " + next);
		switch (next.getTeleportType()) {
			case ZAAP:
				return runZaap(next, map);
			case ZAAPI:
				return runZaapi(next, map);
			case MAP_TP:
				return runMap(next, map);
			case POTION_ASTRUB:
				return runPopoAstrub(next, map);
			case POTION_BONTA:
				return runPopoBonta(next, map);
			case POTION_BRAK:
				return runPopoBrakmar(next, map);
			default:
				throw new IllegalArgumentException("The teleportType '" + next.getTeleportType() + "' is not handled !");
		}
	}

	private Navigator runPopoBonta(TeleporterTrigger next, ManchouMap map) {
		Threads.uSleep(3, TimeUnit.SECONDS);
		getPerso().getUtilities().setNextMapId(next.getDest().getMapId());
		getPerso().getPerso().useAllItemsWithType(DofusItems.POTION_DE_CITE_BONTA);
		return this;
	}

	private Navigator runPopoAstrub(TeleporterTrigger next, ManchouMap map) {
		Threads.uSleep(3, TimeUnit.SECONDS);
		getPerso().getUtilities().setNextMapId(next.getDest().getMapId());
		getPerso().getPerso().useAllItemsWithType(DofusItems.POTION_DE_RAPPEL);
		return this;
	}

	private Navigator runPopoBrakmar(TeleporterTrigger next, ManchouMap map) {
		Threads.uSleep(3, TimeUnit.SECONDS);
		getPerso().getUtilities().setNextMapId(next.getDest().getMapId());
		getPerso().getPerso().useAllItemsWithType(DofusItems.POTION_DE_CITE_BRAKMAR);
		return this;
	}

	private Navigator runMap(TeleporterTrigger next, ManchouMap map) {
		List<Node> template = Pathfinding.getCellPath(getPerso().getPerso().getCellId(), next.getCellId(), map.getProtocolCells(), map.getWidth(), map.getHeight(), Pathfinding::getNeighbors,
				PathValidator.alwaysTrue());
		List<Node> real = Pathfinding.getCellPath(getPerso().getPerso().getCellId(), next.getCellId(), map.getProtocolCells(), map.getWidth(), map.getHeight(), Pathfinding::getNeighbors,
				Validators.avoidingMobs(map));
		if (template == null) throw new PathNotFoundException(PathContext.empty());
		if (real == null) throw new MobOnPathException();
		getPerso().getUtilities().setNextMapId(next.getDest().getMapId());
		getPerso().getPerso().move(real, true);
		return this;
	}

	private Navigator runZaap(TeleporterTrigger next, ManchouMap map) {
		List<Node> template = Pathfinding.getCellPath(getPerso().getPerso().getCellId(), next.getCellId(), map.getProtocolCells(), map.getWidth(), map.getHeight(), Pathfinding::getNeighbors,
				PathValidator.alwaysTrue());
		List<Node> real = Pathfinding.getCellPath(getPerso().getPerso().getCellId(), next.getCellId(), map.getProtocolCells(), map.getWidth(), map.getHeight(), Pathfinding::getNeighbors,
				Validators.avoidingMobs(map));
		if (template == null) throw new PathNotFoundException(PathContext.empty());
		if (real == null) throw new MobOnPathException();
		getPerso().getUtilities().setNextMapId(next.getDest().getMapId());
		Executors.SCHEDULED.schedule(() -> getPerso().getPerso().useZaap(next.getCellId(), Zaap.getWithMap(next.getDest().getMapId())), getPerso().getPerso().move(real, true), TimeUnit.MILLISECONDS);
		return this;
	}

	private Navigator runZaapi(TeleporterTrigger next, ManchouMap map) {
		List<Node> template = Pathfinding.getCellPath(getPerso().getPerso().getCellId(), next.getCellId(), map.getProtocolCells(), map.getWidth(), map.getHeight(), Pathfinding::getNeighbors,
				PathValidator.alwaysTrue());
		List<Node> real = Pathfinding.getCellPath(getPerso().getPerso().getCellId(), next.getCellId(), map.getProtocolCells(), map.getWidth(), map.getHeight(), Pathfinding::getNeighbors,
				Validators.avoidingMobs(map));
		if (template == null) throw new PathNotFoundException(PathContext.empty());
		if (real == null) throw new MobOnPathException();
		getPerso().getUtilities().setNextMapId(next.getDest().getMapId());
		Executors.SCHEDULED.schedule(() -> getPerso().getPerso().useZaapi(next.getCellId(), Zaapi.getWithMap(next.getDest().getMapId())), getPerso().getPerso().move(real, true),
				TimeUnit.MILLISECONDS);
		return this;
	}

	@Override
	public void shutdown() {
	}

}
