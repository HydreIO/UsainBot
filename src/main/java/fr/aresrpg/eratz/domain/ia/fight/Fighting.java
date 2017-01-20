package fr.aresrpg.eratz.domain.ia.fight;

import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.tofumanchou.domain.data.enums.DofusMobs;

/**
 * 
 * @since
 */
public class Fighting extends Info {

	private final FightBehavior behavior;
	private DofusMobs[] mobs;

	/**
	 * @param perso
	 */
	public Fighting(BotPerso perso) {
		super(perso);
		this.behavior = Fights.getBehavior(perso.getPerso().getClasse(), perso.getPerso().getLevel());
	}

	public void playTurn() {
		behavior.playTurn();
	}

	@Override
	public void shutdown() {
	}

}
