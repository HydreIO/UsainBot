package fr.aresrpg.eratz.domain.listener;

import fr.aresrpg.commons.domain.event.*;
import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.Interrupt;
import fr.aresrpg.tofumanchou.domain.event.entity.EntityPlayerJoinMapEvent;

import java.util.ArrayList;
import java.util.List;

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
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		perso.getMind().forEachState(c -> c.accept(perso.getUtilities().isOnPath() ? Interrupt.MOVED : Interrupt.OUT_OF_PATH));
	}
}
