package fr.aresrpg.eratz.domain.data.player.info;

import fr.aresrpg.eratz.domain.data.dofus.player.*;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.object.Spell;

import java.util.HashMap;

/**
 * 
 * @since
 */
public class StatsInfo extends Info {

	private Genre sexe;
	private Classe classe;
	private int life;
	private int pa;
	private int pm;
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
	 * @return the pa
	 */
	public int getPa() {
		return pa;
	}

	/**
	 * @param pa
	 *            the pa to set
	 */
	public void setPa(int pa) {
		this.pa = pa;
	}

	/**
	 * @return the pm
	 */
	public int getPm() {
		return pm;
	}

	/**
	 * @param pm
	 *            the pm to set
	 */
	public void setPm(int pm) {
		this.pm = pm;
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
		return "StatsInfo [sexe=" + sexe + ", classe=" + classe + ", life=" + life + ", pa=" + pa + ", pm=" + pm + ", lvl=" + lvl + ", pods=" + pods + ", maxPods=" + maxPods + ", spells=" + spells
				+ "]";
	}

}
