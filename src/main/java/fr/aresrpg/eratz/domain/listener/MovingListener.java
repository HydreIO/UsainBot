package fr.aresrpg.eratz.domain.listener;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.event.*;
import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.Interrupt;
import fr.aresrpg.tofumanchou.domain.data.enums.Zaap;
import fr.aresrpg.tofumanchou.domain.event.entity.EntityPlayerJoinMapEvent;
import fr.aresrpg.tofumanchou.domain.event.player.ZaapGuiOpenEvent;
import fr.aresrpg.tofumanchou.domain.event.player.ZaapUseErrorEvent;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public class MovingListener implements Listener {

	private static final MovingListener instance = new MovingListener();
	private static List<Pair<EventBus, Subscriber>> subs = new ArrayList<>();

	private MovingListener() {
	}

	public static void register() {
		try {
			subs = Events.register(instance);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void unRegister() {
		subs.forEach(p -> p.getFirst().unsubscribe(p.getSecond()));
	}

	@Subscribe
	public void onMap(EntityPlayerJoinMapEvent e) {
		LOGGER.success("MOVED");
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null || e.getPlayer().getUUID() != perso.getPerso().getUUID()) return;
		LOGGER.success("WAIT UNTIL");
		perso.getUtilities().waitUntilMapUpdate();
		LOGGER.success("WAITED");
		perso.getMind().forEachState(c -> c.accept(perso.getUtilities().isOnPath() ? Interrupt.MOVED : Interrupt.OUT_OF_PATH));
	}

	@Subscribe
	public void onZaapError(ZaapUseErrorEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		// on clear les zaap pour ne plus les utiliser
		perso.getUtilities().getZaaps().clear(); // TODO faire un systeme qui attend un event de refill de kamas pour set la liste a null (comme ça il retournera au zaap pour l'init lors du prochain move)
		Executors.SCHEDULED.schedule(() -> {
			perso.getPerso().leaveZaap();
			Threads.uSleep(500, TimeUnit.MILLISECONDS);
			perso.getMind().forEachState(c -> c.accept(Interrupt.OUT_OF_PATH)); // on vient d'enregistrer les zaap donc on peut recalculer le path
		}, 500, TimeUnit.MILLISECONDS);
	}

	@Subscribe
	public void onZaap(ZaapGuiOpenEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		if (perso.getUtilities().getZaaps() == null) perso.getUtilities().setZaaps(Arrays.stream(e.getWaypoints()).map(w -> Zaap.getWithMap(w.getId())).collect(Collectors.toSet()));
	}
}
