package fr.aresrpg.eratz.domain.ia.behavior.craft.type;

import fr.aresrpg.commons.domain.util.exception.NotImplementedException;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;
import fr.aresrpg.eratz.domain.ia.behavior.craft.CraftBehavior;

/**
 * 
 * @since
 */
public class WheatFlourCraftBehavior extends CraftBehavior {

	/**
	 * @param perso
	 */
	public WheatFlourCraftBehavior(BotPerso perso, int quantity) {
		super(perso, quantity);
		throw new NotImplementedException();
	}

	@Override
	public BehaviorStopReason start() {
		return null;
	}

	/*
	 * @Override
	 * public void start() {
	 * BaseAbility ab = getPerso().getBaseAbility();
	 * CraftAbility ca = getPerso().getCraftAbility();
	 * Navigation na = getPerso().getNavigation();
	 * Banque banque = getPerso().getAccount().getBanque();
	 * makeRessources();
	 * ab.equip(8540);
	 * int inInv = getPerso().getQuantityInInventoryOf(ItemsData.FARINE_DE_BLE.getId());
	 * int toCraft = getQuantity() - inInv;
	 * if (inInv > 0) getPerso().goEmptyInvInBanque(492, 8540, 577);
	 * while (toCraft > 0) {
	 * ab.goAndOpenBank();
	 * ab.moveItem(ItemsData.BLE.getId(), getPerso().getFreePods() / ItemsData.BLE.getPod(), Exchange.BANK, Exchange.PLAYER_INVENTORY);
	 * ab.goToZaapi(Zaapi.BONTA_HDV_CHASSEUR);
	 * na.moveRight().moveDown(8).moveLeft().moveToCell(446).moveToCell(354);
	 * ab.useCraftingMachine(1);
	 * ca.placeItem(ItemsData.BLE.getId(), 2);
	 * ca.startCraft(toCraft);
	 * toCraft -= getPerso().getQuantityInInventoryOf(ItemsData.FARINE_DE_BLE.getId());
	 * getPerso().goEmptyInvInBanque(492, 8540, 577);
	 * }
	 * }
	 * private void makeRessources() {
	 * int id = ItemsData.BLE.getId();
	 * int inInv = getPerso().getQuantityInInventoryOf(id);
	 * int winBanque = getPerso().getQuantityInBanqueOf(id);
	 * int wheat = inInv + winBanque;
	 * int quantityToHarvest = getQuantity() * 2 - wheat;
	 * if (quantityToHarvest > 0)
	 * getPerso().harvestWheat(quantityToHarvest, true);
	 * }
	 */

}
