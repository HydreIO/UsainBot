/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.player;

import fr.aresrpg.dofus.structures.character.Character;
import fr.aresrpg.eratz.domain.dofus.player.Classe;
import fr.aresrpg.eratz.domain.dofus.player.Genre;

/**
 * 
 * @since
 */
public class Player {

	private int id;
	private final String pseudo;
	private int lvl;
	private int life;
	private int cellid;
	private Genre sexe;
	private Classe classe;
	private int pa;
	private int pm;
	// ajouter resi

	public Player(int id, String pseudo, Classe classe, Genre sexe) {
		this.id = id;
		this.sexe = sexe;
		this.pseudo = pseudo;
		this.classe = classe;
	}

	public static Player fromCharacter(Character c) {
		return new Player(c.getId(), c.getPseudo(), null, c.getSex() == 0 ? Genre.MALE : Genre.FEMALE); // TODO pour le genre j'ai mis au pif
	}

	/**
	 * @return the sexe
	 */
	public Genre getSexe() {
		return sexe;
	}

	/**
	 * @return the lvl
	 */
	public int getLvl() {
		return lvl;
	}

	/**
	 * @param cellid
	 *            the cellid to set
	 */
	public void setCellid(int cellid) {
		this.cellid = cellid;
	}

	/**
	 * @param life
	 *            the life to set
	 */
	public void setLife(int life) {
		this.life = life;
	}

	/**
	 * @param lvl
	 *            the lvl to set
	 */
	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	/**
	 * @param pa
	 *            the pa to set
	 */
	public void setPa(int pa) {
		this.pa = pa;
	}

	/**
	 * @param pm
	 *            the pm to set
	 */
	public void setPm(int pm) {
		this.pm = pm;
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
