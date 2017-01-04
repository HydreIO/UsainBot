package fr.aresrpg.eratz.domain.listener;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.event.*;
import fr.aresrpg.commons.domain.log.AnsiColors.AnsiColor;
import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.dofus.protocol.exchange.client.ExchangeMoveItemsPacket.MovedItem;
import fr.aresrpg.dofus.structures.*;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.dofus.structures.item.ItemCategory;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.util.Maps;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.dofus.util.Pathfinding.PathValidator;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.Roads;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.state.BotState;
import fr.aresrpg.eratz.domain.event.PathEndEvent;
import fr.aresrpg.eratz.domain.util.ComparatorUtil;
import fr.aresrpg.eratz.domain.util.InterractUtil;
import fr.aresrpg.tofumanchou.domain.data.enums.Smiley;
import fr.aresrpg.tofumanchou.domain.data.item.Item;
import fr.aresrpg.tofumanchou.domain.event.fight.FightEndEvent;
import fr.aresrpg.tofumanchou.domain.event.map.FrameUpdateEvent;
import fr.aresrpg.tofumanchou.domain.event.map.HarvestTimeReceiveEvent;
import fr.aresrpg.tofumanchou.domain.event.player.MapJoinEvent;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.*;

import java.awt.Point;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public class HarvestListener implements Listener {

	private static HarvestListener instance = new HarvestListener();
	private static List<Pair<EventBus, Subscriber>> subs = new ArrayList<>();

	public HarvestListener() {
		instance = this;
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

	@Subscribe
	public void onFrame(final FrameUpdateEvent e) {
		final BotPerso perso = BotFather.getPerso(e.getClient());
		final BotState st = perso.getBotState();
		final boolean finishedHarvest = e.getCell().getId() == st.currentRessource && e.getFrame() == 3;
		final ManchouMap map = (ManchouMap) e.getClient().getPerso().getMap();
		if (finishedHarvest && !st.goToBank && !harvestRessource(BotFather.getPerso(e.getClient()), (ManchouMap) e.getClient().getPerso().getMap(), st)) {
			st.needToGo = null;
			goToNextMap(perso, map, st); // si il a finit d'harvest & qu'il ne doit pas aller a la banque et qu'il n'y a plus de ressources sur la map
		}
	}

	@Subscribe
	public void onFightEnd(final FightEndEvent e) {
		Executors.SCHEDULED.schedule(() -> {
			final ManchouPerso perso = (ManchouPerso) e.getClient().getPerso();
			final BotPerso bp = BotFather.getPerso(perso);
			final BotState st = bp.getBotState();
			if (!st.goToBank && !harvestRessource(bp, perso.getMap(), st)) {
				st.needToGo = null;
				goToNextMap(bp, perso.getMap(), st);
			}
		} , 1, TimeUnit.SECONDS);
	}

	@Subscribe
	public void onRessourceStartHarvest(final HarvestTimeReceiveEvent e) {
		final BotPerso perso = BotFather.getPerso(e.getClient());
		final BotState st = perso.getBotState();
		final boolean steal = st.currentRessource == e.getCellId() && e.getClient().getPerso().getUUID() != e.getPlayer().getUUID();
		final ManchouMap map = (ManchouMap) e.getClient().getPerso().getMap();
		LOGGER.debug("STEAL = " + steal);
		if (steal && !st.goToBank && !harvestRessource(perso, map, st)) {
			st.needToGo = null; // si on lui a volé la ressource & qu'il ne doit pas aller a la banque et qu'il n'y a plus de ressources sur la map
			goToNextMap(perso, map, st);
		}
	}

	@Subscribe
	public void onMap(final MapJoinEvent e) {
		LOGGER.debug("new map");
		if (e.getMap().isEnded())
			Executors.SCHEDULED.schedule(Threads.threadContextSwitch("MapJoin", () -> this.onMap(BotFather.getPerso(e.getClient()), (ManchouMap) e.getMap())),
					1, TimeUnit.SECONDS);
	}

	private void onMap(final BotPerso perso, final ManchouMap map) {
		final BotState st = perso.getBotState();
		LOGGER.debug("checking map");
		Roads.checkMap(map, perso); // enregistre les tp manquant etc
		st.hasChangedMap = true;
		LOGGER.debug("set has changed map true");
		try {
			boolean canUseToTeleport = st.lastCellMoved == null ? true : Roads.canUseToTeleport(st.lastCellMoved.getFirst(), st.lastCellMoved.getSecond());
			LOGGER.debug("rt");
			LOGGER.debug("lastmoved = " + st.lastCellMoved + " can use ? " + canUseToTeleport);
			if (!st.canGoIndoor && !map.isOutdoor()) {
				LOGGER.debug("Indoor !" + map.getCoordsInfos());
				final ManchouCell cl = perso.getPerso().getNearestTeleporters()[0];
				if (perso.isInBankMap()) {
					LOGGER.debug("in bank");
					perso.getPerso().speakToNpc(-2);
					Threads.uSleep(200, TimeUnit.MILLISECONDS);
					perso.getPerso().npcTalkChoice(318, 259);
					Threads.uSleep(500, TimeUnit.MILLISECONDS);
					depositBank(perso);
					Threads.uSleep(500, TimeUnit.MILLISECONDS); // TODO Changer pour un systeme non blockant avec les évents d'ouverture d'inventaire !
				} else if (st.lastCellMoved != null) {
					Roads.notifyCantUse(st.lastCellMoved.getFirst(), st.lastCellMoved.getSecond());
					LOGGER.debug("On notify cantUse " + st.lastCellMoved);
					st.lastCellMoved = null;
				}
				LOGGER.debug("on move sur " + cl.getId());
				perso.getPerso().moveToCell(cl.getId(), true, true, false);
				return;
			}
			if (st.goToBank) {
				LOGGER.debug("on goto bank");
				goToBankMap(perso, map, st);
				return;
			}
			if (st.needToGo != null) {
				if (map.isOnCoords(st.needToGo.x, st.needToGo.y)) {
					LOGGER.debug("On est arrivé !");
					st.needToGo = null;
					st.lastBlockedMap.clear();
				} else {
					LOGGER.debug("goto nextMap");
					goToNextMap(perso, map, st);
					return;
				}
			}
			if (!harvestRessource(perso, map, st)) {
				LOGGER.debug("onMap plus de ressource goto next");
				goToNextMap(perso, map, st);
			}
		} catch (final Exception e) {
			LOGGER.error(e);
		}
	}

	public void goToNextMap(final BotPerso perso, final ManchouMap map, final BotState st) {
		LOGGER.debug("go to next map method");
		if (st.path.isEmpty()) {
			LOGGER.debug("PATH EMPTY return");
			new PathEndEvent(perso, st).send();
			return;
		}
		if (st.needToGo == null) {
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
		if (path == null) throw new NullPointerException("Unable to find path from [" + map.getX() + "," + map.getY() + "] to [" + next.x + "," + next.y + "]");
		if (path.size() == 1) {
			st.needToGo = null;
			LOGGER.debug("path size = 1 !! " + path);
			goToNextMap(perso, map, st);
			return;
		}
		LOGGER.debug("path pour aller a la next ressource map " + path);
		final Point point = path.get(1);
		LOGGER.debug("prochain point " + point);
		Orientation dir = Pathfinding.getDirectionForMap(map.getX(), map.getY(), point.x, point.y);
		if (dir == null) throw new NullPointerException("Unable to find a direction to move from [" + map.getX() + "," + map.getY() + "] to [" + point.x + "," + point.y + "]");
		st.hasChangedMap = false;
		LOGGER.debug("st haschangedmap = false");
		LOGGER.debug("direction pour bouger= " + dir);
		Pair<Long, ManchouCell> timeToTravel = perso.changeMapWithDirection(dir);
		if (timeToTravel.getFirst() == -1L) { // éssai sans diagonales
			LOGGER.debug("impossible d'aller dans la direction " + dir);
			LOGGER.debug("On change de map sans diagonals");
			dir = dir.getNearestNeighborWithoutDiagonal();
			timeToTravel = perso.changeMapWithDirection(dir);
		}
		if (timeToTravel.getFirst() == -1L) { // éssai random
			LOGGER.debug("impossible d'aller dans la direction " + dir);
			st.lastBlockedMap.add(map.toPoint());
			LOGGER.debug("on est blocké sur " + map.getCoordsInfos());
			LOGGER.debug("On change de map random");
			timeToTravel = perso.changeMap();
		}
		if (timeToTravel.getFirst() == -1L) { // echec
			LOGGER.debug("le changeMap a échoué retry dans 15s");
			Executors.SCHEDULED.schedule(Threads.threadContextSwitch("GoToNextMap-" + perso.getPerso().getPseudo(), () -> goToNextMap(perso, map, st)), 15, TimeUnit.SECONDS);// Retest des que les mobs bougent ! TODO
			perso.getPerso().sit(true);
			Threads.uSleep(500, TimeUnit.MILLISECONDS);
			perso.getPerso().sendSmiley(Smiley.SUEUR);
			return;
		}
		final ManchouCell celltogo = timeToTravel.getSecond();
		LOGGER.debug("celltogo = " + celltogo.toPoint());
		Executors.SCHEDULED.schedule(Threads.threadContextSwitch("BadTp", () -> {
			final ManchouMap mm = perso.getPerso().getMap();
			if (!st.hasChangedMap && mm.isOnCoords(map.getX(), map.getY())) {
				LOGGER.error("IMPOSSIBLE D'USE CE TP (" + celltogo + "), ON NOTE ET ON RETENTE");
				Roads.notifyCantUse(mm, celltogo.getId());
				goToNextMap(perso, map, st);
			}
		}), (long) (timeToTravel.getFirst() * 2), TimeUnit.MILLISECONDS);
	}

	public void goToBankMap(final BotPerso perso, final ManchouMap map, final BotState st) {
		final Point coords = perso.getNearestBank().getCoords();
		if (map.isOnCoords(coords.x, coords.y)) return;
		//Pathfinding.getm
	}

	private String nameObject(final Item o) {
		return "x" + o.getAmount() + " " + o.getName();
	}

	public void depositBank(final BotPerso perso) {
		final Set<Item> inv = perso.getPerso().getInventory().getContents().values().stream().filter(e -> {
			if (ArrayUtils.contains(e.getTypeId(), perso.getItemsToKeep())) return false;
			if (e.getCategory() == ItemCategory.QUESTOBJECT || e.getCategory() == ItemCategory.QUEST) return false;
			return true;
		}).collect(Collectors.toSet());
		final MovedItem[] array = inv.stream().map(it -> new MovedItem(ExchangeMove.ADD, it.getUUID(), it.getAmount())).toArray(MovedItem[]::new);
		Arrays.stream(array).forEach(i -> {
			perso.getPerso().moveItem(i);
			Threads.uSleep(250, TimeUnit.MILLISECONDS);
		});
		LOGGER.info(AnsiColor.GREEN + "à déposé : " + inv.stream().map(this::nameObject).collect(Collectors.joining(",", "[", "]")) + " en banque !");
		Threads.uSleep(1, TimeUnit.SECONDS);
		final int kamas = perso.getPerso().getInventory().getKamas();
		final int bankK = perso.getPerso().getAccount().getBank().getKamas();
		int tomove;
		if (kamas < 10_000) {
			tomove = 10_000 - kamas;
			if (tomove > bankK) tomove = bankK;
			tomove = -tomove;
		} else tomove = kamas - 10_000;
		perso.getPerso().moveKama(tomove);
		perso.getPerso().exchangeLeave();
	}

	public boolean harvestRessource(final BotPerso perso, final ManchouMap map, final BotState st) { // return false si plus de ressource
		final ManchouJob job = perso.getPerso().getJob();
		LOGGER.debug("harvestRessource for job " + job.getType());
		if (job == null) throw new NullPointerException("No job");
		for (final ManchouCell c : perso.getPerso().getMap().getCells()) {
			if (!c.isInterractable()) continue;
			final Interractable i = c.getInterractable();
			if (c.isRessource() && st.ressources.contains(i) && c.isRessourceSpawned()) {
				final Skills skill = InterractUtil.getSkillFor(i, job.getType());
				if (skill == null || skill.getMinLvlToUse() > job.getLvl()) continue;
				final Node[] neighbors = Pathfinding.getNeighbors(new Node(c.getX(), c.getY()));
				for (final Node n : neighbors) {
					if (!Maps.isInMapRotated(n.getX(), n.getY(), map.getWidth(), map.getHeight())) continue;
					final ManchouCell manchouCell = map.getCells()[Maps.getIdRotated(n.getX(), n.getY(), map.getWidth(), map.getHeight())];
					LOGGER.debug(n + " = " + manchouCell.toPoint());
					if (!manchouCell.isWalkeable() || manchouCell.isTeleporter() || manchouCell.hasMobOn()) continue;
					final Cell[] protocolCells = map.getProtocolCells();
					final List<Point> cpath = Pathfinding.getCellPath(perso.getPerso().getCellId(), manchouCell.getId(), protocolCells,
							map.getWidth(),
							map.getHeight(), Pathfinding::getNeighbors,
							perso.getPerso()::canGoOnCellAvoidingMobs);
					if (cpath == null) continue;
					st.currentRessource = c.getId();
					LOGGER.debug("harvest ress " + st.currentRessource);
					perso.getPerso().move(cpath, false);
					final float time = Pathfinding.getPathTime(cpath, protocolCells, map.getWidth(), map.getHeight(), false) * 30;
					Executors.SCHEDULED.schedule(Threads.threadContextSwitch("Harvesting-" + perso.getPerso().getPseudo(), () -> {
						if (st.currentRessource == c.getId()) perso.getPerso().interract(skill, c.getId());
					}), (long) time, TimeUnit.MILLISECONDS);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @return the instance
	 */
	public static HarvestListener getInstance() {
		return instance;
	}

}
