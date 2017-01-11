package fr.aresrpg.eratz.domain.ia.fight;

import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;

/**
 * 
 * @since
 */
public class FightRunner extends Info {

	/**
	 * @param perso
	 */
	public FightRunner(BotPerso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {

	}

}
