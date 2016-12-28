package fr.aresrpg.eratz.domain.data.player.inventory;

import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.dofus.util.StringJoiner;
import fr.aresrpg.eratz.domain.data.ItemsData;
import fr.aresrpg.eratz.domain.data.ItemsData.LangItem;

import java.util.*;

/**
 * 
 * @since
 */
public class Inventory {

	private Map<Long, Item> contents = new HashMap<>(); // itemUid | item
	private int kamas;

	public String showContent() {
		StringJoiner joiner = new StringJoiner(",", "[", "]").add("Kamas: " + kamas);
		contents.values().forEach(i -> joiner.add(showItem(i)));
		return joiner.toString();
	}

	private String showItem(Item i) {
		LangItem l = ItemsData.get(i.getItemTypeId());
		return "{x" + i.getQuantity() + " " + l.getName() + "(id:" + i.getUid() + ")(pods:" + (l.getPod() * i.getQuantity()) + ")}";
	}

	/**
	 * @param kamas
	 *            the kamas to set
	 */
	public void setKamas(int kamas) {
		this.kamas = kamas;
	}

	public void updateContent(List<Item> items) {
		contents.clear();
		for (Item i : items)
			contents.put(i.getUid(), i);
	}

	public void addKamas(int kamas) {
		if (kamas < 0) throw new IllegalArgumentException("Impossible d'ajouter un nombre négatif de kamas !");
		setKamas(getKamas() + kamas);
	}

	public void removeKamas(int kamas) {
		if (kamas < 0) throw new IllegalArgumentException("Impossible de retirer un nombre négatif de kamas !");
		int tot = getKamas() - kamas;
		if (tot < 0) throw new IllegalArgumentException("Il y a moins de " + kamas + " dans la banque !");
		setKamas(tot);
	}

	/**
	 * @return the contents
	 */
	public Map<Long, Item> getContents() {
		return contents;
	}

	public Item getItem(long itemUid) {
		return getContents().get(itemUid);
	}

	/**
	 * @return the kamas
	 */
	public int getKamas() {
		return kamas;
	}

	@Override
	public String toString() {
		return "Inventory [contents=" + contents + ", kamas=" + kamas + "]";
	}

}
