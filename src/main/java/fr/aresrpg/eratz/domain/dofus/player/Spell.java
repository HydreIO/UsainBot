package fr.aresrpg.eratz.domain.dofus.player;

/**
 * 
 * @since
 */
public class Spell {

	private final Spells type;
	private int spellLvl;
	private int po;

	public Spell(Spells type) {
		this.type = type;
		this.spellLvl = 1;
	}

	/**
	 * @return the po
	 */
	public int getPo() {
		return po;
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

}
