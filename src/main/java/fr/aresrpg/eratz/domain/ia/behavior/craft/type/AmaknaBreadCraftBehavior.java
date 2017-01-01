/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.ia.behavior.craft.type;

import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;
import fr.aresrpg.eratz.domain.ia.behavior.craft.CraftBehavior;

/**
 * 
 * @since
 */
public class AmaknaBreadCraftBehavior extends CraftBehavior {

	/**
	 * @param perso
	 */
	public AmaknaBreadCraftBehavior(BotPerso perso, int craft) {
		super(perso, craft);

	}

	@Override
	public BehaviorStopReason start() {
		return null;
	}

	/*
	 * @Override
	 * public void start() {
	 * if (getPerso().getQuantityInInventoryOf(ItemsData.FARINE_DE_BLE.getId()) > 0) getPerso().goEmptyInvInBanque(492, 8540, 577);
	 * int farineNeed = getQuantity() - getPerso().getQuantityInBanqueOf(ItemsData.FARINE_DE_BLE.getId());
	 * int waterNeed = getQuantity() - (getPerso().getQuantityInInventoryOf(ItemsData.EAU.getId()) + getPerso().getQuantityInBanqueOf(ItemsData.EAU.getId()));
	 * if (waterNeed > 0) new WaterBuy(getPerso(), waterNeed).provide();
	 * getPerso().goEmptyInvInBanque(492, 8540, 577);
	 * if (farineNeed > 0)
	 * new WheatFlourCraftBehavior(getPerso(), farineNeed).start();
	 * BaseAbility ba = getPerso().getBaseAbility();
	 * Navigation na = getPerso().getNavigation();
	 * CraftAbility ca = getPerso().getCraftAbility();
	 * int tocraft = getQuantity();
	 * int eauId = ItemsData.EAU.getId();
	 * int farineId = ItemsData.FARINE_DE_BLE.getId();
	 * while (tocraft > 0) {
	 * ba.goAndOpenBank();
	 * int totalpodForOne = ItemsData.EAU.getPod() + ItemsData.FARINE_DE_BLE.getPod();
	 * int eau = getPerso().getFreePods() / totalpodForOne;
	 * int farine = getPerso().getFreePods() - eau;
	 * ba.moveItem(eauId, eau, Exchange.BANK, Exchange.PLAYER_INVENTORY);
	 * ba.moveItem(farineId, farine, Exchange.BANK, Exchange.PLAYER_INVENTORY);
	 * ba.goToZaapi(Zaapi.BONTA_HDV_BOULANGER);
	 * na.moveToCell(472).moveToCell(219);
	 * ba.useCraftingMachine(2);
	 * ca.placeItem(eauId, 1);
	 * ca.placeItem(farineId, 1);
	 * ca.startCraft(tocraft);
	 * tocraft -= getPerso().getQuantityInInventoryOf(ItemsData.PAIN_AMAKNA.getId());
	 * getPerso().goEmptyInvInBanque(492, 8540, 577);
	 * }
	 * }
	 */
}
