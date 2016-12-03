package fr.aresrpg.eratz.domain.ability.craft;

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
