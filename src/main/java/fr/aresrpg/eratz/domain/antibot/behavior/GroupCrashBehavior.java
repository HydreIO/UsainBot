package fr.aresrpg.eratz.domain.antibot.behavior;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.dofus.protocol.party.PartyRefusePacket;
import fr.aresrpg.dofus.protocol.party.client.PartyInvitePacket;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class GroupCrashBehavior extends Behavior {

	private boolean running;
	private String target;

	/**
	 * @param perso
	 */
	public GroupCrashBehavior(BotPerso perso, String target) {
		super(perso);
		this.running = true;
		this.target = target;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	public void stop() {
		this.running = false;
	}

	@Override
	public BehaviorStopReason start() {
		for (int a = 0; a < 50; a++) {
			Threads.uSleep(3, TimeUnit.MILLISECONDS);
			PartyInvitePacket pkt = new PartyInvitePacket();
			pkt.setPname(getTarget());
			getPerso().sendPacketToServer(pkt);
			Threads.uSleep(3, TimeUnit.MILLISECONDS);
			PartyRefusePacket ref = new PartyRefusePacket();
			getPerso().sendPacketToServer(ref);
		}
		return BehaviorStopReason.FINISHED;
	}

	@Override
	public String toString() {
		return "GroupCrashBehavior [running=" + running + ", target=" + target + "]";
	}

}
