package fr.aresrpg.eratz.domain.ia.fight;

import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.fight.behavior.FightBehavior;

/**
 * 
 * @since
 */
public class Fighting extends Info {

	private final FightBehavior behavior;

	/**
	 * @param perso
	 */
	public Fighting(BotPerso perso) {
		super(perso);
		this.behavior = Fights.getBehavior(perso, perso.getPerso().getLevel());
	}

	public void playTurn() {
		behavior.playTurn();
	}

	public void notifyStatsReceive() {
		behavior.notifyStatsReceive();
	}

	public void notifyPaReceive() {
		behavior.notifyPaReceive();
	}

	public void notifyPmReceive() {
		behavior.notifyPmReceive();
	}

	@Override
	public void shutdown() {
	}

}
