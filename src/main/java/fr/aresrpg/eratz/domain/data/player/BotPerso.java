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
import fr.aresrpg.commons.domain.log.AnsiColors.AnsiColor;
import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.dofus.protocol.Packet;
import fr.aresrpg.dofus.protocol.exchange.client.ExchangeMoveItemsPacket.MovedItem;
import fr.aresrpg.dofus.protocol.item.client.ItemDestroyPacket;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.dofus.structures.Orientation;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.structures.stat.Stat;
import fr.aresrpg.dofus.util.*;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.dofus.util.Pathfinding.PathValidator;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.Paths;
import fr.aresrpg.eratz.domain.data.Roads;
import fr.aresrpg.eratz.domain.data.player.info.ChatInfo;
import fr.aresrpg.eratz.domain.data.player.state.BotState;
import fr.aresrpg.eratz.domain.data.player.state.PlayerState;
import fr.aresrpg.eratz.domain.event.PathEndEvent;
import fr.aresrpg.eratz.domain.util.*;
import fr.aresrpg.tofumanchou.domain.data.Spell;
import fr.aresrpg.tofumanchou.domain.data.entity.Entity;
import fr.aresrpg.tofumanchou.domain.data.entity.mob.Mob;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Player;
import fr.aresrpg.tofumanchou.domain.data.enums.*;
import fr.aresrpg.tofumanchou.domain.data.item.Item;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.*;

import java.awt.Point;
import java.util.*;
import java.util.Map.Entry;
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
	private Map<Integer, Integer> itemsToKeep = new HashMap<>();
	private final int kamasToKeep = 10_000;

	private ScheduledFuture antiblock;

	public BotPerso(ManchouPerso perso) {
		this.perso = perso;
		view = new DofusMapView();
		itemsToKeep.put(DofusItems2.POTION_DE_CITE_BONTA.getId(), 20);
		itemsToKeep.put(DofusItems2.POTION_DE_RAPPEL.getId(), 20);
		this.sch = Executors.SCHEDULER.register(this::scheduledActions, 10, TimeUnit.SECONDS);
	}

	public void scheduledActions() { // methode éxécutée toute les 10s, utile pour divers petit check
		if (perso == null || perso.getUUID() == 0 || perso.getMap() == null) return;
		try {
			perso.useRessourceBags();
			if (currentPath != null && perso.getMap().isEnded() && botState.lastmove + 30000 < System.currentTimeMillis()) {// bloqué depuis + de 20s 
				LOGGER.severe("BOT BLOCKED ! UNBLOCKING..");
				if (currentPath != null) {
					currentPath.getPath().fillCoords(botState.path);
					currentPath.getPath().fillRessources(botState.ressources);
				}
				changeMap();
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	/**
	 * @return the kamasToKeep
	 */
	public int getKamasToKeep() {
		return kamasToKeep;
	}

	/**
	 * @return the itemsToKeep
	 */
	public Map<Integer, Integer> getItemsToKeep() {
		return itemsToKeep;
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

	public void notifyNeedToGoBank() {
		botState.goToBank = true;
		//	if (hasPopoBonta()) perso.useItem(DofusItems2.POTION_DE_CITE_BONTA);
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

	private boolean isBlockedLoop() {
		List<ManchouMap> l = botState.visitedMaps;
		if (l.size() < 6) return false;
		ManchouMap m1 = l.get(0);
		ManchouMap m2 = l.get(1);
		ManchouMap m3 = l.get(2);
		ManchouMap m4 = l.get(3);
		ManchouMap m5 = l.get(4);
		ManchouMap m6 = l.get(5);
		if (m1.equals(m3) && m2.equals(m4) && m3.equals(m5) && m4.equals(m6)) {
			LOGGER.severe("BLOCKED LOOP === !");
			Roads.notifyCantUse(botState.lastCellMoved.getFirst(), botState.lastCellMoved.getSecond().getFirst());
			l.clear();
			return true;
		}
		l.clear();
		return false;
	}

	public void depositBank() {
		final Set<Item> inv = perso.getInventory().getContents().values().stream().map(i -> (ManchouItem) i).filter(UtilFunc.needToDeposit(this)).collect(Collectors.toSet());
		final MovedItem[] array = inv.stream().map(i -> (ManchouItem) i).map(UtilFunc.deposit(this)).filter(Objects::nonNull).toArray(MovedItem[]::new);
		Arrays.stream(array).forEach(i -> {
			perso.moveItem(i);
			Threads.uSleep(250, TimeUnit.MILLISECONDS);
		});
		Threads.uSleep(1, TimeUnit.SECONDS);
		LOGGER.info(AnsiColor.GREEN + "à déposé : " + inv.stream().map(Item::showInfos).collect(Collectors.joining(",", "[", "]")) + " en banque !");
		MovedItem[] toRetrieve = perso.getAccount().getBank().getContents().values().stream().map(i -> (ManchouItem) i).map(UtilFunc.retrieve(this)).filter(Objects::nonNull)
				.toArray(MovedItem[]::new);
		Arrays.stream(toRetrieve).forEach(i -> {
			perso.moveItem(i);
			Threads.uSleep(250, TimeUnit.MILLISECONDS);
		});
		Threads.uSleep(1, TimeUnit.SECONDS);
		final int kamas = perso.getInventory().getKamas();
		final int bankK = perso.getAccount().getBank().getKamas();
		int tomove;
		if (kamas < 10_000) {
			tomove = 10_000 - kamas;
			if (tomove > bankK) tomove = bankK;
			tomove = -tomove;
		} else tomove = kamas - 10_000;
		perso.moveKama(tomove);
		perso.exchangeLeave();
	}

	public void goToNextMap() {
		final ManchouMap map = getPerso().getMap();
		if (!map.isEnded()) return;
		final BotState st = botState;
		LOGGER.debug("go to next map method");
		if (st.needToGo == null) {
			if (st.path.isEmpty()) {
				LOGGER.debug("PATH EMPTY return");
				new PathEndEvent(this, st).send();
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
		int distanceToAstrub = Maps.distanceManathan(Zaap.ASTRUB.getCoords(), next, map.getWidth(), map.getHeight());
		int distanceToPlayer = Maps.distanceManathan(map.toPoint(), next, map.getWidth(), map.getHeight());
		PathValidator val = (a, b, c, d) -> {
			boolean canMove = Roads.canMove(a, b, c, d);
			boolean contains = st.lastBlockedMap.contains(new Point(c, d));
			return canMove && !contains;
		};
		final List<Point> path = Pathfinding.getPath(map.getX(), map.getY(), next.x, next.y, val, Pathfinding::getNeighbors);
		if (path == null) {
			LOGGER.debug("Unable to find path from [" + map.getX() + "," + map.getY() + "] to [" + next.x + "," + next.y + "]");
			LOGGER.debug("Retrying");
			changeMap();
			return;
		}
		if (path.size() == 1) {
			st.needToGo = null;
			LOGGER.debug("path size = 1 !! " + path);
			goToNextMap();
			return;
		}
		LOGGER.debug("path pour aller a la next ressource map " + path);
		final Point point = path.get(1);
		LOGGER.debug("prochain point " + point);
		Orientation dir = Pathfinding.getDirectionForMap(map.getX(), map.getY(), point.x, point.y);
		if (dir == null) { throw new NullPointerException("Unable to find a direction to move from [" + map.getX() + "," + map.getY() + "] to [" + point.x + "," + point.y + "]"); }
		LOGGER.debug("st haschangedmap = false");
		LOGGER.debug("direction pour bouger= " + dir);
		Pair<Long, ManchouCell> timeToTravel = isBlockedLoop() ? changeMap() : changeMapWithDirection(dir);
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
			//getPerso().sendSmiley(Smiley.SUEUR);
			//getPerso().sit(true);
			getPerso().useItem(DofusItems2.POTION_DE_RAPPEL);
			return;
		}
		final ManchouCell celltogo = timeToTravel.getSecond();
		LOGGER.debug("celltogo = " + celltogo.toPoint());
		antiblock = Executors.SCHEDULED.schedule(Threads.threadContextSwitch("BadTp", () -> {
			final ManchouMap mm = getPerso().getMap();
			if (mm.isOnCoords(map.getX(), map.getY()) && mm.isEnded()) {
				LOGGER.error("IMPOSSIBLE D'USE CE TP (" + celltogo + "), ON NOTE ET ON RETENTE");
				Roads.notifyCantUse(mm, celltogo.getId());
				goToNextMap();
			}
		}), (long) (timeToTravel.getFirst() * 2), TimeUnit.MILLISECONDS);
	}

	public List<Integer> getAccessibleCells(int range) {
		return ShadowCasting.getAccesibleCells(perso.getCellId(), range, perso.getMap().serialize(), perso.getMap().cellAccessible().negate()).stream().map(Cell::getId).collect(Collectors.toList());
	}

	public List<Integer> getAccessibleCells(int origin, int range) {
		return ShadowCasting.getAccesibleCells(origin, range, perso.getMap().serialize(), perso.getMap().cellAccessible().negate()).stream().map(Cell::getId).collect(Collectors.toList());
	}

	public boolean hasPopoRappel() {
		return !perso.getInventory().getItems(DofusItems2.POTION_DE_RAPPEL.getId()).isEmpty();
	}

	public boolean hasPopoBonta() {
		return !perso.getInventory().getItems(DofusItems2.POTION_DE_CITE_BONTA.getId()).isEmpty();
	}

	public boolean hasPopoBrakmar() {
		return !perso.getInventory().getItems(DofusItems2.POTION_DE_CITE_BRAKMAR.getId()).isEmpty();
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

	public int getQuantityInInventoryOf(int itemTypeId) {
		Set<Item> i = perso.getInventory().getItems(itemTypeId);
		return (int) (i.isEmpty() ? 0 : i.stream().mapToLong(Item::getAmount).sum());
	}

	public int getQuantityInBankOf(long itemUid) {
		Item i = perso.getAccount().getBank().getItem(itemUid);
		return i == null ? 0 : i.getAmount();
	}

	public int getQuantityInBankOf(int itemTypeId) {
		Set<Item> i = perso.getAccount().getBank().getItems(itemTypeId);
		return (int) (i.isEmpty() ? 0 : i.stream().mapToLong(Item::getAmount).sum());
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
			botState.lastCellMoved = new Pair<ManchouMap, Pair<Integer, Orientation>>(perso.getMap(), new Pair<Integer, Orientation>(c.getId(), null));
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
		botState.lastCellMoved = new Pair<ManchouMap, Pair<Integer, Orientation>>(perso.getMap(), new Pair<Integer, Orientation>(tp, dir));
		perso.move(cellPath, true);
		return new Pair<Long, ManchouCell>((long) (Pathfinding.getPathTime(cellPath, protocolCells, width, height, false) * 30), perso.getMap().getCells()[tp]);
	}

	public Cell getBestCellForZoneSpell(final int distToPlayer) {
		return null;
	}

	/**
	 * Trouve une case proche du joueur la plus proche possible du mob
	 * 
	 * @param distToPlayer
	 *            limite depuis le joueur
	 * @param m
	 *            le mob
	 * @return la case ou null si non trouvée
	 */
	public ManchouCell getCellNearMob(final Entity m, final int distToPlayer, boolean free) {
		final Set<ManchouCell> aroundP = getCellsAroundPlayer(distToPlayer);
		ManchouCell nearest = null;
		int dist = Integer.MAX_VALUE;
		for (final ManchouCell cell : aroundP) {
			if (!cell.isWalkeable()) continue;
			if (free && cell.hasMobOn()) continue;
			final int distance = cell.distanceManathan(m.getCellId());
			if (nearest == null || distance < dist) {
				nearest = cell;
				dist = distance;
			}
		}
		return nearest;
	}

	public int getMissingMaxPoFor(final Spell spell, final int targetCell) {
		int maxPo = spell.getMaxPo() + perso.getStat(Stat.PO).getTotal();
		if (maxPo < 1) maxPo = 1;
		final int cell = perso.getCellId();
		final int width = perso.getMap().getWidth();
		final int height = perso.getMap().getHeight();
		return Maps.distanceManathan(cell, targetCell, width, height) - maxPo;
	}

	public int getMissingMinPoFor(final Spell spell, final int targetCell) {
		final int minPo = spell.getMinPo();
		final int cell = perso.getCellId();
		final int width = perso.getMap().getWidth();
		final int height = perso.getMap().getHeight();
		return minPo - Maps.distanceManathan(cell, targetCell, width, height);
	}

	public int getMaxPoFor(Spell spell) {
		return spell.getMaxPo() + perso.getStat(Stat.PO).getTotal();
	}

	public boolean hasMinPoFor(final Spell spell, final int targetCell) {
		final int minPo = spell.getMinPo();
		final int cell = perso.getCellId();
		final int width = perso.getMap().getWidth();
		final int height = perso.getMap().getHeight();
		return Maps.distanceManathan(cell, targetCell, width, height) >= minPo;
	}

	public boolean hasMaxPoFor(final Spell spell, final int targetCell) {
		final int maxPo = spell.getMaxPo() + perso.getStat(Stat.PO).getTotal();
		final int cell = perso.getCellId();
		final int width = perso.getMap().getWidth();
		final int height = perso.getMap().getHeight();
		BotFather.LOGGER.error("[playerCell:" + cell + "][targetCell:" + targetCell + "] distance = " + Maps.distanceManathan(cell, targetCell, width, height) + " maxpo = " + maxPo);
		return Maps.distanceManathan(cell, targetCell, width, height) <= maxPo;
	}

	public void runToMob(final Entity m, final boolean safely, int pmToUse) {
		final int pm = perso.getPm();
		if (pmToUse > pm) pmToUse = pm;
		final ManchouCell cellNearMob = safely ? getCellNearAndSafeFromMob(m, pmToUse, true) : getCellNearMob(m, pmToUse, true);
		runTo(cellNearMob.getId());
	}

	public void runAwayFromMobs() {
		try {
			ManchouCell c = getCellAwayFromMob(perso.getPm());
			LOGGER.debug("pm player = " + perso.getPm());
			if (c != null) {
				BotFather.LOGGER.severe("cell away from mob = " + c.getId());
				runTo(c.getId());
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	public boolean isSafeFromMobs() {
		int team = perso.getTeam();
		for (Entity e : perso.getMap().getEntities().values()) {
			if (e instanceof Perso) continue;
			else if (e instanceof Player) {
				ManchouPlayerEntity pl = (ManchouPlayerEntity) e;
				if (pl.getTeam() == team) continue;
				int dist = Maps.distanceManathan(perso.getCellId(), pl.getCellId(), perso.getMap().getWidth(), perso.getMap().getHeight()) - 1;//-1 car pas besoin d'arriver sur la cell
				if (dist <= pl.getPm()) return false;
			} else if (e instanceof Mob) {
				ManchouMob m = (ManchouMob) e;
				if (m.getTeam() == team) continue;
				int dist = Maps.distanceManathan(perso.getCellId(), m.getCellId(), perso.getMap().getWidth(), perso.getMap().getHeight()) - 1;//-1 car pas besoin d'arriver sur la cell
				if (dist <= m.getPm()) return false;
			}
		}
		return true;
	}

	public void runTo(final int cell) {
		final int width = perso.getMap().getWidth();
		final int height = perso.getMap().getHeight();
		final int cellId = perso.getCellId();
		if (cell == cellId) {
			LOGGER.debug("déja sur la cell");
			return;
		}
		int dist = Maps.distanceManathan(cellId, cell, width, height);
		final PathValidator canGo = (x1, y1, x2, y2) -> {
			final int id = Maps.getIdRotated(x2, y2, width, height);
			return !perso.getMap().getCells()[id].hasMobOn();
		};
		final List<Point> cellPath = Pathfinding.getCellPath(cellId, cell, perso.getMap().getProtocolCells(), width, height, Pathfinding::getNeighborsWithoutDiagonals, canGo);
		// perso.setPm(perso.getPm() - dist); // TEMP REMOVE PM car on attend pas que le serv nous le dise pour pouvoir finir notre tour, de tt façon il reset apres
		BotFather.LOGGER.warning("Trying to move from " + cellId + " to " + cell + " path=" + cellPath);
		if (cellPath == null) throw new NullPointerException("PATH INVALID -_-");
		perso.move(cellPath, false);
	}

	/**
	 * Trouve une case inateignable par un mob mais le plus proche possible de lui
	 * 
	 * @param distToPlayer
	 *            limite de distance depuis le joueur
	 * @return la case ou null si non trouvée
	 */
	public ManchouCell getCellNearAndSafeFromMob(final Entity m, final int distToPlayer, boolean free) {
		final Set<ManchouCell> aroundP = getCellsAroundPlayer(distToPlayer);
		final Iterator<ManchouCell> it = aroundP.iterator();
		while (it.hasNext()) {
			final ManchouCell cell = it.next();
			if (free && !cell.isWalkeable()) it.remove();
			if (cell.distanceManathan(m.getCellId()) <= m.getPm()) it.remove();
		}
		ManchouCell found = null;
		int dist = Integer.MAX_VALUE;
		for (final ManchouCell cell : aroundP) {
			final int distance = cell.distanceManathan(m.getCellId());
			if (found == null || distance < dist) {
				found = cell;
				dist = distance;
			}
		}
		return found;
	}

	/**
	 * Trouve toute les case safe autour du joueur par rapport a un mob
	 * 
	 * @param m
	 *            le mob
	 * @param distToPlayer
	 *            distance max
	 * @return les cases ou un set vide si aucune case trouvée
	 */
	public Set<ManchouCell> getCellsSafeFromMob(final Entity m, final int distToPlayer) {
		final Set<ManchouCell> aroundP = getCellsAroundPlayer(distToPlayer);
		final Iterator<ManchouCell> it = aroundP.iterator();
		while (it.hasNext()) {
			final ManchouCell cell = it.next();
			if (cell.hasMobOn() || !cell.isWalkeable() || cell.distanceManathan(m.getCellId()) <= m.getPm()) it.remove();
		}
		return aroundP;
	}

	/**
	 * Trouve une case a distance X du joueur ayant la ligne de vue pour toucher une cell
	 * 
	 * @param distToPlayer
	 *            distance max du joueur
	 * @param targetCell
	 *            ennemy
	 * @param range
	 *            portee du sort
	 * @param line
	 *            lancer en ligne
	 * @return une case accessible pouvant viser la cible
	 */
	public ManchouCell getCellToTargetMob(int distToPlayer, int targetCell, int range, boolean line) {
		Set<ManchouCell> cellsAroundPlayer = getCellsAroundPlayer(distToPlayer);
		for (ManchouCell c : cellsAroundPlayer) {
			if (c.hasMobOn() || !c.isWalkeable()) continue;
			if (line && !c.isOnSameLineOrCollumn(perso.getMap().getCells()[targetCell])) continue;
			List<Integer> acc = getAccessibleCells(c.getId(), range);
			if (acc.contains(targetCell)) return c;
		}
		return null;
	}

	public Entity getNearestEnnemy() {
		int dist = Integer.MAX_VALUE;
		Entity near = null;
		for (final Entity m : perso.getMap().getEntities().values()) {
			if (m.getLife() < 1) continue;
			// continue if allies
			if (m instanceof Perso) continue;
			else if (m instanceof Player) {
				ManchouPlayerEntity pl = (ManchouPlayerEntity) m;
				if (pl.getTeam() == perso.getTeam()) continue;
			} else if (m instanceof Mob) {
				ManchouMob mm = (ManchouMob) m;
				if (mm.getTeam() == perso.getTeam()) continue;
			}
			if (near == null) near = m;
			final int distance = Maps.distanceManathan(perso.getCellId(), m.getCellId(), perso.getMap().getWidth(), perso.getMap().getHeight());//pas besoin du -1 car on cherche le plus pres
			if (distance < dist) {
				dist = distance;
				near = m;
			}
		}
		return near;
	}

	/**
	 * Trouve une case la plus éloignée possible des mobs
	 * 
	 * @param distToPlayer
	 *            limite de distance depuis le joueur
	 * @return la case ou null si non trouvée
	 */
	public ManchouCell getCellAwayFromMob(final int distToPlayer) {
		final Set<ManchouCell> aroundP = getCellsAroundPlayer(distToPlayer);
		Map<ManchouCell, Integer> cost = new HashMap<>();
		int team = perso.getTeam();
		for (ManchouCell c : aroundP) {
			if (c.hasMobOn() || !c.isWalkeable()) continue;
			int pts = 0;
			for (Entity e : perso.getMap().getEntities().values()) {
				// continue if allies
				if (e instanceof Perso) continue;
				else if (e instanceof Player) {
					ManchouPlayerEntity pl = (ManchouPlayerEntity) e;
					if (pl.getTeam() == team) continue;
				} else if (e instanceof Mob) {
					ManchouMob m = (ManchouMob) e;
					if (m.getTeam() == team) continue;
				}
				int cl = e.getCellId();
				pts += c.distanceManathan(cl);
			}
			cost.put(c, pts);
		}
		ManchouCell far = null;
		int maxpts = 0;
		for (Entry<ManchouCell, Integer> i : cost.entrySet()) {
			if (i.getValue() > maxpts) {
				far = i.getKey();
				maxpts = i.getValue();
			}
		}
		return far;
	}

	public Set<ManchouCell> getCellsAroundPlayer(final int dist) {
		final Set<ManchouCell> around = new HashSet<>();
		Arrays.stream(perso.getMap().getCells()).filter(c -> Maps.distanceManathan(perso.getCellId(), c.getId(), perso.getMap().getWidth(), perso.getMap().getHeight()) <= dist).forEach(around::add);
		return around;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		return obj instanceof BotPerso && ((BotPerso) obj).perso.getUUID() == perso.getUUID();
	}

	@Override
	public int hashCode() {
		return (int) perso.getUUID();
	}

}
