package fr.aresrpg.eratz.domain.ia.behavior.harvest;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.dofus.structures.Skills;
import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.eratz.domain.TheBotFather;
import fr.aresrpg.eratz.domain.data.dofus.ressource.Interractable;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.object.Ressource;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;

import java.awt.Point;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
	private List<Point> posRessources = new ArrayList<>();
	private boolean automate;

	/**
	 * @param perso
	 */
	public HarvestBehavior(Perso perso, int quantity) {
		super(perso);
		this.quantity = quantity;
		initMoves();
	}

	protected void experimentPos(int x, int y) {
		posRessources.add(new Point(x, y));
	}

	protected void experimentalTrace() {
		posRessources.sort((a, b) -> Math.abs(b.x - a.x) + Math.abs(b.y - a.y));
	}

	protected void useExperimentalIA() {
		this.automate = true;
	}

	@Override
	public BehaviorStopReason start() {
		BaseAbility ab = getPerso().getAbilities().getBaseAbility();
		BehaviorStopReason reason = BehaviorStopReason.FINISHED;
		if (!automate) {
			IntStream.range(0, pathMoveCount()).forEach(i -> nextPathMove().run()); // go to zone
			for (int i = 0; i < zoneMoveCount(); i++) { // start behavior
				reason = harvestMap();
				if (reason != BehaviorStopReason.FINISHED) return reason;
				nextZoneMove().run();
			}
		} else {
			experimentalTrace();
			for (Point p : posRessources) {
				getPerso().getNavigation().joinCoords(p.x, p.y);
				Threads.uSleep(1, TimeUnit.SECONDS); // le temps d'apply frame bordel
				reason = harvestMap();
				if (reason != BehaviorStopReason.FINISHED) return reason;
			}
			if (reason == BehaviorStopReason.FINISHED) { // si on a visité toute les map et que la raison n'a pas changé
				TheBotFather.LOGGER.success("Récolte terminé mais non fullPod ! Reprise du trajet.");
				return start(); // alors on recommence le trajet jusqu'a fullpod ou quantity harvested
			}
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
			Skills skill = getSkill(next.getType());
			TheBotFather.LOGGER.debug("Récolte de " + next.getType() + "(" + next + ")");
			getPerso().getAbilities().getHarvestAbility().harvest(next, skill);
			if (podMax()) return BehaviorStopReason.POD_LIMIT;
			else if (getQuantityOfHarvestedInInv() >= getQuantity()) return BehaviorStopReason.QUANTITY_REACHED;
		}
		return BehaviorStopReason.FINISHED;
	}

	protected int getQuantityOfHarvestedInInv() {
		int amount = 0;
		for (Item i : getPerso().getInventory().getContents().values())
			if (ArrayUtils.contains(i.getItemTypeId(), getTypesToHarvest())) amount++;
		return amount;
	}

	public abstract Skills getSkill(Interractable type);

	/**
	 * @return a spawned ressource on the map
	 */
	protected Ressource nextRessource() {
		Set<Ressource> collect = getPerso().getMapInfos().getMap().getRessources().stream().filter(r -> {
			if (!ArrayUtils.contains(r.getType(), getTypesToHarvest())) return false;
			Skills skill = getSkill(r.getType());
			if (getPerso().getBotInfos().getCurrentJob().getLvl() < skill.getMinLvlToUse()) return false;
			return r.isSpawned();
		}).collect(Collectors.toSet());
		Ressource r = null;
		int dist = Integer.MAX_VALUE;
		for (Ressource re : collect) {
			if (r == null) r = re;
			int di = re.distance(getPerso().getMapInfos().getCellId());
			if (di < dist) {
				r = re;
				dist = di;
			}
		}
		return r;
	}

	public abstract Interractable[] getTypesToHarvest();

	public abstract void initMoves();

}
