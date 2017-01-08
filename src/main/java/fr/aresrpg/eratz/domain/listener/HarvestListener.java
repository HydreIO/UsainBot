package fr.aresrpg.eratz.domain.listener;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.event.*;
import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.dofus.structures.*;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.util.Maps;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.state.BotState;
import fr.aresrpg.eratz.domain.event.PathEndEvent;
import fr.aresrpg.eratz.domain.ia.path.Path;
import fr.aresrpg.eratz.domain.ia.path.Paths;
import fr.aresrpg.eratz.domain.util.InterractUtil;
import fr.aresrpg.tofumanchou.domain.event.aproach.InfoMessageEvent;
import fr.aresrpg.tofumanchou.domain.event.item.PodsUpdateEvent;
import fr.aresrpg.tofumanchou.domain.event.map.FrameUpdateEvent;
import fr.aresrpg.tofumanchou.domain.event.map.HarvestTimeReceiveEvent;
import fr.aresrpg.tofumanchou.domain.event.player.MapJoinEvent;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
	public void onFinish(final PathEndEvent e) {
		Executors.SCHEDULED.schedule(() -> {
			Paths currentPath = e.getPerso().getCurrentPath();
			if (currentPath == null) throw new NullPointerException("The bot doesn't have a path anymore !");
			Path path = currentPath.getPath();
			path.fillCoords(e.getPerso().getBotState().path);
			path.fillRessources(e.getPerso().getBotState().ressources);
			//	e.getPerso().goToNextMap();
		} , 3, TimeUnit.SECONDS);

	}

	@Subscribe
	public void onPod(PodsUpdateEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null || perso.getPodsPercent() < 95) return;
		//	perso.notifyNeedToGoBank();
	}

	@Subscribe
	public void podBlocked(InfoMessageEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		if ((e.getType() == InfosMsgType.ERROR && InfosMessage.TROP_CHARGE_.getId() == e.getMessageId())
				|| (e.getType() == InfosMsgType.INFOS && InfosMessage.RECOLTE_LOST_FULL_POD.getId() == e.getMessageId())) {
			perso.destroyHeaviestRessource();
			//	perso.notifyNeedToGoBank();
		}
	}

	@Subscribe
	public void onFrame(final FrameUpdateEvent e) {
		final BotPerso perso = BotFather.getPerso(e.getClient());
		final BotState st = perso.getBotState();
		final boolean finishedHarvest = e.getCell().getId() == st.currentRessource && e.getFrame() == 3;
		final ManchouMap map = (ManchouMap) e.getClient().getPerso().getMap();
		/*
		 * if (st.goToBank) perso.goToBankMap();
		 * else if (finishedHarvest && !st.goToBank && !harvestRessource(BotFather.getPerso(e.getClient()), (ManchouMap) e.getClient().getPerso().getMap(), st)) {
		 * st.needToGo = null;
		 * perso.goToNextMap(); // si il a finit d'harvest & qu'il ne doit pas aller a la banque et qu'il n'y a plus de ressources sur la map
		 * }
		 */
	}

	@Subscribe
	public void onRessourceStartHarvest(final HarvestTimeReceiveEvent e) {
		final BotPerso perso = BotFather.getPerso(e.getClient());
		final BotState st = perso.getBotState();
		final boolean steal = st.currentRessource == e.getCellId() && e.getClient().getPerso().getUUID() != e.getPlayer().getUUID();
		final ManchouMap map = (ManchouMap) e.getClient().getPerso().getMap();
		LOGGER.debug("STEAL = " + steal);
		/*
		 * if (steal && st.goToBank) perso.goToBankMap();
		 * else if (steal && !st.goToBank && !harvestRessource(perso, map, st)) {
		 * st.needToGo = null; // si on lui a volé la ressource & qu'il ne doit pas aller a la banque et qu'il n'y a plus de ressources sur la map
		 * perso.goToNextMap();
		 * }
		 */
	}

	@Subscribe
	public void onMap(final MapJoinEvent e) {
		LOGGER.debug("new map");
		BotPerso perso = BotFather.getPerso(e.getClient());
		//	if (perso != null && perso.getAntiblock() != null) perso.getAntiblock().cancel(true); // empeche le blockage d'une cellule pour rien 
		if (e.getMap().isEnded())
			Executors.SCHEDULED.schedule(Threads.threadContextSwitch("MapJoin", () -> this.onMap(BotFather.getPerso(e.getClient()), (ManchouMap) e.getMap())),
					1, TimeUnit.SECONDS);
	}

	private void onMap(final BotPerso perso, final ManchouMap map) {
		final BotState st = perso.getBotState();
		st.visitedMaps.add(map);
		LOGGER.debug("checking map");
		//	Roads.checkMap(map, perso); // enregistre les tp manquant etc
		LOGGER.debug("set has changed map true");
		if (perso.getPodsPercent() >= 95) {
			//	perso.notifyNeedToGoBank();
			return;
		}
		try {
			//		boolean canUseToTeleport = st.lastCellMoved == null ? true : Roads.canUseToTeleport(st.lastCellMoved.getFirst(), st.lastCellMoved.getSecond().getFirst());
			LOGGER.debug("rt");
			//		LOGGER.debug("lastmoved = " + st.lastCellMoved + " can use ? " + canUseToTeleport);
			if (!st.canGoIndoor && !map.isOutdoor()) {
				LOGGER.debug("Indoor !" + map.getCoordsInfos());
				final ManchouCell cl = perso.getPerso().getNearestTeleporters()[0];
				/*
				 * if (perso.isInBankMap()) {
				 * LOGGER.debug("in bank");
				 * perso.getPerso().speakToNpc(-2);
				 * Threads.uSleep(1, TimeUnit.SECONDS);
				 * perso.getPerso().npcTalkChoice(318, 259);
				 * Threads.uSleep(1, TimeUnit.SECONDS);
				 * perso.depositBank();
				 * Threads.uSleep(1, TimeUnit.SECONDS); // TODO Changer pour un systeme non blockant avec les évents d'ouverture d'inventaire !
				 * st.goToBank = false;
				 * } else if (st.lastCellMoved != null) {
				 * Roads.notifyCantUse(st.lastCellMoved.getFirst(), st.lastCellMoved.getSecond().getFirst());
				 * LOGGER.debug("On notify cantUse " + st.lastCellMoved);
				 * st.lastCellMoved = null;
				 * }
				 */
				LOGGER.debug("on move sur " + cl.getId());
				perso.getPerso().moveToCell(cl.getId(), true, true, false);
				return;
			}
			if (st.goToBank) {
				LOGGER.debug("on goto bank");
				//	perso.goToBankMap();
				return;
			}
			if (st.needToGo != null) {
				if (harvestRessource(perso, map, st)) return;
				if (map.isOnCoords(st.needToGo.x, st.needToGo.y)) {
					LOGGER.debug("On est arrivé !");
					st.needToGo = null;
					st.lastBlockedMap.clear();
				} else {
					LOGGER.debug("goto nextMap");
					//		perso.goToNextMap();
					return;
				}
			}
			if (!harvestRessource(perso, map, st)) {
				LOGGER.debug("onMap plus de ressource goto next");
				//		perso.goToNextMap();
			}
		} catch (final Exception e) {
			LOGGER.error(e);
		}
	}

	public boolean harvestRessource(final BotPerso perso, final ManchouMap map, final BotState st) { // return false si plus de ressource
		final ManchouJob job = perso.getPerso().getJob();
		if (job == null) throw new NullPointerException("No job");
		LOGGER.debug("harvestRessource for job " + job.getType());
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
