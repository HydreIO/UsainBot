package fr.aresrpg.eratz.domain.ia.behavior.harvest;

import fr.aresrpg.dofus.structures.Skills;
import fr.aresrpg.eratz.domain.TheBotFather;
import fr.aresrpg.eratz.domain.data.dofus.ressource.Interractable;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.object.Ressource;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;

import java.util.Set;
import java.util.stream.Collectors;
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
			getPerso().getAbilities().getHarvestAbility().harvest(next, getSkill());
			if (podMax()) return BehaviorStopReason.POD_LIMIT;
			else if (getPerso().getQuantityInInventoryOf(getTypesToHarvest().getId()) >= getQuantity()) return BehaviorStopReason.QUANTITY_REACHED;
		}
		return BehaviorStopReason.FINISHED;
	}

	public abstract Skills getSkill();

	/**
	 * @return a spawned ressource on the map
	 */
	protected Ressource nextRessource() {
		Set<Ressource> collect = getPerso().getMapInfos().getMap().getRessources().stream().filter(r -> getTypesToHarvest() == r.getType() && r.isSpawned()).collect(Collectors.toSet());
		Ressource r = null;
		int dist = Integer.MAX_VALUE;
		for (Ressource re : collect) {
			if (r == null) r = re;
			int di = re.getCell().distance(getPerso().getMapInfos().getCellId());
			if (di < dist) {
				r = re;
				dist = di;
			}
		}
		TheBotFather.LOGGER.debug("Récolte de " + r);
		return r;
	}

	public abstract Interractable getTypesToHarvest();

	public abstract void initMoves();

}
