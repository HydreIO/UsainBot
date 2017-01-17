package fr.aresrpg.eratz.domain.listener;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.event.*;
import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.dofus.structures.InfosMessage;
import fr.aresrpg.dofus.structures.InfosMsgType;
import fr.aresrpg.dofus.structures.server.ServerState;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.Interrupt;
import fr.aresrpg.tofumanchou.domain.data.enums.Zaap;
import fr.aresrpg.tofumanchou.domain.event.*;
import fr.aresrpg.tofumanchou.domain.event.aproach.InfoMessageEvent;
import fr.aresrpg.tofumanchou.domain.event.aproach.LoginErrorEvent;
import fr.aresrpg.tofumanchou.domain.event.entity.EntityPlayerJoinMapEvent;
import fr.aresrpg.tofumanchou.domain.event.fight.FightEndEvent;
import fr.aresrpg.tofumanchou.domain.event.fight.FightJoinEvent;
import fr.aresrpg.tofumanchou.domain.event.map.FrameUpdateEvent;
import fr.aresrpg.tofumanchou.domain.event.map.HarvestTimeReceiveEvent;
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
		if (perso == null || e.getPlayer().getUUID() != perso.getPerso().getUUID() || perso.isInFight()) return;
		perso.getUtilities().useRessourceBags();
		LOGGER.debug("Joined " + perso.getPerso().getMap().getInfos());
		perso.getMind().handleState(Interrupt.MOVED);
	}

	@Subscribe
	public void onDisconnect(BotDisconnectEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		perso.getMind().handleState(Interrupt.DISCONNECT);
	}

	@Subscribe
	public void onCrash(ClientCrashEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		perso.getMind().handleState(Interrupt.DISCONNECT);
	}

	@Subscribe
	public void onFight(FightJoinEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		Executors.SCHEDULED.schedule(() -> {
			FightListener.register();
			FightListener.getInstance().onJoin(e);
		}, 1, TimeUnit.SECONDS);
		perso.getMind().handleState(Interrupt.FIGHT_JOIN);
	}

	@Subscribe
	public void onFightEnd(FightEndEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		Executors.FIXED.execute(FightListener::unRegister);
	}

	@Subscribe
	public void onBan(LoginErrorEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso != null) perso.getMind().handleState(Interrupt.LOGIN_ERROR);
	}

	@Subscribe
	public void onServer(ServerStateEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso != null) perso.getMind().handleState(e.getServer().getState() == ServerState.SAVING ? Interrupt.SAVE : Interrupt.CLOSED);
	}

	@Subscribe
	public void onZaapError(ZaapUseErrorEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		// on clear les zaap pour ne plus les utiliser
		if (perso.getPerso().getInventory().getKamas() < 1000) {
			LOGGER.debug("CLEAR ZAAP !!");
			perso.getUtilities().getZaaps().clear(); // TODO faire un systeme qui attend un event de refill de kamas pour set la liste a null (comme ça il retournera au zaap pour l'init lors du prochain move)
		}
		Executors.SCHEDULED.schedule(() -> {
			perso.getPerso().leaveZaap();
			Threads.uSleep(500, TimeUnit.MILLISECONDS);
			perso.getMind().handleState(Interrupt.MOVED); // on vient d'enregistrer les zaap donc on peut sortir (comme il n'est pas sur la bonne map le path sera recalculé)
		}, 500, TimeUnit.MILLISECONDS);
	}

	@Subscribe
	public void onZaap(ZaapGuiOpenEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		// si jamais il n'a pas de kama il ne va pas réouvrir le gui donc il ne va pas reset ses zaap en boucle
		perso.getUtilities().setZaaps(Arrays.stream(e.getWaypoints()).map(w -> Zaap.getWithMap(w.getId())).collect(Collectors.toSet()));
	}

	@Subscribe
	public void onError(ActionErrorEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null || perso.isInFight() || perso.getUtilities().getPodsPercent() > 99) return;
		perso.getMind().handleState(Interrupt.ACTION_STOP);
	}

	@Subscribe
	public void podBlocked(InfoMessageEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		if ((e.getType() == InfosMsgType.ERROR && InfosMessage.TROP_CHARGE_.getId() == e.getMessageId())
				|| (e.getType() == InfosMsgType.INFOS && InfosMessage.RECOLTE_LOST_FULL_POD.getId() == e.getMessageId())) {
			perso.getMind().handleState(Interrupt.FULL_POD);
		}
	}

	@Subscribe
	public void onSteal(HarvestTimeReceiveEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		boolean steal = e.getCellId() == perso.getUtilities().getCurrentHarvest() && e.getPlayer().getUUID() != perso.getPerso().getUUID();
		LOGGER.debug(e.getPlayer().getPseudo() + " Harvestime ! steal = " + steal);
		if (steal) perso.getMind().handleState(Interrupt.RESSOURCE_STEAL);
	}

	@Subscribe
	public void onFrame(final FrameUpdateEvent e) {
		final BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		if (e.getCell().getId() != perso.getUtilities().getCurrentHarvest()) {
			if (e.getCell().isLayerObject2Interactive()) perso.getMind().handleState(Interrupt.RESSOURCE_SPAWN);
			return;
		}
		if (e.getFrame() == 3) perso.getMind().handleState(Interrupt.RESSOURCE_HARVESTED);
	}

}
