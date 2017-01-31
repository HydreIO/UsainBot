package fr.aresrpg.eratz.domain.listener;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.event.*;
import fr.aresrpg.commons.domain.log.AnsiColors.AnsiColor;
import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.ChatUtilities;
import fr.aresrpg.eratz.domain.util.BotConfig;
import fr.aresrpg.tofumanchou.domain.event.chat.ChatMsgEvent;
import fr.aresrpg.tofumanchou.domain.event.entity.EntityPlayerJoinMapEvent;
import fr.aresrpg.tofumanchou.domain.event.group.GroupInvitationAcceptedEvent;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class IaListener implements Listener {

	private static final IaListener instance = new IaListener();
	private static List<Pair<EventBus, Subscriber>> subs = new ArrayList<>();

	private IaListener() {
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

	static int count = 0;

	@Subscribe
	public void onMap(EntityPlayerJoinMapEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null || !perso.isOnline() || e.getPlayer().getUUID() != perso.getPerso().getUUID() || perso.isInFight()) return;
		perso.getUtilities().useRessourceBags();
		LOGGER.debug("Joined " + perso.getPerso().getMap().getInfos());
		perso.getPerso().cancelRunner();
	}

	@Subscribe
	public void onParty(GroupInvitationAcceptedEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null || !perso.isOnline() || !e.getInvited().equalsIgnoreCase(perso.getPerso().getPseudo())) return;
		if (perso.getGroup() != null && e.getInviter().equalsIgnoreCase(perso.getGroup().getBoss().getPerso().getPseudo())) perso.getPerso().acceptGroupInvitation(true);
	}

	@Subscribe
	public void onpm(ChatMsgEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null || !perso.isOnline()) return;
		LOGGER.debug((e.getChat() == Chat.PM_RECEIVE ? AnsiColor.RED : AnsiColor.WHITE) + "[" + e.getChat() + "][" + e.getPseudo() + "]: " + e.getMsg());
		if (e.getChat() != Chat.PRIVATE && e.getChat() != Chat.PM_RECEIVE) return;
		if (!BotConfig.AUTO_SPEAK) return;
		ChatUtilities cu = perso.getChatUtilities();
		if (cu.hasSpeaked(10, e.getPseudo())) return;
		Executors.FIXED.execute(() -> {
			Threads.uSleep(Randoms.nextBetween(6, 8), TimeUnit.SECONDS);
			cu.notifySpeak(e.getPseudo());
			perso.getPerso().sendPm(e.getPseudo(), cu.getResponse(e.getMsg()));
		});
	}

}
