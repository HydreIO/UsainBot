package fr.aresrpg.eratz.domain.listener;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.event.*;
import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.dofus.protocol.game.actions.GameActions;
import fr.aresrpg.dofus.protocol.game.actions.client.GameDuelAction;
import fr.aresrpg.dofus.protocol.game.actions.client.GameRefuseDuelAction;
import fr.aresrpg.dofus.protocol.game.client.GameClientActionPacket;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.tofumanchou.domain.event.entity.EntityPlayerJoinMapEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class AntiBotListener implements Listener {

	private static final AntiBotListener instance = new AntiBotListener();
	private static List<Pair<EventBus, Subscriber>> subs = new ArrayList<>();
	private Map<BotPerso, Set<Long>> uuids = new HashMap<BotPerso, Set<Long>>();

	private AntiBotListener() {
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

	public static void registerBotToCrash(BotPerso perso, long uuid) {
		Set<Long> set = instance.uuids.get(perso);
		if (set == null) instance.uuids.put(perso, set = new HashSet<>());
		LOGGER.success("Registering bot with uuid " + uuid);
		set.add(uuid);
	}

	@Subscribe
	public void onMap(EntityPlayerJoinMapEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		Set<Long> set = uuids.get(perso);
		if (set == null || !set.contains(e.getPlayer().getUUID())) return;
		LOGGER.success("Starting to crash " + e.getPlayer().getPseudo());
		crashBot(e.getPlayer().getUUID(), perso);
	}

	void crashBot(long uid, BotPerso bp) {
		int count = 0;
		while (++count < 120) {
			Threads.uSleep(50, TimeUnit.MILLISECONDS); // gentil cpu ! pas cramer !
			GameDuelAction action = new GameDuelAction(uid);
			GameClientActionPacket ga = new GameClientActionPacket(GameActions.DUEL, action);
			bp.sendPacketToServer(ga);
			Threads.uSleep(2, TimeUnit.MILLISECONDS);
			GameRefuseDuelAction actionr = new GameRefuseDuelAction();
			actionr.setTargetId(bp.getPerso().getUUID());
			bp.sendPacketToServer(new GameClientActionPacket(GameActions.REFUSE_DUEL, actionr));
		}
	}

}
