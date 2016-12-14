package fr.aresrpg.eratz.domain.ia.behavior.item;

import fr.aresrpg.eratz.domain.data.ItemsData;
import fr.aresrpg.eratz.domain.data.dofus.map.City;
import fr.aresrpg.eratz.domain.data.dofus.map.Zaapi;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbility.BuyResult;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;
import fr.aresrpg.eratz.domain.provider.ItemProvider;

public class WaterBuy extends Behavior {

	private int quantity;
	/**
	 * @param perso
	 */
	public WaterBuy(Perso perso,int quantity) {
		super(perso);
		this.quantity = quantity;
		moveDown();
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	@Override
	public void provide() {
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
	public BehaviorStopReason start() {
		BaseAbility ba = getPerso().getAbilities().getBaseAbility();
		return null;
	}

}
