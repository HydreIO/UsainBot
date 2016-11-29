package fr.aresrpg.eratz.domain.behavior.craft;

/**
 * 
 * @since
 */
public interface CraftAbility {

	void placeItem(int id, int quantity);

	void removeItem(int index);

	void closeGui();

	void startCraft();

	void cancelCraft();

}
