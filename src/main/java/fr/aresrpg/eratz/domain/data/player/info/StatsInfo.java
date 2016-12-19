package fr.aresrpg.eratz.domain.data.player.info;

import fr.aresrpg.dofus.structures.stat.Stat;
import fr.aresrpg.dofus.structures.stat.StatValue;
import fr.aresrpg.eratz.domain.data.dofus.player.*;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.object.Spell;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @since
 */
public class StatsInfo extends Info {

	private Genre sexe;
	private Classe classe;
	private int xp;
	private int minXp;
	private int maxXp;
	private int statsPoint;
	private int spellsPoints;
	private int life;
	private int lifeMax;
	private int energy;
	private int energyMax;
	private int initiative;
	private int prospection;
	private Map<Stat, StatValue> stats;
	private int lvl;
	private int pods;
	private int maxPods;
	private final java.util.Map<Spells, Spell> spells = new HashMap<>();

	/**
	 * @param perso
	 */
	public StatsInfo(Perso perso) {
		super(perso);
	}

	public StatValue getStat(Stat stat) {
		return getStats().get(stat);
	}

	public int getPA() {
		return getStat(Stat.PA).getTotal();
	}

	public int getPM() {
		return getStat(Stat.PM).getTotal();
	}

	/**
	 * @return the xp
	 */
	public int getXp() {
		return xp;
	}

	/**
	 * @param xp
	 *            the xp to set
	 */
	public void setXp(int xp) {
		this.xp = xp;
	}

	/**
	 * @return the minXp
	 */
	public int getMinXp() {
		return minXp;
	}

	/**
	 * @param minXp
	 *            the minXp to set
	 */
	public void setMinXp(int minXp) {
		this.minXp = minXp;
	}

	/**
	 * @return the maxXp
	 */
	public int getMaxXp() {
		return maxXp;
	}

	/**
	 * @param maxXp
	 *            the maxXp to set
	 */
	public void setMaxXp(int maxXp) {
		this.maxXp = maxXp;
	}

	/**
	 * @return the statsPoint
	 */
	public int getStatsPoint() {
		return statsPoint;
	}

	/**
	 * @param statsPoint
	 *            the statsPoint to set
	 */
	public void setStatsPoint(int statsPoint) {
		this.statsPoint = statsPoint;
	}

	/**
	 * @return the spellsPoints
	 */
	public int getSpellsPoints() {
		return spellsPoints;
	}

	/**
	 * @param spellsPoints
	 *            the spellsPoints to set
	 */
	public void setSpellsPoints(int spellsPoints) {
		this.spellsPoints = spellsPoints;
	}

	/**
	 * @return the lifeMax
	 */
	public int getLifeMax() {
		return lifeMax;
	}

	/**
	 * @param lifeMax
	 *            the lifeMax to set
	 */
	public void setLifeMax(int lifeMax) {
		this.lifeMax = lifeMax;
	}

	/**
	 * @return the energy
	 */
	public int getEnergy() {
		return energy;
	}

	/**
	 * @param energy
	 *            the energy to set
	 */
	public void setEnergy(int energy) {
		this.energy = energy;
	}

	/**
	 * @return the energyMax
	 */
	public int getEnergyMax() {
		return energyMax;
	}

	/**
	 * @param energyMax
	 *            the energyMax to set
	 */
	public void setEnergyMax(int energyMax) {
		this.energyMax = energyMax;
	}

	/**
	 * @return the initiative
	 */
	public int getInitiative() {
		return initiative;
	}

	/**
	 * @param initiative
	 *            the initiative to set
	 */
	public void setInitiative(int initiative) {
		this.initiative = initiative;
	}

	/**
	 * @return the prospection
	 */
	public int getProspection() {
		return prospection;
	}

	/**
	 * @param prospection
	 *            the prospection to set
	 */
	public void setProspection(int prospection) {
		this.prospection = prospection;
	}

	/**
	 * @param stats
	 *            the stats to set
	 */
	public void setStats(Map<Stat, StatValue> stats) {
		this.stats = stats;
	}

	/**
	 * @return the sexe
	 */
	public Genre getSexe() {
		return sexe;
	}

	/**
	 * @param sexe
	 *            the sexe to set
	 */
	public void setSexe(Genre sexe) {
		this.sexe = sexe;
	}

	/**
	 * @return the classe
	 */
	public Classe getClasse() {
		return classe;
	}

	/**
	 * @param classe
	 *            the classe to set
	 */
	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	/**
	 * @return the life
	 */
	public int getLife() {
		return life;
	}

	/**
	 * @param life
	 *            the life to set
	 */
	public void setLife(int life) {
		this.life = life;
	}

	/**
	 * @return the stats
	 */
	public Map<Stat, StatValue> getStats() {
		return stats;
	}

	/**
	 * @return the lvl
	 */
	public int getLvl() {
		return lvl;
	}

	/**
	 * @param lvl
	 *            the lvl to set
	 */
	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	/**
	 * @return the pods
	 */
	public int getPods() {
		return pods;
	}

	public int getFreePods() {
		return getMaxPods() - getPods();
	}

	public int getPodsPercent() {
		return 100 * getPods() / getMaxPods();
	}

	public boolean isFullPod() {
		return getPodsPercent() >= 95;
	}

	/**
	 * @param pods
	 *            the pods to set
	 */
	public void setPods(int pods) {
		this.pods = pods;
	}

	/**
	 * @return the maxPods
	 */
	public int getMaxPods() {
		return maxPods;
	}

	/**
	 * @param maxPods
	 *            the maxPods to set
	 */
	public void setMaxPods(int maxPods) {
		this.maxPods = maxPods;
	}

	/**
	 * @return the spells
	 */
	public java.util.Map<Spells, Spell> getSpells() {
		return spells;
	}

	@Override
	public String toString() {
		return "StatsInfo [sexe=" + sexe + ", classe=" + classe + ", xp=" + xp + ", minXp=" + minXp + ", maxXp=" + maxXp + ", statsPoint=" + statsPoint + ", spellsPoints=" + spellsPoints + ", life="
				+ life + ", lifeMax=" + lifeMax + ", energy=" + energy + ", energyMax=" + energyMax + ", initiative=" + initiative + ", prospection=" + prospection + ", stats=" + stats + ", lvl="
				+ lvl + ", pods=" + pods + ", maxPods=" + maxPods + ", spells=" + spells + "]";
	}

}
