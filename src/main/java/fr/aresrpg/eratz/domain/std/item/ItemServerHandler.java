package fr.aresrpg.eratz.domain.std.item;

import fr.aresrpg.dofus.protocol.item.server.ItemAddErrorPacket.AddResult;
import fr.aresrpg.dofus.protocol.item.server.ItemDropErrorPacket.DropResult;
import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.dofus.structures.job.Jobs;

import java.util.Set;

/**
 * 
 * @since
 */
public interface ItemServerHandler {

	void onItemsAdd(Set<Item> items);

	void onItemAddError(AddResult error);

	void onItemDropError(DropResult error);

	void onItemRemove(int itemUid);

	void onItemQuantityUpdate(int itemUid, int quantity);

	void onItemMove(long itemUid, int position); // la pos n'est pas forcément un equipmentPosition si c'est dans l'inv rapide

	void onItemToolEquip(Jobs job);

	void onPodsUpdate(int used, int max);

}
