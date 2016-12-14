package fr.aresrpg.eratz.domain.ia.ability.craft;

/**
 * 
 * @since
 */
public interface CraftAbility {

	void placeItem(int id, int quantity);

	void removeItem(int index);

	void closeGui();

	void startCraft(int quantity);

	void cancelCraft();

}
