package fr.aresrpg.eratz.domain.data.player.object;

import fr.aresrpg.eratz.domain.data.dofus.player.Spells;

/**
 * 
 * @since
 */
public class Spell {

	private final Spells type;
	private int spellLvl = 1;
	private int position;
	private int relance;

	public Spell(Spells type) {
		this.type = type;
		this.spellLvl = 1;
	}

	/**
	 * @return the relance
	 */
	public int getRelance() {
		return relance;
	}

	/**
	 * @param relance
	 *            the relance to set
	 */
	public void setRelance(int relance) {
		this.relance = relance;
	}

	public void decrementRelance() {
		this.relance--;
		if (this.relance < 0) this.relance = 0;
	}

	/**
	 * @return the minPo
	 */
	public int getMinPo() {
		return 0;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the maxPo
	 */
	public int getMaxPo() {
		return 10;
	}

	/**
	 * @param spellLvl
	 *            the spellLvl to set
	 */
	public void setSpellLvl(int spellLvl) {
		this.spellLvl = spellLvl;
	}

	/**
	 * @return the type
	 */
	public Spells getType() {
		return type;
	}

	/**
	 * @return the spellLvl
	 */
	public int getSpellLvl() {
		return spellLvl;
	}

	/**
	 * @param lvl
	 *            the lvl to set
	 */
	public void setLvl(int lvl) {
		this.spellLvl = lvl;
	}

	@Override
	public String toString() {
		return "Spell [type=" + type + ", spellLvl=" + spellLvl + ", position=" + position + "]";
	}

}
