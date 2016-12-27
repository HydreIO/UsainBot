package fr.aresrpg.eratz.domain.data.player.inventory;

import fr.aresrpg.dofus.structures.EquipmentPosition;
import fr.aresrpg.dofus.structures.character.Character;
import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.dofus.structures.item.ItemCategory;
import fr.aresrpg.eratz.domain.data.ItemsData;
import fr.aresrpg.eratz.domain.data.ItemsData.LangItem;
import fr.aresrpg.eratz.domain.data.dofus.item.DofusItems;
import fr.aresrpg.eratz.domain.data.dofus.item.DofusItems2;
import fr.aresrpg.eratz.domain.data.player.Perso;

import java.util.*;

/**
 * 
 * @since
 */
public class PlayerInventory extends Inventory {

	private Perso perso;

	/**
	 * @param perso
	 */
	public PlayerInventory(Perso perso) {
		this.perso = perso;
	}

	public void addItem(Item i) {
		Item local = getItemByType(i.getItemTypeId());
		if (local.isStackableWith(i)) local.setQuantity(local.getQuantity() + i.getQuantity());
		else getContents().put(i.getUid(), i);
	}

	public void parseCharacter(Character c) {
		if (c.getItems() == null) return;
		Arrays.stream(c.getItems()).forEach(i -> getContents().put(i.getUid(), i));
	}

	public Item getItemAtPos(EquipmentPosition pos) {
		Item i = null;
		for (Item e : getContents().values())
			if (e.getPosition() == pos.getPosition()) return i;
		return null;
	}

	public Item getHeaviestItem() {
		int pod = 0;
		Item item = null;
		for (Item i : getContents().values()) {
			LangItem langitem = ItemsData.get(i.getItemTypeId());
			if (!langitem.getCategory().isResource()) continue;
			if (item == null) item = i;
			int p = langitem.getPod() * i.getQuantity();
			if (p > pod) {
				pod = p;
				item = i;
			}
		}
		return item;
	}

	public Item getItemByType(int itemType) {
		for (Item e : getContents().values())
			if (e.getItemTypeId() == itemType) return e;
		return null;
	}

	public Item getItemByType(DofusItems item) {
		return getItemByType(item.getId());
	}

	public Item getItemByType(DofusItems2 item) {
		return getItemByType(item.getId());
	}

	public Set<Item> getItemsByCategory(ItemCategory category) {
		Set<Item> items = new HashSet<>();
		for (Item i : getContents().values()) {
			ItemCategory c = ItemsData.get(i.getItemTypeId()).getCategory();
			if (c == category) items.add(i);
		}
		return items;
	}

	@Override
	public String toString() {
		return "PlayerInventory [perso=" + perso + "]" + super.toString();
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

}
