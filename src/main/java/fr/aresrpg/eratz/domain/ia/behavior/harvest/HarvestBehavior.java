package fr.aresrpg.eratz.domain.ia.behavior.harvest;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.eratz.domain.TheBotFather;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.dofus.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;
import fr.aresrpg.tofumanchou.domain.data.enums.Skills;
import fr.aresrpg.tofumanchou.infra.data.Ressource;

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

	private boolean quantityHarvested;
	private boolean fullPod;
	private List<Point> posRessources = new ArrayList<>();
	private boolean automate;
	private long lastHarvest = System.currentTimeMillis();
	private Set<Interractable> ress = new HashSet<>();
	private int lastlvl;

	/**
	 * @param perso
	 */
	public HarvestBehavior(Perso perso) {
		super(perso);
		init();
	}

	protected void experimentPos(int x, int y) {
		posRessources.add(new Point(x, y));
	}

	protected void experimentalTrace() {
		posRessources.sort((a, b) -> Math.abs(b.x - a.x) + Math.abs(b.y - a.y));
		posRessources.removeIf(p -> !needToVisitMap(p));
	}

	protected void useExperimentalIA() {
		this.automate = true;
	}

	protected boolean needToVisitMap(Point p) {
		BotMap map = MapsManager.getMap(p.x, p.y);
		if (map == null) return true;
		int lvlmin = 100;
		for (Ressource r : map.getRessources()) {
			Interractable intr = r.getType();
			if (!ress.contains(intr)) continue;
			Skills skill = getSkill(r.getType());
			if (skill == null) continue;
			int m = skill.getMinLvlToUse();
			if (m < lvlmin) lvlmin = m;
		}
		return getPerso().getBotInfos().getCurrentJob().getLvl() >= lvlmin;
	}

	@Override
	public BehaviorStopReason start() {
		BaseAbility ab = getPerso().getAbilities().getBaseAbility();
		BehaviorStopReason reason = BehaviorStopReason.FINISHED;
		if (!automate) { // ancienne methode de récolte mais peut tjr servir je laisse au cas ou
			IntStream.range(0, pathMoveCount()).forEach(i -> nextPathMove().run()); // go to zone
			for (int i = 0; i < zoneMoveCount(); i++) { // start behavior
				reason = harvestMap();
				if (reason != BehaviorStopReason.FINISHED) return reason;
				nextZoneMove().run();
			}
		} else { // methode de récolte intélligente
			experimentalTrace(); // calcul du chemin
			this.lastlvl = getPerso().getBotInfos().getCurrentJob().getLvl();
			for (Point p : posRessources) {
				if (getPerso().getBotInfos().getCurrentJob().getLvl() != lastlvl) return start(); // si lvl up on sort pour recalculer
				getPerso().getNavigation().joinCoords(p.x, p.y);
				Threads.uSleep(1, TimeUnit.SECONDS); // le temps d'apply frame bordel thx dofus fdp
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
			if (this.lastHarvest + 1800 > System.currentTimeMillis()) {
				TheBotFather.LOGGER.severe("Harvest_loop_bug détécté ! Attente et switch vers la prochaine ressource.");
				Threads.uSleep(2, TimeUnit.SECONDS);
				continue;
			}
			Skills skill = getSkill(next.getType());
			TheBotFather.LOGGER.debug("Récolte de " + next.getType() + "(" + next + ")");
			this.lastHarvest = System.currentTimeMillis();
			getPerso().getAbilities().getHarvestAbility().harvest(next, skill);
			if (podMax()) return BehaviorStopReason.POD_LIMIT;
		}
		return BehaviorStopReason.FINISHED;
	}

	public Skills getSkill(Interractable type) {
		for (Skills s : Skills.values())
			if (s.getType() == type) return s;
		throw new NullPointerException("Skill not found for the type " + type);
	}

	/**
	 * @return a spawned ressource on the map
	 */
	protected Ressource nextRessource() {
		Set<Ressource> collect = getPerso().getMapInfos().getMap().getRessources().stream().filter(r -> {
			if (!ress.contains(r.getType())) return false;
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

	/**
	 * @return the ress
	 */
	protected Set<Interractable> getRess() {
		return ress;
	}

	public abstract void init();

	protected void addRessource(Interractable type) {
		ress.add(type);
	}

}
