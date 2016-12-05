package fr.aresrpg.eratz.domain.provider.type;

import fr.aresrpg.eratz.domain.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ability.BaseAbility.BuyResult;
import fr.aresrpg.eratz.domain.dofus.item.Items;
import fr.aresrpg.eratz.domain.dofus.map.City;
import fr.aresrpg.eratz.domain.dofus.map.Zaapi;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.provider.ItemProvider;

public class WaterProvider implements ItemProvider {

	private Perso perso;
	private int quantity;

	public WaterProvider(Perso p, int quantity) {
		this.perso = p;
		this.quantity = quantity;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	@Override
	public void provide() {
		BaseAbility ba = getPerso().getBaseAbility();
		ba.goToCity(City.BONTA);
		ba.goToZaapi(Zaapi.BONTA_BAGRUTTE);
		ba.buyToNpc(-3);
		BuyResult r = ba.npcBuyChoice(getItemType().getId(), getQuantity());
		if (r == BuyResult.NO_KAMA || r == BuyResult.NO_PODS) {
			getPerso().crashReport("Impossible d'achetter [" + getQuantity() + "x " + getItemType().getName() + "] | " + r.getReason());
			return;
		}
	}

	@Override
	public Items getItemType() {
		return Items.EAU;
	}

}
