package fr.aresrpg.eratz.domain.listener;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.event.*;
import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.util.*;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.gui.MapView;
import fr.aresrpg.tofumanchou.domain.data.Account;
import fr.aresrpg.tofumanchou.domain.data.map.Carte;
import fr.aresrpg.tofumanchou.domain.event.BotStartMoveEvent;
import fr.aresrpg.tofumanchou.domain.event.entity.*;
import fr.aresrpg.tofumanchou.domain.event.player.MapJoinEvent;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

import java.util.*;
import java.util.stream.Collectors;

import javafx.scene.paint.Color;

/**
 * 
 * @since
 */
public class MapViewListener implements Listener {

	private static MapViewListener instance = new MapViewListener();
	private static List<Pair<EventBus, Subscriber>> subs = new ArrayList<>();

	private MapViewListener() {
	}

	public static void register() {
		try {
			subs = Events.register(getInstance());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void unRegister() {
		subs.forEach(p -> p.getFirst().unsubscribe(p.getSecond()));
	}

	/**
	 * @return the instance
	 */
	public static MapViewListener getInstance() {
		return instance;
	}

	private BotPerso get(Account client) {
		return BotFather.getPerso(client);
	}

	@Subscribe
	public void onBotMove(BotStartMoveEvent e) {
		Carte map = e.getClient().getPerso().getMap();
		get(e.getClient()).getView()
				.setPath(e.getPath().stream()
						.map(frag -> new Node(Maps.getXRotated(frag.getCellId(), map.getWidth(), map.getHeight()), Maps.getYRotated(frag.getCellId(), map.getWidth(), map.getHeight())))
						.collect(Collectors.toList()), Color.DARKTURQUOISE);
	}

	@Subscribe
	public void onJoinMap(MapJoinEvent e) {
		Executors.FIXED.execute(() -> {
			BotPerso botPerso = get(e.getClient());
			if (botPerso == null || botPerso.getView() == null) return;
			botPerso.getView().clearActors();
			botPerso.getView().clearPath();
			MapView.getInstance().setTitle(botPerso.getPerso().getMap().getInfos());
			botPerso.getView().setMap(((ManchouMap) e.getMap()).serialize());
			botPerso.getView().setCurrentPosition(botPerso.getPerso().getCellId());
			botPerso.getView().setOnCellClick(i -> {
				LOGGER.debug("Cell cliked = " + i);
				LOGGER.debug("Movement = " + botPerso.getPerso().getMap().getCells()[i].getMovement());
				LOGGER.debug("Object1num = " + botPerso.getPerso().getMap().getCells()[i].getLayerObject1Num());
				LOGGER.debug("Object2num = " + botPerso.getPerso().getMap().getCells()[i].getLayerObject2Num());
				LOGGER.debug("Cell= " + botPerso.getPerso().getMap().getCells()[i]);
				Set<Cell> accesibleCells = ShadowCasting.getAccesibleCells(i, 10, botPerso.getPerso().getMap().serialize(), botPerso.getPerso().getMap().cellAccessible().negate());
				Iterator<Cell> iterator = accesibleCells.iterator();
				while (iterator.hasNext())
					if (!iterator.next().isWalkeable()) iterator.remove();
				System.out.println(accesibleCells.stream().map(Cell::getId).collect(Collectors.toList()));
				botPerso.getView().setAccessible(accesibleCells.stream().collect(Collectors.toList()), i, 10);
				List<Node> cellPath = Pathfinding.getCellPath(botPerso.getPerso().getCellId(), i, botPerso.getPerso().getMap().getProtocolCells(), e.getMap().getWidth(), e.getMap().getHeight(),
						Pathfinding::getNeighbors,
						botPerso.getPerso()::canGoOnCellAvoidingMobs);
				if (cellPath == null) LOGGER.debug("Path not found !");
				else botPerso.getView().setPath(cellPath, Color.DARKRED);
			});
		});
	}

	@Subscribe
	public void onPlayerAdd(EntityPlayerJoinMapEvent e) {
		BotPerso botPerso = get(e.getClient());
		if (botPerso == null) return;
		Executors.FIXED.execute(() -> botPerso.getView().addEntity((int) e.getPlayer().getUUID(), e.getPlayer().getCellId()));
	}

	@Subscribe
	public void onMobAdd(MonsterGroupSpawnEvent e) {
		BotPerso botPerso = get(e.getClient());
		if (botPerso == null) return;
		Executors.FIXED.execute(() -> botPerso.getView().addMob((int) e.getGroup().getUUID(), e.getGroup().getCellId()));
	}

	@Subscribe
	public void onMobAdd(MonsterJoinMapEvent e) {
		BotPerso botPerso = get(e.getClient());
		if (botPerso == null) return;
		Executors.FIXED.execute(() -> botPerso.getView().addMob((int) e.getMob().getUUID(), e.getMob().getCellId()));
	}

	@Subscribe
	public void onNpcAdd(NpcJoinMapEvent e) {
		BotPerso botPerso = get(e.getClient());
		if (botPerso == null) return;
		Executors.FIXED.execute(() -> botPerso.getView().addNpc((int) e.getNpc().getUUID(), e.getNpc().getCellId()));
	}

	@Subscribe
	public void onMove(EntityMoveEvent e) {
		BotPerso botPerso = get(e.getClient());
		ManchouMap map = botPerso.getPerso().getMap();
		botPerso.getView().addEntity((int) e.getEntity().getUUID(), e.getEntity().getCellId());
		if (e.getEntity().getUUID() == botPerso.getPerso().getUUID()) botPerso.getView().setPath(e.getPath().stream()
				.map(frag -> new Node(Maps.getXRotated(frag.getCellId(), map.getWidth(), map.getHeight()), Maps.getYRotated(frag.getCellId(), map.getWidth(), map.getHeight())))
				.collect(Collectors.toList()), Color.DODGERBLUE);
	}

}
