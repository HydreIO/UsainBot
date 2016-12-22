package fr.aresrpg.eratz.domain.ia.ability.craft;

import fr.aresrpg.eratz.domain.util.Closeable;

/**
 * 
 * @since
 */
public interface CraftAbility extends Closeable {

	void placeItem(int id, int quantity);

	void removeItem(int index);

	void closeGui();

	void startCraft(int quantity);

	void cancelCraft();

}
