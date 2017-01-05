package fr.aresrpg.eratz.domain.listener;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.event.*;
import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.Roads;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.state.BotState;
import fr.aresrpg.tofumanchou.domain.event.fight.FightEndEvent;
import fr.aresrpg.tofumanchou.domain.event.player.MapJoinEvent;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class GotoListener implements Listener {

	private static GotoListener instance = new GotoListener();
	private static List<Pair<EventBus, Subscriber>> subs = new ArrayList<>();

	public GotoListener() {
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
	public void onFightEnd(final FightEndEvent e) {
		Executors.SCHEDULED.schedule(() -> {
			final ManchouPerso perso = (ManchouPerso) e.getClient().getPerso();
			final BotPerso bp = BotFather.getPerso(perso);
			final BotState st = bp.getBotState();
			st.needToGo = null;
			bp.goToNextMap();
		} , 1, TimeUnit.SECONDS);
	}

	@Subscribe
	public void onMap(final MapJoinEvent e) {
		LOGGER.debug("new map");
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso != null && perso.getAntiblock() != null) perso.getAntiblock().cancel(true); // empeche le blockage d'une cellule pour rien 
		if (e.getMap().isEnded())
			Executors.SCHEDULED.schedule(Threads.threadContextSwitch("MapJoin", () -> this.onMap(BotFather.getPerso(e.getClient()), (ManchouMap) e.getMap())),
					1, TimeUnit.SECONDS);
	}

	private void onMap(final BotPerso perso, final ManchouMap map) {
		final BotState st = perso.getBotState();
		LOGGER.debug("checking map");
		st.visitedMaps.add(map);
		Roads.checkMap(map, perso); // enregistre les tp manquant etc
		LOGGER.debug("set has changed map true");
		try {
			boolean canUseToTeleport = st.lastCellMoved == null ? true : Roads.canUseToTeleport(st.lastCellMoved.getFirst(), st.lastCellMoved.getSecond().getFirst());
			LOGGER.debug("rt");
			LOGGER.debug("lastmoved = " + st.lastCellMoved + " can use ? " + canUseToTeleport);
			if (!st.canGoIndoor && !map.isOutdoor()) {
				LOGGER.debug("Indoor !" + map.getCoordsInfos());
				final ManchouCell cl = perso.getPerso().getNearestTeleporters()[0];
				if (st.lastCellMoved != null) {
					Roads.notifyCantUse(st.lastCellMoved.getFirst(), st.lastCellMoved.getSecond().getFirst());
					LOGGER.debug("On notify cantUse " + st.lastCellMoved);
					st.lastCellMoved = null;
				}
				LOGGER.debug("on move sur " + cl.getId());
				perso.getPerso().moveToCell(cl.getId(), true, true, false);
				return;
			}
			if (st.needToGo != null) {
				if (map.isOnCoords(st.needToGo.x, st.needToGo.y)) {
					LOGGER.debug("On est arrivé !");
					st.needToGo = null;
					st.lastBlockedMap.clear();
					unRegister();
				} else {
					LOGGER.debug("goto nextMap");
					perso.goToNextMap();
					return;
				}
			}
		} catch (final Exception e) {
			LOGGER.error(e);
		}
	}

	/**
	 * @return the instance
	 */
	public static GotoListener getInstance() {
		return instance;
	}

}
