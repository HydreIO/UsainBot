package fr.aresrpg.eratz.domain.listener;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.event.*;
import fr.aresrpg.commons.domain.log.AnsiColors.AnsiColor;
import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.structures.*;
import fr.aresrpg.dofus.structures.server.ServerState;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.ChatUtilities;
import fr.aresrpg.eratz.domain.ia.Interrupt;
import fr.aresrpg.eratz.domain.util.BotConfig;
import fr.aresrpg.tofumanchou.domain.data.enums.Zaap;
import fr.aresrpg.tofumanchou.domain.event.*;
import fr.aresrpg.tofumanchou.domain.event.aproach.InfoMessageEvent;
import fr.aresrpg.tofumanchou.domain.event.aproach.LoginErrorEvent;
import fr.aresrpg.tofumanchou.domain.event.chat.ChatMsgEvent;
import fr.aresrpg.tofumanchou.domain.event.duel.DuelRequestEvent;
import fr.aresrpg.tofumanchou.domain.event.entity.EntityPlayerJoinMapEvent;
import fr.aresrpg.tofumanchou.domain.event.entity.MonsterGroupSpawnEvent;
import fr.aresrpg.tofumanchou.domain.event.exchange.ExchangeAcceptedEvent;
import fr.aresrpg.tofumanchou.domain.event.group.GroupInvitationAcceptedEvent;
import fr.aresrpg.tofumanchou.domain.event.map.FrameUpdateEvent;
import fr.aresrpg.tofumanchou.domain.event.map.HarvestTimeReceiveEvent;
import fr.aresrpg.tofumanchou.domain.event.player.ZaapGuiOpenEvent;
import fr.aresrpg.tofumanchou.domain.event.player.ZaapUseErrorEvent;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.ManchouPerso;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
		perso.getMind().handleState(Interrupt.MOVED, true);
	}

	@Subscribe
	public void onReq(DuelRequestEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null || !perso.isOnline()) return;
		if (e.getTarget().getUUID() == perso.getPerso().getUUID()) perso.getMind().handleState(Interrupt.DEFI, false);
	}

	@Subscribe
	public void onParty(GroupInvitationAcceptedEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null || !perso.isOnline() || !e.getInvited().equalsIgnoreCase(perso.getPerso().getPseudo())) return;
		if (perso.getGroup() != null && e.getInviter().equalsIgnoreCase(perso.getGroup().getBoss().getPerso().getPseudo())) perso.getPerso().acceptGroupInvitation(true);
		else perso.getMind().handleState(Interrupt.GROUP, false);
	}

	@Subscribe
	public void guild(GuildInvitedEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null || !perso.isOnline()) return;
		perso.getMind().handleState(Interrupt.GUILD, false);
	}

	@Subscribe
	public void exchange(ExchangeAcceptedEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null || !perso.isOnline()) return;
		perso.getMind().handleState(Interrupt.EXCHANGE, false);
	}

	@Subscribe
	public void onDisconnect(BotDisconnectEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		perso.getMind().handleState(Interrupt.DISCONNECT, false);
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

	@Subscribe
	public void onBan(LoginErrorEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso != null) perso.getMind().handleState(Interrupt.LOGIN_ERROR, false);
	}

	@Subscribe
	public void onServer(ServerStateEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso != null && e.getServer().getState() != ServerState.ONLINE) perso.getMind().handleState(e.getServer().getState() == ServerState.SAVING ? Interrupt.SAVE : Interrupt.CLOSED, false);
	}

	@Subscribe
	public void onZaapError(ZaapUseErrorEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null || !perso.isOnline()) return;
		// on clear les zaap pour ne plus les utiliser
		if (perso.getPerso().getInventory().getKamas() < 1000) {
			LOGGER.debug("CLEAR ZAAP !!");
			perso.getUtilities().getZaaps().clear(); // TODO faire un systeme qui attend un event de refill de kamas pour set la liste a null (comme ça il retournera au zaap pour l'init lors du prochain move)
		}
		Executors.SCHEDULED.schedule(() -> {
			perso.getPerso().leaveZaap();
			Threads.uSleep(500, TimeUnit.MILLISECONDS);
			perso.getMind().handleState(Interrupt.MOVED, false); // on vient d'enregistrer les zaap donc on peut sortir (comme il n'est pas sur la bonne map le path sera recalculé)
		}, 500, TimeUnit.MILLISECONDS);
	}

	@Subscribe
	public void onZaap(ZaapGuiOpenEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null || !perso.isOnline()) return;
		// si jamais il n'a pas de kama il ne va pas réouvrir le gui donc il ne va pas reset ses zaap en boucle
		perso.getUtilities().setZaaps(Arrays.stream(e.getWaypoints()).map(w -> Zaap.getWithMap(w.getId())).collect(Collectors.toSet()));
	}

	@Subscribe
	public void onError(ActionErrorEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null || !perso.isOnline() || perso.isInFight() || perso.getUtilities().getPodsPercent() > 99) return;
		Threads.uSleep(1, TimeUnit.SECONDS);
		ManchouPerso p = perso.getPerso();
		if (perso.isInFight()) return;
		perso.getMind().handleState(Interrupt.ACTION_STOP, false);
	}

	@Subscribe
	public void onSpawn(MonsterGroupSpawnEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null || !perso.isOnline()) return;
		perso.getMind().handleState(Interrupt.MOB_SPAWN, false);
	}

	@Subscribe
	public void podBlocked(InfoMessageEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null || !perso.isOnline()) return;
		if ((e.getType() == InfosMsgType.ERROR && InfosMessage.TROP_CHARGE_.getId() == e.getMessageId())) perso.getMind().handleState(Interrupt.FULL_POD, false);
	}

	@Subscribe
	public void onSteal(HarvestTimeReceiveEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null || !perso.isOnline()) return;
		boolean steal = e.getCellId() == perso.getUtilities().getCurrentHarvest() && e.getPlayer().getUUID() != perso.getPerso().getUUID();
		LOGGER.debug(e.getPlayer().getPseudo() + " Harvestime ! steal = " + steal);
		if (steal) perso.getMind().handleState(Interrupt.RESSOURCE_STEAL, false);
	}

	@Subscribe
	public void onFrame(final FrameUpdateEvent e) {
		final BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null || !perso.isOnline()) return;
		if (e.getCell().getId() != perso.getUtilities().getCurrentHarvest()) {
			if (e.getCell().isLayerObject2Interactive()) perso.getMind().handleState(Interrupt.RESSOURCE_SPAWN, false);
			return;
		}
		if (e.getFrame() == 3) {
			perso.getMind().handleState(perso.getUtilities().isFullPod() ? Interrupt.FULL_POD : Interrupt.RESSOURCE_HARVESTED, false);
		}
	}

}
