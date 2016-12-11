package fr.aresrpg.eratz.domain.player;

import fr.aresrpg.dofus.structures.item.Object;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @since
 */
public class Banque {

	private Set<Object> armes = new HashSet<>();
	private Set<Object> divers = new HashSet<>();
	private Set<Object> ressources = new HashSet<>();
	private int kamas;
	private Account account;

	public Banque(Account account) {
		this.account = account;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @return the kamas
	 */
	public int getKamas() {
		return kamas;
	}

	/**
	 * @param kamas
	 *            the kamas to set
	 */
	public void setKamas(int kamas) {
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
	 * @return the armes
	 */
	public Set<Object> getArmes() {
		return armes;
	}

	/**
	 * @return the divers
	 */
	public Set<Object> getDivers() {
		return divers;
	}

	/**
	 * @return the ressources
	 */
	public Set<Object> getRessources() {
		return ressources;
	}

}
