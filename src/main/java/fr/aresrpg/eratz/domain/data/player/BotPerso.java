/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.data.player;

import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.dofus.protocol.Packet;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.dofus.structures.Orientation;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.util.Maps;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.eratz.domain.data.Roads;
import fr.aresrpg.eratz.domain.data.player.info.ChatInfo;
import fr.aresrpg.eratz.domain.data.player.state.BotState;
import fr.aresrpg.eratz.domain.data.player.state.PlayerState;
import fr.aresrpg.eratz.domain.ia.mind.Mind;
import fr.aresrpg.eratz.domain.util.Closeable;
import fr.aresrpg.tofumanchou.domain.data.item.Item;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.ManchouCell;
import fr.aresrpg.tofumanchou.infra.data.ManchouPerso;

import java.awt.Point;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class BotPerso implements Closeable {

	//private final Mind mind = new BaseMind(this);
	private ManchouPerso perso;
	private Group group;
	private PlayerState state;
	private ScheduledFuture sch;
	private boolean online;
	private boolean hasMount;
	private BotState botState = new BotState();

	private ChatInfo chatInfos = new ChatInfo(this);

	public BotPerso(ManchouPerso perso) {
		this.perso = perso;
		this.sch = Executors.SCHEDULER.register(this::scheduledActions, 10, TimeUnit.SECONDS);
	}

	public void scheduledActions() { // methode éxécutée toute les 10s, utile pour divers petit check

		perso.useRessourceBags();
	}

	@Override
	public void shutdown() {
		sch.cancel(true);
		//mind.shutdown();
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
		int width = perso.getMap().getWidth();
		int height = perso.getMap().getHeight();
		int px = Maps.getX(perso.getCellId(), width, height);
		int py = Maps.getY(perso.getCellId(), width, height);
		Cell[] protocolCells = perso.getMap().getProtocolCells();
		for (ManchouCell c : farestTeleporters) {
			if (!Roads.canUseToTeleport(perso.getMap(), c.getId())) continue;
			int x = c.getX();
			int y = c.getY();
			List<Point> path = Pathfinding.getCellPath(px, py, x, y, protocolCells, width, height, true, perso::canGoOnCellAvoidingMobs);
			if (path == null) continue;
			float time = Pathfinding.getPathTime(path, protocolCells, width, height, hasMount) * 30;
			perso.move(path, true);
			return new Pair<Long, ManchouCell>((long) time, c);
		}
		return new Pair<Long, ManchouCell>(-1L, null);
	}

	public Pair<Long, ManchouCell> changeMapWithDirection(Orientation dir) {
		int width = perso.getMap().getWidth();
		int height = perso.getMap().getHeight();
		int cell = perso.getCellId();
		Cell[] protocolCells = perso.getMap().getProtocolCells();
		int px = Maps.getX(cell, width, height);
		int py = Maps.getY(cell, width, height);
		switch (dir) {
			case DOWN:
			case DOWN_LEFT:
			case DOWN_RIGHT:
				int downTp = perso.getDownTp(i -> Roads.canUseToTeleport(perso.getMap(), i));
				int x = Maps.getX(downTp, width, height);
				int y = Maps.getY(downTp, width, height);
				List<Point> cellPath = Pathfinding.getCellPath(px, py, x, y, perso.getMap().getProtocolCells(), width, height, true, perso::canGoOnCellAvoidingMobs);
				if (cellPath == null) return new Pair<Long, ManchouCell>(-1L, null);
				perso.move(cellPath, true);
				return new Pair<Long, ManchouCell>((long) (Pathfinding.getPathTime(cellPath, protocolCells, width, height, false) * 30), perso.getMap().getCells()[downTp]);
			case LEFT:
				int lTp = perso.getRightTp(i -> Roads.canUseToTeleport(perso.getMap(), i));
				int lx = Maps.getX(lTp, width, height);
				int ly = Maps.getY(lTp, width, height);
				List<Point> lcellPath = Pathfinding.getCellPath(px, py, lx, ly, perso.getMap().getProtocolCells(), width, height, true, perso::canGoOnCellAvoidingMobs);
				if (lcellPath == null) return new Pair<Long, ManchouCell>(-1L, null);
				perso.move(lcellPath, true);
				return new Pair<Long, ManchouCell>((long) (Pathfinding.getPathTime(lcellPath, protocolCells, width, height, false) * 30), perso.getMap().getCells()[lTp]);
			case RIGHT:
				int rTp = perso.getRightTp(i -> Roads.canUseToTeleport(perso.getMap(), i));
				int rx = Maps.getX(rTp, width, height);
				int ry = Maps.getY(rTp, width, height);
				List<Point> rcellPath = Pathfinding.getCellPath(px, py, rx, ry, perso.getMap().getProtocolCells(), width, height, true, perso::canGoOnCellAvoidingMobs);
				if (rcellPath == null) return new Pair<Long, ManchouCell>(-1L, null);
				perso.move(rcellPath, true);
				return new Pair<Long, ManchouCell>((long) (Pathfinding.getPathTime(rcellPath, protocolCells, width, height, false) * 30), perso.getMap().getCells()[rTp]);
			case UP:
			case UP_LEFT:
			case UP_RIGHT:
				int upTp = perso.getUpTp(i -> Roads.canUseToTeleport(perso.getMap(), i));
				int ux = Maps.getX(upTp, width, height);
				int uy = Maps.getY(upTp, width, height);
				List<Point> ucellPath = Pathfinding.getCellPath(px, py, ux, uy, perso.getMap().getProtocolCells(), width, height, true, perso::canGoOnCellAvoidingMobs);
				if (ucellPath == null) return new Pair<Long, ManchouCell>(-1L, null);
				perso.move(ucellPath, true);
				return new Pair<Long, ManchouCell>((long) (Pathfinding.getPathTime(ucellPath, protocolCells, width, height, false) * 30), perso.getMap().getCells()[upTp]);
		}
		return new Pair<Long, ManchouCell>(-1L, null);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		return obj instanceof BotPerso && ((BotPerso) obj).perso.getUUID() == perso.getUUID();
	}

}
