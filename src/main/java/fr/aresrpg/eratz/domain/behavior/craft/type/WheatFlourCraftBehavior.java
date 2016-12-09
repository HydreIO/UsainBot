package fr.aresrpg.eratz.domain.behavior.craft.type;

import fr.aresrpg.eratz.domain.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ability.craft.CraftAbility;
import fr.aresrpg.eratz.domain.ability.move.Navigation;
import fr.aresrpg.eratz.domain.behavior.craft.CraftBehavior;
import fr.aresrpg.eratz.domain.dofus.item.Items;
import fr.aresrpg.eratz.domain.dofus.map.Zaapi;
import fr.aresrpg.eratz.domain.player.Banque;
import fr.aresrpg.eratz.domain.player.Perso;

/**
 * 
 * @since
 */
public class WheatFlourCraftBehavior extends CraftBehavior {

	/**
	 * @param perso
	 */
	public WheatFlourCraftBehavior(Perso perso, int quantity) {
		super(perso, quantity);
	}

	@Override
	public Items getItemToCraft() {
		return Items.FARINE_DE_BLE;
	}

	@Override
	public void start() {
		BaseAbility ab = getPerso().getBaseAbility();
		CraftAbility ca = getPerso().getCraftAbility();
		Navigation na = getPerso().getNavigation();
		Banque banque = getPerso().getAccount().getBanque();
		makeRessources();
		ab.equip(8540);
		int inInv = getPerso().getQuantityInInventoryOf(Items.FARINE_DE_BLE.getId());
		int toCraft = getQuantity() - inInv;
		if (inInv > 0) getPerso().goEmptyInvInBanque(492, 8540, 577);
		while (toCraft > 0) {
			ab.goAndOpenBank();
			ab.getItemFromBank(Items.BLE.getId(), getPerso().getFreePods() / Items.BLE.getPod());
			ab.goToZaapi(Zaapi.BONTA_HDV_CHASSEUR);
			na.moveRight().moveDown(8).moveLeft().moveToCell(446).moveToCell(354);
			ab.useCraftingMachine(1);
			ca.placeItem(Items.BLE.getId(), 2);
			ca.startCraft(toCraft);
			toCraft -= getPerso().getQuantityInInventoryOf(Items.FARINE_DE_BLE.getId());
			getPerso().goEmptyInvInBanque(492, 8540, 577);
		}
	}

	private void makeRessources() {
		int id = Items.BLE.getId();
		int inInv = getPerso().getQuantityInInventoryOf(id);
		int winBanque = getPerso().getQuantityInBanqueOf(id);
		int wheat = inInv + winBanque;
		int quantityToHarvest = getQuantity() * 2 - wheat;
		if (quantityToHarvest > 0)
			getPerso().harvestWheat(quantityToHarvest, true);
	}

}
