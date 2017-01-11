package fr.aresrpg.eratz.domain.listener;

import fr.aresrpg.commons.domain.event.*;
import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.dofus.structures.InfosMessage;
import fr.aresrpg.dofus.structures.InfosMsgType;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.tofumanchou.domain.data.entity.Entity;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;
import fr.aresrpg.tofumanchou.domain.event.aproach.InfoMessageEvent;
import fr.aresrpg.tofumanchou.domain.event.entity.EntityTurnStartEvent;
import fr.aresrpg.tofumanchou.domain.event.fight.FightJoinEvent;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
			Executors.SCHEDULED.schedule(perso.getPerso()::blockSpec, 1, TimeUnit.SECONDS);
		}
	}

	@Subscribe
	public void onJoin(FightJoinEvent e) {
		if (true) return;
		BotPerso p = BotFather.getPerso(e.getClient());
		if (p != null) p.getPerso().blockCombat();
		Executors.SCHEDULED.schedule(() -> {
			BotPerso perso = BotFather.getPerso(e.getClient());
			perso.getPerso().beReady(true);
		}, 2, TimeUnit.SECONDS);
	}

	@Subscribe
	public void onTurn(EntityTurnStartEvent e) {
		if (true) return;

		Entity entity = e.getEntity();
		if (!(entity instanceof Perso)) return;
		BotPerso perso = BotFather.getPerso(entity.getUUID());
		if (perso == null) return;
		//Executors.SCHEDULED.schedule(() -> playTurn(perso), 1, TimeUnit.SECONDS);
	}

	/*
	 * public void playTurn(BotPerso perso) {
	 * try {
	 * ManchouSpell boost = (ManchouSpell) perso.getPerso().getSpells().get(Spells.TIR_ELOIGNEE);
	 * boost.decrementRelance();
	 * if (boost.getRelance() == 0) perso.getPerso().launchSpell(boost, 5, perso.getPerso().getCellId());
	 * ManchouSpell atk = (ManchouSpell) perso.getPerso().getSpells().get(Spells.FLECHE_MAGIQUE);
	 * ManchouCell persoC = perso.getPerso().getCell();
	 * Entity nearestEnnemy = perso.getNearestEnnemy();
	 * LOGGER.debug("nearest Ennemy = " + nearestEnnemy.getLife() + " , " + nearestEnnemy.getCellId() + " id=" + nearestEnnemy.getUUID());
	 * if (perso.hasMaxPoFor(atk, nearestEnnemy.getCellId())) { // si po
	 * LOGGER.debug("has max po for");
	 * if (persoC.distanceManathan(nearestEnnemy.getCellId()) < 2) atk = (ManchouSpell) perso.getPerso().getSpells().get(Spells.FLECHE_DE_RECUL);
	 * perso.getPerso().launchSpell(atk, 0, nearestEnnemy.getCellId());
	 * } else { // si pas po
	 * ManchouCell cellToTargetMob = perso.getCellToTargetMob(perso.getPerso().getPm(), nearestEnnemy.getCellId(), perso.getMaxPoFor(atk), false);
	 * LOGGER.debug("cell line = " + cellToTargetMob);
	 * if (cellToTargetMob == null) {
	 * LOGGER.debug("run to mob");
	 * perso.runToMob(nearestEnnemy, false, perso.getPerso().getPm());
	 * Threads.uSleep(1, TimeUnit.SECONDS);
	 * } else {
	 * LOGGER.debug("run to " + cellToTargetMob.getId());
	 * perso.runTo(cellToTargetMob.getId());
	 * Threads.uSleep(1, TimeUnit.SECONDS);
	 * LOGGER.debug("Atk");
	 * perso.getPerso().launchSpell(atk, 0, nearestEnnemy.getCellId());
	 * }
	 * }
	 * fallBack(perso);
	 * } catch (Exception e) {
	 * LOGGER.error(e);
	 * }
	 * }
	 * public void fallBack(BotPerso perso) {
	 * LOGGER.debug("Run away");
	 * perso.runAwayFromMobs(); // si il reste des pm alors le perso va fuir, si jamais il est trop loin il n'aura plus de pm car il aura déja rush le mob
	 * Threads.uSleep(1, TimeUnit.SECONDS);
	 * perso.getPerso().endTurn();
	 * }
	 */

	/**
	 * @return the instance
	 */
	public static FightListener getInstance() {
		return instance;
	}

}
