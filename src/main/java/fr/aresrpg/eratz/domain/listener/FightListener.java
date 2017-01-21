package fr.aresrpg.eratz.domain.listener;

import fr.aresrpg.commons.domain.event.*;
import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.dofus.structures.InfosMessage;
import fr.aresrpg.dofus.structures.InfosMsgType;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.util.ShadowCasting;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.Interrupt;
import fr.aresrpg.tofumanchou.domain.data.entity.Entity;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;
import fr.aresrpg.tofumanchou.domain.data.enums.Spells;
import fr.aresrpg.tofumanchou.domain.event.aproach.InfoMessageEvent;
import fr.aresrpg.tofumanchou.domain.event.entity.EntityMoveEvent;
import fr.aresrpg.tofumanchou.domain.event.entity.EntityTurnStartEvent;
import fr.aresrpg.tofumanchou.domain.event.fight.FightJoinEvent;
import fr.aresrpg.tofumanchou.domain.event.player.PersoStatsEvent;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
		if (e.getType() == InfosMsgType.INFOS && e.getMessageId() == InfosMessage.PLAYER_IN_SPEC.getId()) {
			BotPerso perso = BotFather.getPerso(e.getClient());
			Executors.SCHEDULED.schedule(perso.getPerso()::blockSpec, 3, TimeUnit.SECONDS);
		}
	}

	@Subscribe
	public void onJoin(FightJoinEvent e) {
		BotPerso p = BotFather.getPerso(e.getClient());
		if (p != null) p.getPerso().blockCombat();
		Executors.SCHEDULED.schedule(() -> {
			BotPerso perso = BotFather.getPerso(e.getClient());
			perso.getPerso().beReady(true);
		}, 2, TimeUnit.SECONDS);
	}

	@Subscribe
	public void onTurn(EntityTurnStartEvent e) {
		Entity entity = e.getEntity();
		if (!(entity instanceof Perso)) return;
		BotPerso perso = BotFather.getPerso(entity.getUUID());
		if (perso == null) return;
		perso.getMind().handleState(Interrupt.TURN_START, false);
	}

	@Subscribe
	public void onmo(EntityMoveEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null || e.getEntity().getUUID() != perso.getPerso().getUUID()) return;
		int maxPoFor = perso.getFightUtilities().getMaxPoFor(perso.getPerso().getSpells().get(Spells.FLECHE_MAGIQUE));
		Set<Cell> accesibleCells = ShadowCasting.getAccesibleCells(perso.getPerso().getCellId(), maxPoFor, perso.getPerso().getMap().serialize(), perso.getPerso().getMap().cellAccessible().negate());
		Iterator<Cell> iterator = accesibleCells.iterator();
		perso.getView().setAccessible(accesibleCells.stream().collect(Collectors.toList()), perso.getPerso().getCellId(), maxPoFor);
	}

	@Subscribe
	public void onStat(PersoStatsEvent e) {
		BotPerso perso = BotFather.getPerso(e.getClient());
		if (perso == null) return;
		perso.getMind().handleState(Interrupt.STATS, true);
	}

	/**
	 * @return the instance
	 */
	public static FightListener getInstance() {
		return instance;
	}

}
