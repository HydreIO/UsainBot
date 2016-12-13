package fr.aresrpg.eratz.domain.behavior.harvest;

import fr.aresrpg.eratz.domain.behavior.Behavior;
import fr.aresrpg.eratz.domain.behavior.BehaviorStopReason;
import fr.aresrpg.eratz.domain.dofus.map.Ressource;
import fr.aresrpg.eratz.domain.dofus.ressource.Interractable;
import fr.aresrpg.eratz.domain.player.Perso;

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
		return getPerso().getPodsPercent() > 95;
	}

	protected boolean isValid(BehaviorStopReason reason) {
		return reason == BehaviorStopReason.FINISHED;
	}

	/**
	 * Harvest an entire map
	 * 
	 * @return true if the map was harvested, false if the perso is full pod/quantity reached
	 */
	protected BehaviorStopReason harvestMap() {
		Ressource next = null;
		while ((next = nextRessource()) != null) {
			getPerso().getHarvestAbility().harvest(next);
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

}
