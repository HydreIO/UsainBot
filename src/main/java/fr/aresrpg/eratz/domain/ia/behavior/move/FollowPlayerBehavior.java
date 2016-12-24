package fr.aresrpg.eratz.domain.ia.behavior.move;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.dofus.protocol.party.client.PartyFollowPacket;
import fr.aresrpg.dofus.structures.game.FightSpawn;
import fr.aresrpg.eratz.domain.data.dofus.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.state.PlayerState;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;

import java.awt.Point;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class FollowPlayerBehavior extends Behavior {

	private int toFollow;

	/**
	 * @param perso
	 */
	public FollowPlayerBehavior(Perso perso, int toFollow) {
		super(perso);
		this.toFollow = toFollow;
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
		LOGGER.debug("Changement de map !");
		Point ff = getPerso().getBotInfos().getFollowedCoords();
		getPerso().getNavigation().joinCoords(ff.x, ff.y);
	}

	public void joinFight() {
		int fightId = -1;
		for (Perso s : getPerso().getGroup().getMembers())
			if (hasFight(s.getId())) fightId = s.getId();
		if (fightId == -1) return;
		getPerso().getAbilities().getFightAbility().joinFight(fightId);
		waitUntilFightEnd();
	}

	public boolean hasFight(int id) {
		for (FightSpawn f : getPerso().getFightInfos().getFightsOnMap())
			if (f.getId() == id) return true;
		return false;
	}

	public void waitUntilFightSpawnOrFollowedMove() {
		getPerso().setState(PlayerState.IDLE);
		while (getPerso().getFightInfos().getFightsOnMap().isEmpty() && isOnSameMap())
			Threads.uSleep(250, TimeUnit.MILLISECONDS); // gentil cpu ! pas cramer !
		getPerso().setState(PlayerState.FOLLOWING);
	}

	public void waitUntilFightEnd() {
		while (getPerso().isInFight())
			Threads.uSleep(250, TimeUnit.MILLISECONDS); // gentil cpu ! pas cramer !
	}

	@Override
	public BehaviorStopReason start() {
		getPerso().setState(PlayerState.FOLLOWING);
		while (isFollowing()) {
			waitUntilFightSpawnOrFollowedMove();
			joinFight();
			goToFollowedMap();
		}
		getPerso().setState(PlayerState.IDLE);
		return BehaviorStopReason.FINISHED;
	}

}
