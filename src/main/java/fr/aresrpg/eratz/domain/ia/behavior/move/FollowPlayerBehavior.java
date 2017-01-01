package fr.aresrpg.eratz.domain.ia.behavior.move;

import static fr.aresrpg.eratz.domain.BotFather.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.event.*;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.protocol.party.client.PartyFollowPacket;
import fr.aresrpg.dofus.structures.game.FightSpawn;
import fr.aresrpg.eratz.domain.data.dofus.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.state.PlayerState;
import fr.aresrpg.eratz.domain.event.BotMoveEvent;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class FollowPlayerBehavior extends Behavior implements Listener {

	private int toFollow;
	private Queue<Integer> pos = new LinkedList<>();

	/**
	 * @param perso
	 */
	public FollowPlayerBehavior(BotPerso perso, int toFollow) {
		super(perso);
		this.toFollow = toFollow;
		try {
			Events.register(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Subscribe
	public void onMove(BotMoveEvent e) {
		if (e.getPlayerId() != toFollow) return;
		if (e.isTeleport()) pos.add(e.getCellId());
	}

	public boolean isFollowing() {
		return getPerso().getState() == PlayerState.FOLLOWING;
	}

	public void startFollow() {
		PartyFollowPacket pkt = new PartyFollowPacket();
		pkt.setPlayerId(0);
		pkt.setFollow(true);
		getPerso().sendPacketToServer(pkt);
	}

	public boolean isOnSameMap() {
		BotMap map = getPerso().getMapInfos().getMap();
		Point ff = getPerso().getBotInfos().getFollowedCoords();
		if (map == null || ff == null) return true;
		return map.getX() == ff.x && map.getY() == ff.y;
	}

	public void goToFollowedMap() {
		if (isOnSameMap()) return;
		LOGGER.debug("En déplacement !");
		Point ff = getPerso().getBotInfos().getFollowedCoords();
		getPerso().getNavigation().joinCoords(ff.x, ff.y);
	}

	public void walk() {
		while (!pos.isEmpty()) {
			Threads.uSleep(Randoms.nextBetween(100, 1500), TimeUnit.MILLISECONDS);
			getPerso().getNavigation().moveToCell(pos.poll(), true);
		}
	}

	public void joinFight() {
		int fightId = -1;
		for (BotPerso s : getPerso().getGroup().getMembers())
			if (hasFight(s.getId())) fightId = s.getId();
		if (fightId == -1) return;
		getPerso().getAbilities().getFightAbility().joinFight(fightId);
		Threads.uSleep(2, TimeUnit.SECONDS);
	}

	public boolean hasFight(int id) {
		for (FightSpawn f : getPerso().getFightInfos().getFightsOnMap())
			if (f.getId() == id) return true;
		return false;
	}

	public void waitUntilFightSpawnOrFollowedMove() {
		getPerso().setState(PlayerState.IDLE);
		while (pos.isEmpty() && getPerso().getFightInfos().getFightsOnMap().isEmpty()) {
			if (!isOnSameMap()) goToFollowedMap();
			Threads.uSleep(250, TimeUnit.MILLISECONDS); // gentil cpu ! pas cramer !
		}
		getPerso().setState(PlayerState.FOLLOWING);
	}

	@Override
	public BehaviorStopReason start() {
		getPerso().setState(PlayerState.FOLLOWING);
		goToFollowedMap();
		while (isFollowing()) {
			waitUntilFightSpawnOrFollowedMove();
			walk();
			joinFight();
		}
		getPerso().setState(PlayerState.IDLE);
		return BehaviorStopReason.FINISHED;
	}

}
