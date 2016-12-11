package fr.aresrpg.eratz.domain.player;

import fr.aresrpg.dofus.structures.item.Object;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @since
 */
public class Inventory {

	private Set<Object> contents = new HashSet<>();
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
	public Set<Object> getContents() {
		return contents;
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
