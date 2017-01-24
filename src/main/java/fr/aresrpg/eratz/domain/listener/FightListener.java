package fr.aresrpg.eratz.domain.listener;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.event.*;
import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.dofus.structures.InfosMessage;
import fr.aresrpg.dofus.structures.InfosMsgType;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.Interrupt;
import fr.aresrpg.eratz.domain.util.BotConfig;
import fr.aresrpg.tofumanchou.domain.data.entity.Entity;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;
import fr.aresrpg.tofumanchou.domain.event.aproach.InfoMessageEvent;
import fr.aresrpg.tofumanchou.domain.event.entity.*;
import fr.aresrpg.tofumanchou.domain.event.fight.FightJoinEvent;
import fr.aresrpg.tofumanchou.domain.event.player.PersoStatsEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @since
 */
public class FightListener implements Listener {

	private static FightListener instance = new FightListener();
	private static List<Pair<EventBus, Subscriber>> subs = new ArrayList<>();

	private FightListener() {
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
	public void playerSpec(InfoMessageEvent e) {
		if (!BotConfig.FIGHT_ENABLED) return;
		if (e.getType() == InfosMsgType.INFOS && e.getMessageId() == InfosMessage.PLAYER_IN_SPEC.getId()) {
			BotPerso perso = BotFather.getPerso(e.getClient());
			//Executors.SCHEDULED.schedule(perso.getPerso()::blockSpec, 3, TimeUnit.SECONDS);
		}
	}

	@Subscribe
	public void onJoin(FightJoinEvent e) {
		LOGGER.debug("fight join");
		if (!BotConfig.FIGHT_ENABLED) return;
		BotPerso p = BotFather.getPerso(e.getClient());
		if (p != null) p.getPerso().blockToGroup();
		p.getUtilities().notifyJoinFight();
	}

	@Subscribe
	public void onTurn(EntityTurnStartEvent e) {
		if (!BotConfig.FIGHT_ENABLED) return;
		Entity entity = e.getEntity();
		if (!(entity instanceof Perso)) return;
		BotPerso perso = BotFather.getPerso(entity.getUUID());
		if (perso == null) return;
		perso.getMind().handleState(Interrupt.TURN_START, false);
	}

	@Subscribe
	public void onStat(PersoStatsEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		perso.getMind().handleState(Interrupt.STATS, true);
	}

	@Subscribe
	public void onStat(EntityPaChangeEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		perso.getMind().handleState(Interrupt.PA, true);
	}

	@Subscribe
	public void onStat(EntityPmChangeEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		perso.getMind().handleState(Interrupt.PM, true);
	}

	/**
	 * @return the instance
	 */
	public static FightListener getInstance() {
		return instance;
	}

}
