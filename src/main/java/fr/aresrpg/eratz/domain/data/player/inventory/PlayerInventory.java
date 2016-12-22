package fr.aresrpg.eratz.domain.data.player.inventory;

import fr.aresrpg.dofus.structures.EquipmentPosition;
import fr.aresrpg.eratz.domain.data.player.Perso;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @since
 */
public class PlayerInventory extends Inventory {

	private Perso perso;
	private Map<EquipmentPosition, Integer> equiped = new HashMap<>();

	/**
	 * @param perso
	 */
	public PlayerInventory(Perso perso) {
		this.perso = perso;
	}

	/**
	 * @return the equiped
	 */
	public Map<EquipmentPosition, Integer> getEquiped() {
		return equiped;
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
