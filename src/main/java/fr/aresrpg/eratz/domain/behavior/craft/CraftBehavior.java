package fr.aresrpg.eratz.domain.behavior.craft;

import fr.aresrpg.eratz.domain.behavior.BaseBehavior;

/**
 * 
 * @since
 */
public interface CraftBehavior extends BaseBehavior {

	void placeItem(int id, int quantity);

	void removeItem(int index);

	void closeGui();

	void startCraft();

	void cancelCraft();

}
