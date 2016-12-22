package fr.aresrpg.eratz.domain.ia.behavior.harvest;

import fr.aresrpg.eratz.domain.data.dofus.item.DofusItems2;
import fr.aresrpg.eratz.domain.data.dofus.ressource.Interractable;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.object.Ressource;
import fr.aresrpg.eratz.domain.ia.Roads;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;

import java.util.stream.IntStream;

/**
 * 
 * @since
 */
public abstract class HarvestBehavior extends Behavior {

	private int quantity;
	private boolean quantityHarvested;
	private boolean fullPod;

	/**
	 * @param perso
	 */
	public HarvestBehavior(Perso perso, int quantity) {
		super(perso);
		this.quantity = quantity;
		initMoves();
	}

	@Override
	public BehaviorStopReason start() {
		BaseAbility ab = getPerso().getAbilities().getBaseAbility();
		BehaviorStopReason reason = BehaviorStopReason.FINISHED;
		if (!ab.useItem(DofusItems2.POTION_DE_RAPPEL)) Roads.nearestRoad(getPerso()).takeRoad(getPerso()); // go to zaap astrub
		IntStream.range(0, pathMoveCount()).forEach(i -> nextPathMove().run()); // go to zone
		for (int i = 0; i < zoneMoveCount(); i++) { // start behavior
			reason = harvestMap();
			if (reason != BehaviorStopReason.FINISHED) return reason;
			nextZoneMove().run();
		}
		return reason;
	}

	/**
	 * @return the fullPod
	 */
	public boolean isFullPod() {
		return fullPod;
	}

	/**
	 * @return the quantityHarvested
	 */
	public boolean isQuantityHarvested() {
		return quantityHarvested;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	protected boolean podMax() {
		return getPerso().getStatsInfos().isFullPod();
	}

	/**
	 * Harvest an entire map
	 * 
	 * @return true if the map was harvested, false if the perso is full pod/quantity reached
	 */
	protected BehaviorStopReason harvestMap() {
		Ressource next = null;
		while ((next = nextRessource()) != null) {
			getPerso().getAbilities().getHarvestAbility().harvest(next);
			if (podMax()) return BehaviorStopReason.POD_LIMIT;
			else if (getPerso().getQuantityInInventoryOf(getTypesToHarvest().getId()) >= getQuantity()) return BehaviorStopReason.QUANTITY_REACHED;
		}
		return BehaviorStopReason.FINISHED;
	}

	/**
	 * @return a spawned ressource on the map
	 */
	protected Ressource nextRessource() {
		for (Ressource r : getPerso().getMapInfos().getMap().getRessources())
			if (getTypesToHarvest() == r.getType() && r.isSpawned()) return r;
		return null;
	}

	public abstract Interractable getTypesToHarvest();

	public abstract void initMoves();

}
