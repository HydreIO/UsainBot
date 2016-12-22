package fr.aresrpg.eratz.domain.antibot.behavior;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.dofus.protocol.exchange.ExchangeLeavePacket;
import fr.aresrpg.dofus.protocol.exchange.client.ExchangeRequestPacket;
import fr.aresrpg.dofus.structures.Exchange;
import fr.aresrpg.eratz.domain.antibot.AntiBot;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class ExchangeCrashBehavior extends Behavior {

	private boolean running;
	private int target;
	private int count;

	/**
	 * @param perso
	 */
	public ExchangeCrashBehavior(Perso perso, int target) {
		super(perso);
		this.running = true;
		this.target = target;
	}

	/**
	 * @return the target
	 */
	public int getTarget() {
		return target;
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	public void stop() {
		getPerso().getAbilities().getBaseAbility().getStates().currentToCrash = 0;
		this.running = false;
	}

	@Override
	public BehaviorStopReason start() {
		LOGGER.info("Starting crash behavior.");
		BaseAbility ab = getPerso().getAbilities().getBaseAbility();
		count = 0;
		if (!getPerso().getMapInfos().getMap().isOnMap(getTarget())) {
			AntiBot.getInstance().notifyLeave(getTarget());
			return BehaviorStopReason.FINISHED;
		}
		ab.getStates().currentToCrash = getTarget();
		while (isRunning() && getPerso().getAccount().isBotOnline()) {
			if (++count > 20) break;
			Threads.uSleep(2, TimeUnit.MILLISECONDS);
			ExchangeRequestPacket pkt = new ExchangeRequestPacket(Exchange.EXCHANGE, getTarget());
			getPerso().sendPacketToServer(pkt);

			Threads.uSleep(2, TimeUnit.MILLISECONDS);
			ExchangeLeavePacket pkl = new ExchangeLeavePacket();
			getPerso().sendPacketToServer(pkl);
		}
		return BehaviorStopReason.FINISHED;
	}

	@Override
	public String toString() {
		return "ExchangeCraftBehavior [running=" + running + ", target=" + target + "]";
	}

}
