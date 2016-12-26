package fr.aresrpg.eratz.domain.data.player.inventory;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.dofus.structures.EquipmentPosition;
import fr.aresrpg.dofus.structures.character.Character;
import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.eratz.domain.data.dofus.item.DofusItems;
import fr.aresrpg.eratz.domain.data.dofus.item.DofusItems2;
import fr.aresrpg.eratz.domain.data.player.Perso;

import java.util.Arrays;

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
		LOGGER.debug("ADD " + c.getItems().length + " ITEMS");
		Arrays.stream(c.getItems()).forEach(i -> getContents().put(i.getUid(), i));
	}

	public Item getItemAtPos(EquipmentPosition pos) {
		Item i = null;
		for (Item e : getContents().values())
			if (e.getPosition() == pos.getPosition()) return i;
		return null;
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
