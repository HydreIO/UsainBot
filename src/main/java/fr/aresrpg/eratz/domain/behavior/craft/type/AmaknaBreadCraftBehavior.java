/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.behavior.craft.type;

import fr.aresrpg.eratz.domain.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ability.craft.CraftAbility;
import fr.aresrpg.eratz.domain.ability.move.Navigation;
import fr.aresrpg.eratz.domain.behavior.craft.CraftBehavior;
import fr.aresrpg.eratz.domain.dofus.item.Items;
import fr.aresrpg.eratz.domain.dofus.map.Zaapi;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.provider.type.WaterProvider;

/**
 * 
 * @since
 */
public class AmaknaBreadCraftBehavior extends CraftBehavior {

	/**
	 * @param perso
	 */
	public AmaknaBreadCraftBehavior(Perso perso, int craft) {
		super(perso, craft);

	}

	@Override
	public Items getItemToCraft() {
		return Items.PAIN_AMAKNA;
	}

	@Override
	public void start() {
		if (getPerso().getQuantityInInventoryOf(Items.FARINE_DE_BLE.getId()) > 0) getPerso().goEmptyInvInBanque(492, 8540, 577);
		int farineNeed = getQuantity() - getPerso().getQuantityInBanqueOf(Items.FARINE_DE_BLE.getId());
		int waterNeed = getQuantity() - (getPerso().getQuantityInInventoryOf(Items.EAU.getId()) + getPerso().getQuantityInBanqueOf(Items.EAU.getId()));
		if (waterNeed > 0) new WaterProvider(getPerso(), waterNeed).provide();
		getPerso().goEmptyInvInBanque(492, 8540, 577);
		if (farineNeed > 0)
			new WheatFlourCraftBehavior(getPerso(), farineNeed).start();
		BaseAbility ba = getPerso().getBaseAbility();
		Navigation na = getPerso().getNavigation();
		CraftAbility ca = getPerso().getCraftAbility();
		int tocraft = getQuantity();
		int eauId = Items.EAU.getId();
		int farineId = Items.FARINE_DE_BLE.getId();
		while (tocraft > 0) {
			ba.goAndOpenBank();
			int totalpodForOne = Items.EAU.getPod() + Items.FARINE_DE_BLE.getPod();
			int eau = getPerso().getFreePods() / totalpodForOne;
			int farine = getPerso().getFreePods() - eau;
			ba.getItemFromBank(eauId, eau);
			ba.getItemFromBank(farineId, farine);
			ba.goToZaapi(Zaapi.BONTA_HDV_BOULANGER);
			na.moveToCell(472).moveToCell(219);
			ba.useCraftingMachine(2);
			ca.placeItem(eauId, 1);
			ca.placeItem(farineId, 1);
			ca.startCraft(tocraft);
			tocraft -= getPerso().getQuantityInInventoryOf(Items.PAIN_AMAKNA.getId());
			getPerso().goEmptyInvInBanque(492, 8540, 577);
		}
	}
}
