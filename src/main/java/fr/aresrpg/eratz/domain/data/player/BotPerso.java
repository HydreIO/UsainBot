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

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.dofus.protocol.Packet;
import fr.aresrpg.dofus.protocol.item.client.ItemDestroyPacket;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.dofus.structures.Orientation;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.util.DofusMapView;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.dofus.util.Pathfinding.PathValidator;
import fr.aresrpg.eratz.domain.data.Paths;
import fr.aresrpg.eratz.domain.data.Roads;
import fr.aresrpg.eratz.domain.data.player.info.ChatInfo;
import fr.aresrpg.eratz.domain.data.player.state.BotState;
import fr.aresrpg.eratz.domain.data.player.state.PlayerState;
import fr.aresrpg.eratz.domain.event.PathEndEvent;
import fr.aresrpg.eratz.domain.ia.mind.Mind;
import fr.aresrpg.eratz.domain.util.Closeable;
import fr.aresrpg.eratz.domain.util.ComparatorUtil;
import fr.aresrpg.tofumanchou.domain.data.enums.*;
import fr.aresrpg.tofumanchou.domain.data.item.Item;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.*;

import java.awt.Point;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BotPerso implements Closeable {

	private ManchouPerso perso;
	private Group group;
	private PlayerState state;
	private ScheduledFuture sch;
	private boolean online;
	private boolean hasMount;
	private BotState botState = new BotState();
	private DofusMapView view;
	private Paths currentPath;

	private ChatInfo chatInfos = new ChatInfo(this);
	private int[] itemsToKeep = { DofusItems2.HACHE_DE_L_APPRENTI_BÛCHERON.getId(), DofusItems2.POTION_DE_CITE___BONTA.getId(), DofusItems2.POTION_DE_RAPPEL.getId() };

	private boolean moving;
	private ScheduledFuture antiblock;

	public BotPerso(ManchouPerso perso) {
		this.perso = perso;
		view = new DofusMapView();
		this.sch = Executors.SCHEDULER.register(this::scheduledActions, 10, TimeUnit.SECONDS);
	}

	public void scheduledActions() { // methode éxécutée toute les 10s, utile pour divers petit check
		perso.useRessourceBags();
	}

	/**
	 * @return the currentPath
	 */
	public Paths getCurrentPath() {
		return currentPath;
	}

	/**
	 * @param currentPath
	 *            the currentPath to set
	 */
	public void setCurrentPath(Paths currentPath) {
		this.currentPath = currentPath;
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

	public int getPodsPercent() {
		int curr = perso.getPods();
		int max = perso.getMaxPods();
		return 100 * curr / max;
	}

	public void destroyHeaviestRessource() {
		Item it = getPerso().getInventory().getHeaviestItem();
		if (it == null) throw new NullPointerException("La ressource la plus lourde est introuvable !");
		int maxp = perso.getMaxPods();
		int pod = perso.getPods();
		int over = pod + 1 - maxp;
		int poditem = it.getPods();
		int fullw = poditem * it.getAmount();
		LOGGER.debug("Pod en trop = " + over);
		LOGGER.debug("Poid total de l'item = " + fullw);
		if (fullw <= over) {
			LOGGER.debug("Destruction de x" + it.getAmount() + " " + it.getName());
			getPerso().sendPacketToServer(new ItemDestroyPacket(it.getUUID(), it.getAmount()));
		} else {
			int todestroy = over / poditem + (over % poditem == 0 ? 0 : 1);
			LOGGER.debug("Destruction de x" + todestroy + " " + it.getName());
			getPerso().sendPacketToServer(new ItemDestroyPacket(it.getUUID(), todestroy));
		}
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

	public void goToBankMap() {
		ManchouMap map = perso.getMap();
		Bank nearestBank = getNearestBank();
		final Point coords = nearestBank.getCoords();
		if (map.isOnCoords(coords.x, coords.y)) {
			perso.moveToCell(nearestBank.getCellid(), true, true, true);
			return;
		}
		botState.path.clear();
		botState.addPath(coords.x, coords.y);
		goToNextMap();
	}

	/**
	 * @return the antiblock
	 */
	public ScheduledFuture getAntiblock() {
		return antiblock;
	}

	public void goToNextMap() {
		if (moving) return;
		moving = true;
		final ManchouMap map = getPerso().getMap();
		if (!map.isEnded()) return;
		final BotState st = botState;
		LOGGER.debug("go to next map method");
		if (st.needToGo == null) {
			if (st.path.isEmpty()) {
				LOGGER.debug("PATH EMPTY return");
				new PathEndEvent(this, st).send();
				moving = false;
				return;
			}
			LOGGER.debug("Need to go null ! on calcule");
			final List<Point> list = st.path.stream().sorted((p1, p2) -> ComparatorUtil.comparingDistanceToPlayer(false, new Point(map.getX(), map.getY()), p1, p2)).collect(Collectors.toList());
			st.needToGo = list.remove(0);
			LOGGER.debug("needtogo apres calcul = " + st.needToGo);
			st.path.remove(st.needToGo);
		}
		final Point next = st.needToGo;
		LOGGER.debug("needtogo point = " + next);
		PathValidator val = (a, b, c, d) -> {
			boolean canMove = Roads.canMove(a, b, c, d);
			boolean contains = st.lastBlockedMap.contains(new Point(c, d));
			return canMove && !contains;
		};
		final List<Point> path = Pathfinding.getPath(map.getX(), map.getY(), next.x, next.y, val, Pathfinding::getNeighbors);
		if (path == null) {
			LOGGER.debug("Unable to find path from [" + map.getX() + "," + map.getY() + "] to [" + next.x + "," + next.y + "]");
			LOGGER.debug("Retrying");
			moving = false;
			changeMap();
			return;
		}
		if (path.size() == 1) {
			st.needToGo = null;
			LOGGER.debug("path size = 1 !! " + path);
			moving = false;
			goToNextMap();
			return;
		}
		LOGGER.debug("path pour aller a la next ressource map " + path);
		final Point point = path.get(1);
		LOGGER.debug("prochain point " + point);
		Orientation dir = Pathfinding.getDirectionForMap(map.getX(), map.getY(), point.x, point.y);
		if (dir == null) {
			moving = false;
			throw new NullPointerException("Unable to find a direction to move from [" + map.getX() + "," + map.getY() + "] to [" + point.x + "," + point.y + "]");
		}
		LOGGER.debug("st haschangedmap = false");
		LOGGER.debug("direction pour bouger= " + dir);
		Pair<Long, ManchouCell> timeToTravel = changeMapWithDirection(dir);
		if (timeToTravel.getFirst() == -1L) { // éssai sans diagonales
			LOGGER.debug("impossible d'aller dans la direction " + dir);
			LOGGER.debug("On change de map sans diagonals");
			dir = dir.getNearestNeighborWithoutDiagonal();
			timeToTravel = changeMapWithDirection(dir);
		}
		if (timeToTravel.getFirst() == -1L) { // éssai random
			LOGGER.debug("impossible d'aller dans la direction " + dir);
			st.lastBlockedMap.add(map.toPoint());
			LOGGER.debug("on est blocké sur " + map.getCoordsInfos());
			LOGGER.debug("On change de map random");
			timeToTravel = changeMap();
		}
		if (timeToTravel.getFirst() == -1L) { // echec
			LOGGER.debug("le changeMap a échoué retry dans 15s");
			Executors.SCHEDULED.schedule(Threads.threadContextSwitch("GoToNextMap-" + getPerso().getPseudo(), () -> {
				setMoving(false);
				goToNextMap();
			}), 15, TimeUnit.SECONDS);// Retest des que les mobs bougent ! TODO
			getPerso().sit(true);
			Threads.uSleep(500, TimeUnit.MILLISECONDS);
			getPerso().sendSmiley(Smiley.SUEUR);
			return;
		}
		final ManchouCell celltogo = timeToTravel.getSecond();
		LOGGER.debug("celltogo = " + celltogo.toPoint());
		antiblock = Executors.SCHEDULED.schedule(Threads.threadContextSwitch("BadTp", () -> {
			final ManchouMap mm = getPerso().getMap();
			if (mm.isOnCoords(map.getX(), map.getY()) && mm.isEnded()) {
				LOGGER.error("IMPOSSIBLE D'USE CE TP (" + celltogo + "), ON NOTE ET ON RETENTE");
				Roads.notifyCantUse(mm, celltogo.getId());
				moving = false;
				goToNextMap();
			}
		}), (long) (timeToTravel.getFirst() * 2), TimeUnit.MILLISECONDS);
		moving = false;
	}

	/**
	 * @param moving
	 *            the moving to set
	 */
	private void setMoving(boolean moving) {
		this.moving = moving;
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
		ManchouCell[] farestTeleporters = perso.getFarestTeleporters();
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
