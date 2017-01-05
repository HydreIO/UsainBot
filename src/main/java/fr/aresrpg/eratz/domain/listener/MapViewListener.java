package fr.aresrpg.eratz.domain.listener;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.event.Listener;
import fr.aresrpg.commons.domain.event.Subscribe;
import fr.aresrpg.dofus.util.Maps;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.gui.MapView;
import fr.aresrpg.tofumanchou.domain.Manchou;
import fr.aresrpg.tofumanchou.domain.data.Account;
import fr.aresrpg.tofumanchou.domain.data.map.Carte;
import fr.aresrpg.tofumanchou.domain.event.BotStartMoveEvent;
import fr.aresrpg.tofumanchou.domain.event.entity.*;
import fr.aresrpg.tofumanchou.domain.event.player.MapJoinEvent;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

import java.awt.Point;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.paint.Color;

/**
 * 
 * @since
 */
public class MapViewListener implements Listener {

	private static MapViewListener instance = new MapViewListener();

	private MapViewListener() {
		Manchou.registerEvent(this);
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
						.map(frag -> new Point(Maps.getXRotated(frag.getCellId(), map.getWidth(), map.getHeight()), Maps.getYRotated(frag.getCellId(), map.getWidth(), map.getHeight())))
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
				LOGGER.debug("Cell = " + botPerso.getPerso().getMap().getCells()[i]);
				List<Point> cellPath = Pathfinding.getCellPath(botPerso.getPerso().getCellId(), i, botPerso.getPerso().getMap().getProtocolCells(), e.getMap().getWidth(), e.getMap().getHeight(),
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
		botPerso.getView().addEntity((int) e.getPlayer().getUUID(), e.getPlayer().getCellId());
	}

	@Subscribe
	public void onMobAdd(MonsterGroupSpawnEvent e) {
		get(e.getClient()).getView().addMob((int) e.getGroup().getUUID(), e.getGroup().getCellId());
	}

	@Subscribe
	public void onMobAdd(MonsterJoinMapEvent e) {
		get(e.getClient()).getView().addMob((int) e.getMob().getUUID(), e.getMob().getCellId());
	}

	@Subscribe
	public void onNpcAdd(NpcJoinMapEvent e) {
		get(e.getClient()).getView().addNpc((int) e.getNpc().getUUID(), e.getNpc().getCellId());
	}

	@Subscribe
	public void onMove(EntityMoveEvent e) {
		BotPerso botPerso = get(e.getClient());
		ManchouMap map = botPerso.getPerso().getMap();
		botPerso.getView().addEntity((int) e.getEntity().getUUID(), e.getEntity().getCellId());
		if (e.getEntity().getUUID() == botPerso.getPerso().getUUID()) botPerso.getView().setPath(e.getPath().stream()
				.map(frag -> new Point(Maps.getXRotated(frag.getCellId(), map.getWidth(), map.getHeight()), Maps.getYRotated(frag.getCellId(), map.getWidth(), map.getHeight())))
				.collect(Collectors.toList()), Color.DODGERBLUE);
	}

}
