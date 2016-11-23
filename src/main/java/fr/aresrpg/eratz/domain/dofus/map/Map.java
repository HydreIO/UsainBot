package fr.aresrpg.eratz.domain.dofus.map;

import fr.aresrpg.eratz.domain.dofus.mob.Mob;
import fr.aresrpg.eratz.domain.dofus.ressource.Ressource;
import fr.aresrpg.eratz.domain.player.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @since
 */
public class Map {

	private int id;
	private Set<Player> players = new HashSet<>();
	private Set<Ressource> ressources = new HashSet<>();
	private Set<Mob> mobs = new HashSet<>();

	private Map(int id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the players
	 */
	public Set<Player> getPlayers() {
		return players;
	}

	/**
	 * @return the ressources
	 */
	public Set<Ressource> getRessources() {
		return ressources;
	}

	/**
	 * @return the mobs
	 */
	public Set<Mob> getMobs() {
		return mobs;
	}

}
