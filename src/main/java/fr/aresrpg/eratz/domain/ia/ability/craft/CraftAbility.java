package fr.aresrpg.eratz.domain.ia.ability.craft;

import fr.aresrpg.eratz.domain.util.BotThread;
import fr.aresrpg.eratz.domain.util.Closeable;

/**
 * 
 * @since
 */
public interface CraftAbility extends Closeable {

	void startCraft();

	void startCraft(int nbr);

	void cancelCraft();

	void replaceCraft();

	BotThread getBotThread();

}
