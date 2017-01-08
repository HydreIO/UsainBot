package fr.aresrpg.eratz.domain.data.player.info;

import fr.aresrpg.eratz.domain.data.player.BotPerso;

/**
 * 
 * @since
 */
public class BotInfo extends Info {
	
	

	/**
	 * @param perso
	 */
	public BotInfo(BotPerso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {
	}

}
