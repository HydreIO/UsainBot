package fr.aresrpg.eratz.domain.player;

import fr.aresrpg.eratz.domain.dofus.player.*;

import java.util.HashMap;

/**
 * 
 * @since
 */
public class StatsInfo {

	private Genre sexe;
	private Classe classe;
	private int life;
	private int pa;
	private int pm;
	private int lvl;
	private final java.util.Map<Spells, Spell> spells = new HashMap<>();

}
