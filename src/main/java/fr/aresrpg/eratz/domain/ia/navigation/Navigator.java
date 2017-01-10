package fr.aresrpg.eratz.domain.ia.navigation;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.event.*;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.Interrupt;
import fr.aresrpg.eratz.domain.util.PathCompiler;
import fr.aresrpg.eratz.domain.util.Validators;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger;
import fr.aresrpg.tofumanchou.domain.data.enums.Zaap;
import fr.aresrpg.tofumanchou.domain.event.entity.EntityPlayerJoinMapEvent;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 
 * @since
 */
public class Navigator implements Supplier<Interrupt>, Listener {

	private BotPerso perso;
	private BotMap destination;
	private boolean end;

	private Subscriber<EntityPlayerJoinMapEvent> subscribe;

	private Queue<TeleporterTrigger> path = new LinkedList<>();
	private Interrupt inter = Interrupt.PATH_END;

	/**
	 * @param perso
	 * @param destination
	 */
	public Navigator(BotPerso perso, BotMap destination) {
		Objects.requireNonNull(destination);
		Objects.requireNonNull(perso);
		this.perso = perso;
		this.destination = destination;
		ManchouMap mMap = perso.getPerso().getMap();
		BotMap current = MapsManager.getOrCreateMap(mMap);
		if (current.equals(destination)) end = true;
		else {
			subscribe = EventBus.getBus(EntityPlayerJoinMapEvent.class).subscribe(this::onMap, 0);
			List<TeleporterTrigger> compilPath = PathCompiler.compilPath(perso.getPerso().getCellId(), mMap.getMapId(), destination.getMapId(), i -> perso.hasZaap(Zaap.getWithMap(i)));
			compilPath.forEach(path::add);
		}
	}

	void onMap(EntityPlayerJoinMapEvent e) {
		BotPerso p = BotFather.getPerso(e.getClient());
		if (p == null || !perso.equals(p) || p.getPerso().getUUID() != e.getPlayer().getUUID()) return;
		TeleporterTrigger cur = path.poll();
		BotMap map = MapsManager.getMap(cur.getDest().getMapId());
		if (map == null || p.getPerso().getMap().getMapId() != map.getMapId()) {
			inter = Interrupt.OUT_OF_PATH;
			end = true;
			LOGGER.warning("Out of path !");
		} else if (map.equals(destination)) end = true;
		else runToNext();
	}

	void runToNext() {
		TeleporterTrigger next = path.peek();
		ManchouMap map = perso.getPerso().getMap();
		List<Node> cellPath = Pathfinding.getCellPath(perso.getPerso().getCellId(), next.getCellId(), map.getProtocolCells(), map.getWidth(), map.getHeight(), Pathfinding::getNeighbors,
				Validators.avoidingMobs(map));
		if (cellPath != null) perso.getPerso().move(cellPath, true);
		else end = true;
	}

	@Override
	public Interrupt get() {
		runToNext();
		while (!end)
			Threads.uSleep(100, TimeUnit.MILLISECONDS);
		if (subscribe != null) EventBus.getBus(EntityPlayerJoinMapEvent.class).unsubscribe(subscribe);
		return this.inter;
	}

}
