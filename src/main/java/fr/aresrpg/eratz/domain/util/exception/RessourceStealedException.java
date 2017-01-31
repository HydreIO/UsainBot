package fr.aresrpg.eratz.domain.util.exception;

import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Player;

/**
 * 
 * @since
 */
public class RessourceStealedException extends RuntimeException {

	private final Interractable ressource;
	private final Player whoStealed;

	public RessourceStealedException(Interractable ress, Player whoStealed) {
		super(ress + " stealed by " + whoStealed.getPseudo());
		this.ressource = ress;
		this.whoStealed = whoStealed;
	}

	/**
	 * @return the ressource
	 */
	public Interractable getRessource() {
		return ressource;
	}

	/**
	 * @return the whoStealed
	 */
	public Player getWhoStealed() {
		return whoStealed;
	}

	@Override
	public String toString() {
		return "RessourceStealedException [ressource=" + ressource + ", whoStealed=" + whoStealed + "]";
	}

}
