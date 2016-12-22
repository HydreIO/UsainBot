package fr.aresrpg.eratz.domain.std.sell;

import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.eratz.domain.data.dofus.item.Hdv;

/**
 * 
 * @since
 */
public interface SellHandler {

	void onBuyingHdvOpened(Hdv hdv);

	void onSellingHdvOpened(Hdv hdv);

	void onItemToSellAdded(Hdv hdv, Item item, long price, int quantity);

	void onItemToSellRemoved(Hdv hdv, Item item);

	void onItemInHdvUpdate(Hdv hdv, Item item);

	void onClosingHdv(Hdv hdv);

}
