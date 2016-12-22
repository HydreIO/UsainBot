package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import fr.aresrpg.eratz.domain.data.player.Perso;

/**
 * 
 * @since
 */
public abstract class BotHandlerAbstract {

	private Perso perso;

	public BotHandlerAbstract(Perso perso) {
		this.perso = perso;
	}

	public Perso getPerso() {
		return perso;
	}

}
