package fr.aresrpg.eratz.domain.ia.behavior.move;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.dofus.protocol.party.client.PartyFollowPacket;
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

	private String toFollow;

	/**
	 * @param perso
	 */
	public FollowPlayerBehavior(Perso perso, String toFollow) {
		super(perso);
		this.toFollow = toFollow;
	}

	public boolean isFollowing() {
		return getPerso().getState() == PlayerState.FOLLOWING;
	}

	public void startFollow() {
		PartyFollowPacket pkt = new PartyFollowPacket();
		pkt.setPname(toFollow);
		pkt.setFollow(true);
		getPerso().sendPacketToServer(pkt);
	}

	public boolean isOnSameMap() {
		BotMap map = getPerso().getMapInfos().getMap();
		Point ff = getPerso().getBotInfos().getFollowedCoords();
		return map.getX() == ff.x && map.getY() == ff.y;
	}

	public void goToFollowedMap() {
		Point ff = getPerso().getBotInfos().getFollowedCoords();
		getPerso().getNavigation().joinCoords(ff.x, ff.y);
	}

	@Override
	public BehaviorStopReason start() {
		while (isFollowing()) {
			Threads.uSleep(50, TimeUnit.MILLISECONDS); // gentil cpu ! pas cramer !

		}
		return BehaviorStopReason.FINISHED;
	}

}
