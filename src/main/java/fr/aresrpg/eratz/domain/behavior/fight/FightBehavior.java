package fr.aresrpg.eratz.domain.behavior.fight;

import fr.aresrpg.eratz.domain.behavior.Behavior;
import fr.aresrpg.eratz.domain.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.util.config.Variables;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public abstract class FightBehavior extends Behavior {

	private static Random r = new Random();
	private Fight fight;

	/**
	 * @param perso
	 */
	public FightBehavior(Perso perso) {
		super(perso);
	}

	public boolean humanFight() {
		return Variables.HUMAN_FIGHT;
	}

	public abstract int getBeginCellId();

	@Override
	public void start() {
		if (getPerso().getCurrentFight() == null) throw new IllegalStateException("Le combat est null");
		getPerso().getFightAbility().goToCellBeforeStart(getBeginCellId());
		waitCanStartFight();
		getPerso().getFightAbility().beReady(true);
		while (!getPerso().getCurrentFight().isEnded())
			if (getPerso().getCurrentFight().getCurrentTurn() == getPerso()) playTurn();
	}

	public abstract void playTurn();

	public void tryHuman() {
		if (humanFight()) botWait(r.nextInt(3000) + 1000, TimeUnit.MILLISECONDS);
	}

	protected void waitCanStartFight() {
		while (!getPerso().getFightOptions().canStartCombat()) // attente de pouvoir start le combat
			;
	}

}
