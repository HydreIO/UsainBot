package fr.aresrpg.eratz.domain.dofus.player;

/**
 * 
 * @since
 */
public class Spell {

	private final Spells type;
	private int zone;
	private int lvl;

	public Spell(Spells type, int zone) {
		this.type = type;
		this.lvl = 1;
		this.zone = zone;
	}

	/**
	 * @return the zone
	 */
	public int getZone() {
		return zone;
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
