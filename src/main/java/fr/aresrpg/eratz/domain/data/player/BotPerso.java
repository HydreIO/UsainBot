/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.data.player;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.dofus.protocol.Packet;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.dofus.structures.Orientation;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.util.DofusMapView;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.data.Roads;
import fr.aresrpg.eratz.domain.data.player.info.ChatInfo;
import fr.aresrpg.eratz.domain.data.player.state.BotState;
import fr.aresrpg.eratz.domain.data.player.state.PlayerState;
import fr.aresrpg.eratz.domain.ia.mind.Mind;
import fr.aresrpg.eratz.domain.util.Closeable;
import fr.aresrpg.tofumanchou.domain.data.enums.Bank;
import fr.aresrpg.tofumanchou.domain.data.item.Item;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.*;

import java.awt.Point;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class BotPerso implements Closeable {

	//private final Mind mind = new BaseMind(this);
	private ManchouPerso perso;
	private Group group;
	private PlayerState state;
	private ScheduledFuture sch;
	private boolean online;
	private boolean hasMount;
	private BotState botState = new BotState();
	private DofusMapView view;

	private ChatInfo chatInfos = new ChatInfo(this);
	private int[] itemsToKeep = new int[0];

	public BotPerso(ManchouPerso perso) {
		this.perso = perso;
		view = new DofusMapView();
		this.sch = Executors.SCHEDULER.register(this::scheduledActions, 10, TimeUnit.SECONDS);
	}

	public void scheduledActions() { // methode éxécutée toute les 10s, utile pour divers petit check

		perso.useRessourceBags();
	}

	/**
	 * @return the view
	 */
	public DofusMapView getView() {
		return view;
	}

	/**
	 * @param view
	 *            the view to set
	 */
	public void setView(DofusMapView view) {
		this.view = view;
	}

	@Override
	public void shutdown() {
		sch.cancel(true);
		//mind.shutdown();
	}

	/**
	 * @return the itemsToKeep
	 */
	public int[] getItemsToKeep() {
		return itemsToKeep;
	}

	public Bank getNearestBank() {
		Bank near = Bank.ASTRUB;
		int dist = perso.getMap().distance(near.getCoords());
		for (Bank k : Bank.values()) {
			int di = perso.getMap().distance(k.getCoords());
			if (di < dist) {
				dist = di;
				near = k;
			}
		}
		return near;
	}

	/**
	 * @param itemsToKeep
	 *            the itemsToKeep to set
	 */
	public void setItemsToKeep(int... itemsToKeep) {
		this.itemsToKeep = itemsToKeep;
	}

	public boolean hasMount() {
		return hasMount;
	}

	/**
	 * @return the botState
	 */
	public BotState getBotState() {
		return botState;
	}

	/**
	 * @param hasMount
	 *            the hasMount to set
	 */
	public void setHasMount(boolean hasMount) {
		this.hasMount = hasMount;
	}

	/**
	 * @return the online
	 */
	public boolean isOnline() {
		return online;
	}

	/**
	 * @param online
	 *            the online to set
	 */
	public void setOnline(boolean online) {
		this.online = online;
	}

	public void respondTo(String msg, Chat c) {
		String resp = chatInfos.getResponse(msg);
		if (resp != null) perso.speak(c, resp);
	}

	/**
	 * @return the chatInfos
	 */
	public ChatInfo getChatInfos() {
		return chatInfos;
	}

	/**
	 * @return the perso
	 */
	public ManchouPerso getPerso() {
		return perso;
	}

	/**
	 * @return the mind
	 */
	public Mind getMind() {
		return null; // FIXME
	}

	/**
	 * @return the state
	 */
	public PlayerState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(PlayerState state) {
		this.state = state;
	}

	public void sendPacketToServer(Packet... pkts) {
		Arrays.stream(pkts).forEach(this::sendPacketToServer);
	}

	public void sendPacketToServer(Packet pkt) {
		perso.sendPacketToServer(pkt);
	}

	public void sendPacketToServer(Packet pkt, int delay, TimeUnit unit) {
		Executors.SCHEDULED.schedule(() -> sendPacketToServer(pkt), delay, unit);
	}

	public boolean isAtAstrubZaap() {
		return perso.getMap().isOnCoords(4, -19);
	}

	public void connectIn(long time, TimeUnit unit) {
		Executors.SCHEDULED.schedule(this::connect, time, unit);
	}

	public void connect() {
		perso.connect();
	}

	public boolean isInFight() {
		return !perso.getMap().isEnded();
	}

	public boolean hasGroup() {
		return this.group != null;
	}

	public boolean isInBankMap() {
		int x = perso.getMap().getX();
		int y = perso.getMap().getY();
		if (perso.getMap().isOutdoor()) return false;
		ManchouMap m = perso.getMap();
		return m.isOnCoords(4, -16) ||
				m.isOnCoords(2, -2) ||
				m.isOnCoords(14, 25) ||
				m.isOnCoords(-23, 40) ||
				m.isOnCoords(-16, 4) ||
				m.isOnCoords(-29, -58);
	}

	/**
	 * @return the group
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

	private int quantityOf(int id, Set<Item> set) { // pr item non stackable
		return (int) set.stream().filter(i -> i.getTypeId() == id).count();
	}

	public boolean inventoryContainsItem(long itemUid) {
		return perso.getInventory().getContents().containsKey(itemUid);
	}

	public int getQuantityInInventoryOf(long itemUid) {
		Item i = perso.getInventory().getItem(itemUid);
		return i == null ? 0 : i.getAmount();
	}

	public int getQuantityInBanqueOf(long itemUid) {
		Item i = perso.getAccount().getBank().getItem(itemUid);
		return i == null ? 0 : i.getAmount();
	}

	/**
	 * Change de map en essayant d'aller sur le teleporter le plus loin du joueur
	 * 
	 * @return le temps du path ainsi que la cellule du tp ou -1 si aucun teleporteur n'est accessible (blocké par mob ?)
	 */
	public Pair<Long, ManchouCell> changeMap() {
		ManchouCell[] farestTeleporters = perso.getFarestTeleporters1030();
		if (farestTeleporters == null || farestTeleporters.length == 0) farestTeleporters = perso.getFarestTeleporters();
		if (farestTeleporters.length == 0) throw new NullPointerException("No teleporters found on map !");
		LOGGER.debug("farest tp = " + Arrays.toString(perso.getFarestTeleporters()));
		LOGGER.debug("nearest tp = " + Arrays.toString(perso.getNearestTeleporters()));
		int width = perso.getMap().getWidth();
		int height = perso.getMap().getHeight();
		Cell[] protocolCells = perso.getMap().getProtocolCells();
		for (ManchouCell c : farestTeleporters) {
			if (!Roads.canUseToTeleport(perso.getMap(), c.getId())) continue;
			List<Point> path = Pathfinding.getCellPath(perso.getCellId(), c.getId(), protocolCells, width, height, Pathfinding::getNeighbors, perso::canGoOnCellAvoidingMobs);
			if (path == null) continue;
			float time = Pathfinding.getPathTime(path, protocolCells, width, height, hasMount) * 30;
			botState.lastCellMoved = new Pair<ManchouMap, Integer>(perso.getMap(), c.getId());
			perso.move(path, true);
			return new Pair<Long, ManchouCell>((long) time, c);
		}
		return new Pair<Long, ManchouCell>(-1L, null);
	}

	/**
	 * Try to change map with the given direction
	 * 
	 * @param dir
	 *            the direction
	 * @return a Pair<pathTime,Teleporter> or a Pair<-1,null> if the teleporter is not found
	 */
	public Pair<Long, ManchouCell> changeMapWithDirection(Orientation dir) {
		int width = perso.getMap().getWidth();
		int height = perso.getMap().getHeight();
		int cell = perso.getCellId();
		Cell[] protocolCells = perso.getMap().getProtocolCells();
		LOGGER.error("CHANGE MAP TO " + dir);
		Function<Node, Node[]> func = Pathfinding::getNeighbors;
		int[] tps = perso.getTeleporters(i -> !Roads.canUseToTeleport(perso.getMap(), i));
		int tp = perso.getTp(dir, tps);
		LOGGER.error("cell = " + tp);
		if (tp == -1) return new Pair<Long, ManchouCell>(-1L, null);
		List<Point> cellPath = Pathfinding.getCellPath(cell, tp, perso.getMap().getProtocolCells(), width, height, func, perso::canGoOnCellAvoidingMobs);
		if (cellPath == null) return new Pair<Long, ManchouCell>(-1L, null);
		botState.lastCellMoved = new Pair<ManchouMap, Integer>(perso.getMap(), tp);
		perso.move(cellPath, true);
		return new Pair<Long, ManchouCell>((long) (Pathfinding.getPathTime(cellPath, protocolCells, width, height, false) * 30), perso.getMap().getCells()[tp]);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		return obj instanceof BotPerso && ((BotPerso) obj).perso.getUUID() == perso.getUUID();
	}

}
