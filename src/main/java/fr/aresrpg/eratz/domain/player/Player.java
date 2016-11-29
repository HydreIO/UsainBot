/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.player;

import fr.aresrpg.eratz.domain.dofus.player.Classe;

/**
 * 
 * @since
 */
public class Player {

	private int id;
	private String pseudo;
	private int life;
	private int cellid;
	private Classe classe;
	private int pa;
	private int pm;
	// ajouter resi

	public Player(int id, String pseudo) {
		this.id = id;
		this.pseudo = pseudo;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the pseudo
	 */
	public String getPseudo() {
		return pseudo;
	}

	/**
	 * @return the life
	 */
	public int getLife() {
		return life;
	}

	/**
	 * @return the cellid
	 */
	public int getCellid() {
		return cellid;
	}

	/**
	 * @return the classe
	 */
	public Classe getClasse() {
		return classe;
	}

}
