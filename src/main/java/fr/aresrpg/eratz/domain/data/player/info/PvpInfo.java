package fr.aresrpg.eratz.domain.data.player.info;

import fr.aresrpg.dofus.structures.Rank;
import fr.aresrpg.dofus.structures.game.Alignement;
import fr.aresrpg.eratz.domain.data.player.Perso;

/**
 * 
 * @since
 */
public class PvpInfo extends Info {

	private Alignement alignment;
	private Rank rank;

	/**
	 * @param perso
	 */
	public PvpInfo(Perso perso) {
		super(perso);
	}

	/**
	 * @return the alignment
	 */
	public Alignement getAlignment() {
		return alignment;
	}

	/**
	 * @param alignment
	 *            the alignment to set
	 */
	public void setAlignment(Alignement alignment) {
		this.alignment = alignment;
	}

	/**
	 * @return the rank
	 */
	public Rank getRank() {
		return rank;
	}

	/**
	 * @param rank
	 *            the rank to set
	 */
	public void setRank(Rank rank) {
		this.rank = rank;
	}

	@Override
	public String toString() {
		return "PvpInfo [alignment=" + alignment + ", rank=" + rank + "]";
	}

}
