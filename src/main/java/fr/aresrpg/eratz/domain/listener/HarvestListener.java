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
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.Roads;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.state.BotState;
import fr.aresrpg.eratz.domain.event.PathEndEvent;
import fr.aresrpg.eratz.domain.util.ComparatorUtil;
import fr.aresrpg.eratz.domain.util.InterractUtil;
import fr.aresrpg.tofumanchou.domain.data.Account;
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

	private Map<Account, BotState> states = new HashMap<>();

	public HarvestListener() {
		instance = this;
	}

	public static void register() {
		try {
			subs = Events.register(getInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void unRegister() {
		subs.forEach(p -> p.getFirst().unsubscribe(p.getSecond()));
	}

	public BotState get(Account account) {
		BotState mapState = states.get(account);
		if (mapState == null) states.put(account, mapState = new BotState());
		return mapState;
	}

	@Subscribe
	public void onFrame(FrameUpdateEvent e) {
		BotState st = get(e.getClient());
		boolean finishedHarvest = e.getCell().getId() == st.currentRessource && e.getFrame() == 3;
		BotPerso perso = BotFather.getPerso(e.getClient());
		ManchouMap map = (ManchouMap) e.getClient().getPerso().getMap();
		if (finishedHarvest && !st.goToBank && !harvestRessource(BotFather.getPerso(e.getClient()), (ManchouMap) e.getClient().getPerso().getMap(), st)) {
			goToNextMap(perso, map, st); // si il a finit d'harvest & qu'il ne doit pas aller a la banque et qu'il n'y a plus de ressources sur la map
		}
	}

	@Subscribe
	public void onFightEnd(FightEndEvent e) {
		Executors.SCHEDULED.schedule(() -> {
			ManchouPerso perso = (ManchouPerso) e.getClient().getPerso();
			BotPerso bp = BotFather.getPerso(perso);
			BotState st = bp.getBotState();
			if (!st.goToBank && !harvestRessource(bp, perso.getMap(), st)) goToNextMap(bp, perso.getMap(), st);
		} , 1, TimeUnit.SECONDS);
	}

	@Subscribe
	public void onRessourceStartHarvest(HarvestTimeReceiveEvent e) {
		BotState st = get(e.getClient());
		boolean steal = st.currentRessource == e.getCellId() && e.getClient().getPerso().getUUID() != e.getPlayer().getUUID();
		BotPerso perso = BotFather.getPerso(e.getClient());
		ManchouMap map = (ManchouMap) e.getClient().getPerso().getMap();
		if (steal && !st.goToBank && !harvestRessource(perso, map, st)) {
			st.needToGo = null; // si on lui a volé la ressource & qu'il ne doit pas aller a la banque et qu'il n'y a plus de ressources sur la map
			goToNextMap(perso, map, st);
		}
	}

	@Subscribe
	public void onMap(MapJoinEvent e) {
		if (e.getMap().isEnded()) Executors.SCHEDULED.schedule(() -> this.onMap(BotFather.getPerso(e.getClient()), (ManchouMap) e.getMap()), 1, TimeUnit.SECONDS);
	}

	private void onMap(BotPerso perso, ManchouMap map) {
		LOGGER.success("ON MAP ================= 1");
		BotState st = get(perso.getPerso().getAccount());
		LOGGER.success("ON MAP ================= 2");
		Roads.checkMap(map, perso); // enregistre les tp manquant etc
		LOGGER.success("ON MAP ================= 3 " + !st.canGoIndoor + " " + !map.isOutdoor());
		try {
			if (!st.canGoIndoor && !map.isOutdoor()) {
				LOGGER.success("ON MAP ================= 4");
				ManchouCell cl = perso.getPerso().getNearestTeleporters()[0];
				if (perso.isInBankMap()) {
					perso.getPerso().speakToNpc(-2);
					Threads.uSleep(200, TimeUnit.MILLISECONDS);
					perso.getPerso().npcTalkChoice(318, 259);
					Threads.uSleep(500, TimeUnit.MILLISECONDS);
					depositBank(perso);
					Threads.uSleep(500, TimeUnit.MILLISECONDS); // TODO Changer pour un systeme non blockant avec les évents d'ouverture d'inventaire !
				}
				LOGGER.success("ON MAP ================= 5");
				perso.getPerso().moveToCell(cl.getId(), true, true, false);
				LOGGER.success("ON MAP ================= 6");
				return;
			}
			if (st.goToBank) {
				goToBankMap(perso, map, st);
				return;
			}
			if (st.needToGo != null) {
				if (map.isOnCoords(st.needToGo.x, st.needToGo.y)) st.needToGo = null;
				else {
					goToNextMap(perso, map, st);
					return;
				}
			}
			if (!harvestRessource(perso, map, st)) goToNextMap(perso, map, st);
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	public void goToNextMap(BotPerso perso, ManchouMap map, BotState st) {
		if (st.path.isEmpty()) {
			new PathEndEvent(perso, st).send();
			return;
		}
		if (st.needToGo == null) {
			List<Point> list = st.path.stream().sorted((p1, p2) -> ComparatorUtil.comparingDistanceToPlayer(false, new Point(map.getX(), map.getY()), p1, p2)).collect(Collectors.toList());
			st.needToGo = list.remove(0);
			st.path.remove(st.needToGo);
		}
		Point next = st.needToGo;
		List<Point> path = Pathfinding.getPath(map.getX(), map.getY(), next.x, next.y, Roads::canMove, Pathfinding::getNeighborsWithoutDiagonals);
		if (path == null) throw new NullPointerException("Unable to find path from [" + map.getX() + "," + map.getY() + "] to [" + next.x + "," + next.y + "]");
		Point point = path.get(1);
		Orientation dir = Pathfinding.getDirectionForMap(map.getX(), map.getY(), point.x, point.y);
		if (dir == null) throw new NullPointerException("Unable to find a direction to move from [" + map.getX() + "," + map.getY() + "] to [" + point.x + "," + point.y + "]");
		Pair<Long, ManchouCell> timeToTravel = perso.changeMapWithDirection(dir);
		if (timeToTravel.getFirst() == -1L) timeToTravel = perso.changeMap();
		if (timeToTravel.getFirst() == -1L) Executors.SCHEDULED.schedule(() -> goToNextMap(perso, map, st), 1, TimeUnit.MINUTES);// Retest des que les mobs bougent ! TODO
		ManchouCell celltogo = timeToTravel.getSecond();
		Executors.SCHEDULED.schedule(() -> {
			ManchouMap mm = perso.getPerso().getMap();
			if (mm.isOnCoords(point.x, point.y)) {
				System.out.println("IMPOSSIBLE D'USE CE TP, ON NOTE ET ON RETENTE");
				Roads.notifyCantUse(mm, celltogo.getId());
				goToNextMap(perso, map, st);
			}
		} , (long) (timeToTravel.getFirst() * 2), TimeUnit.MILLISECONDS);
	}

	public void goToBankMap(BotPerso perso, ManchouMap map, BotState st) {
		Point coords = perso.getNearestBank().getCoords();
		if (map.isOnCoords(coords.x, coords.y)) return;
		//Pathfinding.getm
	}

	private String nameObject(Item o) {
		return "x" + o.getAmount() + " " + o.getName();
	}

	public void depositBank(BotPerso perso) {
		Set<Item> inv = perso.getPerso().getInventory().getContents().values().stream().filter(e -> {
			if (ArrayUtils.contains(e.getTypeId(), perso.getItemsToKeep())) return false;
			if (e.getCategory() == ItemCategory.QUESTOBJECT || e.getCategory() == ItemCategory.QUEST) return false;
			return true;
		}).collect(Collectors.toSet());
		MovedItem[] array = inv.stream().map(it -> new MovedItem(ExchangeMove.ADD, it.getUUID(), it.getAmount())).toArray(MovedItem[]::new);
		Arrays.stream(array).forEach(i -> {
			perso.getPerso().moveItem(i);
			Threads.uSleep(250, TimeUnit.MILLISECONDS);
		});
		LOGGER.info(AnsiColor.GREEN + "à déposé : " + inv.stream().map(this::nameObject).collect(Collectors.joining(",", "[", "]")) + " en banque !");
		Threads.uSleep(1, TimeUnit.SECONDS);
		int kamas = perso.getPerso().getInventory().getKamas();
		int bankK = perso.getPerso().getAccount().getBank().getKamas();
		int tomove = 0;
		if (kamas < 10_000) {
			tomove = 10_000 - kamas;
			if (tomove > bankK) tomove = bankK;
			tomove = -tomove;
		} else tomove = kamas - 10_000;
		perso.getPerso().moveKama(tomove);
		perso.getPerso().exchangeLeave();
	}

	private boolean harvestRessource(BotPerso perso, ManchouMap map, BotState st) { // return false si plus de ressource
		ManchouJob job = perso.getPerso().getJob();
		ManchouCell near = null;
		int persocell = perso.getPerso().getCellId();
		ManchouCell persocellc = map.getCells()[persocell];
		int x = persocellc.getX();
		int y = persocellc.getY();
		for (ManchouCell c : perso.getPerso().getMap().getCells()) {
			if (!c.isInterractable()) continue;
			Interractable i = c.getInterractable();
			if (c.isRessource() && st.ressources.contains(i)) {
				Skills skill = InterractUtil.getSkillFor(i, job.getType());
				if (skill == null || skill.getMinLvlToUse() > job.getLvl()) continue;
				Node[] neighbors = Pathfinding.getNeighbors(new Node(c.getX(), c.getY()));
				for (Node n : neighbors) {
					if (!Maps.isInMapRotated(n.getX(), n.getY(), map.getWidth(), map.getHeight())) continue;
					ManchouCell manchouCell = map.getCells()[Maps.getIdRotated(n.getX(), n.getY(), map.getWidth(), map.getHeight())];
					if (!manchouCell.isWalkeable() || manchouCell.isTeleporter() || manchouCell.hasMobOn()) continue;
					Cell[] protocolCells = map.getProtocolCells();
					List<Point> cpath = Pathfinding.getCellPath(Maps.getIdRotated(n.getX(), n.getY(), map.getWidth(), map.getHeight()), manchouCell.getId(), protocolCells, map.getWidth(),
							map.getHeight(), Pathfinding::getNeighbors,
							perso.getPerso()::canGoOnCellAvoidingMobs);
					if (cpath == null) continue;
					float time = Pathfinding.getPathTime(cpath, protocolCells, map.getWidth(), map.getHeight(), false) * 30;
					st.currentRessource = c.getId();
					Executors.SCHEDULED.schedule(() -> perso.getPerso().interract(skill, c.getId()), (long) time, TimeUnit.MILLISECONDS);
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
