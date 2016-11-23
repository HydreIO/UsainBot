package fr.aresrpg.eratz.domain.dofus.player;

/**
 * 
 * @since
 */
public class Spell {

	private final Spells type;
	private int lvl;

	public Spell(Spells type) {
		this.type = type;
		this.lvl = 1;
	}

	/**
	 * @return the type
	 */
	public Spells getType() {
		return type;
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

}
