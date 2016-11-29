package fr.aresrpg.eratz.domain.behavior.craft;

import fr.aresrpg.dofus.structures.character.Item;
import fr.aresrpg.eratz.domain.behavior.BaseBehavior;

/**
 * 
 * @since
 */
public interface CraftBehavior extends BaseBehavior {

	Item getItemToCraft();

	int getQuantityToCraft();
}
