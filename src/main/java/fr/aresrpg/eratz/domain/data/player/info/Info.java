package fr.aresrpg.eratz.domain.data.player.info;

import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.util.Closeable;

/**
 * 
 * @since
 */
public abstract class Info implements Closeable {

	private BotPerso perso;

	/**
	 * @param perso
	 */
	public Info(BotPerso perso) {
		this.perso = perso;
	}

	/**
	 * @return the perso
	 */
	public BotPerso getPerso() {
		return perso;
	}

	@Override
	public String toString() {
		return "Info [perso=" + perso + "]";
	}

}
