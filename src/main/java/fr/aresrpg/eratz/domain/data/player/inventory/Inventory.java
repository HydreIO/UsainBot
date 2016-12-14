package fr.aresrpg.eratz.domain.data.player.inventory;

import fr.aresrpg.dofus.structures.item.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @since
 */
public class Inventory {

	private Map<Integer, Item> contents = new HashMap<>(); // itemtype | item
	private int kamas;

	/**
	 * @param kamas
	 *            the kamas to set
	 */
	private void setKamas(int kamas) {
		this.kamas = kamas;
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
	public Map<Integer, Item> getContents() {
		return contents;
	}

	public Item getItem(int itemtype) {
		return getContents().get(itemtype);
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
