package fr.aresrpg.eratz.domain.ia.behavior.craft.type;

import fr.aresrpg.dofus.structures.Exchange;
import fr.aresrpg.eratz.domain.data.ItemsData;
import fr.aresrpg.eratz.domain.data.dofus.map.Zaapi;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.inventory.Banque;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ia.ability.craft.CraftAbility;
import fr.aresrpg.eratz.domain.ia.ability.move.Navigation;
import fr.aresrpg.eratz.domain.ia.behavior.craft.CraftBehavior;

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
	public ItemsData getItemToCraft() {
		return ItemsData.FARINE_DE_BLE;
	}

	@Override
	public void start() {
		BaseAbility ab = getPerso().getBaseAbility();
		CraftAbility ca = getPerso().getCraftAbility();
		Navigation na = getPerso().getNavigation();
		Banque banque = getPerso().getAccount().getBanque();
		makeRessources();
		ab.equip(8540);
		int inInv = getPerso().getQuantityInInventoryOf(ItemsData.FARINE_DE_BLE.getId());
		int toCraft = getQuantity() - inInv;
		if (inInv > 0) getPerso().goEmptyInvInBanque(492, 8540, 577);
		while (toCraft > 0) {
			ab.goAndOpenBank();
			ab.moveItem(ItemsData.BLE.getId(), getPerso().getFreePods() / ItemsData.BLE.getPod(), Exchange.BANK, Exchange.PLAYER_INVENTORY);
			ab.goToZaapi(Zaapi.BONTA_HDV_CHASSEUR);
			na.moveRight().moveDown(8).moveLeft().moveToCell(446).moveToCell(354);
			ab.useCraftingMachine(1);
			ca.placeItem(ItemsData.BLE.getId(), 2);
			ca.startCraft(toCraft);
			toCraft -= getPerso().getQuantityInInventoryOf(ItemsData.FARINE_DE_BLE.getId());
			getPerso().goEmptyInvInBanque(492, 8540, 577);
		}
	}

	private void makeRessources() {
		int id = ItemsData.BLE.getId();
		int inInv = getPerso().getQuantityInInventoryOf(id);
		int winBanque = getPerso().getQuantityInBanqueOf(id);
		int wheat = inInv + winBanque;
		int quantityToHarvest = getQuantity() * 2 - wheat;
		if (quantityToHarvest > 0)
			getPerso().harvestWheat(quantityToHarvest, true);
	}

}
