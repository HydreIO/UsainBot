package fr.aresrpg.eratz.domain.dofus.map;

import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.eratz.domain.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.player.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @since
 */
public class BotMap {

	private DofusMap dofusMap;
	private Set<Ressource> ressources = new HashSet<>();
	private Set<Fight> fights = new HashSet<>();
	private Set<Player> players = new HashSet<>();

	public BotMap(DofusMap m) {
		this.dofusMap = m;
	}

	/**
	 * @return the players
	 */
	public Set<Player> getPlayers() {
		return players;
	}

	/**
	 * @param players
	 *            the players to set
	 */
	public void setPlayers(Set<Player> players) {
		this.players = players;
	}

	/**
	 * @return the fights
	 */
	public Set<Fight> getFights() {
		return fights;
	}

	/**
	 * @param ressources
	 *            the ressources to set
	 */
	public void setRessources(Set<Ressource> ressources) {
		this.ressources = ressources;
	}

	/**
	 * @param fights
	 *            the fights to set
	 */
	public void setFights(Set<Fight> fights) {
		this.fights = fights;
	}

	/**
	 * @return the dofusMap
	 */
	public DofusMap getDofusMap() {
		return dofusMap;
	}

	public static BotMap fromDofusMap(DofusMap map) {
		return new BotMap(map);
	}

	public int getX() {
		return dofusMap.getX();
	}

	public int getZ() {
		return dofusMap.getZ();
	}

	/**
	 * @return the ressources
	 */
	public Set<Ressource> getRessources() {
		return ressources;
	}
}
