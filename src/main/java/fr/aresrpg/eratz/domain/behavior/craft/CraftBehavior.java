package fr.aresrpg.eratz.domain.behavior.craft;

import fr.aresrpg.eratz.domain.behavior.Behavior;
import fr.aresrpg.eratz.domain.dofus.item.Items;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.player.Player;

/**
 * 
 * @since
 */
public abstract class CraftBehavior extends Behavior {

	private int quantity;

	/**
	 * @param perso
	 */
	public CraftBehavior(Perso perso, int quantity) {
		super(perso);
		this.quantity = quantity;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	public abstract Items getItemToCraft();

	@Override
	public boolean acceptDefi(Player p) {
		return false;
	}

	@Override
	public boolean acceptEchange(Player p) {
		return false;
	}

	@Override
	public boolean acceptGuilde(String pname) {
		return false;
	}

	@Override
	public boolean acceptGroup(String pname) {
		for (Player p : getPerso().getGroup())
			if (p.getPseudo().equalsIgnoreCase(pname)) return true;
		return false;
	}

}
