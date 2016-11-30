/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
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
	private int x;
	private int z;
	private Area area;
	private SubArea subarea;
	private Set<Player> players = new HashSet<>();
	private Set<Ressource> ressources = new HashSet<>();
	private Set<Mob> mobs = new HashSet<>();

	public Map(int id, int x, int z, Area area, SubArea subarea) {
		this.id = id;
		this.x = x;
		this.z = z;
		this.area = area;
		this.subarea = subarea;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Map)) return false;
		Map map = (Map) obj;
		return x == map.getX() && z == map.getZ() && area == map.getArea() && subarea == map.getSubarea();
	}

	/**
	 * @return the area
	 */
	public Area getArea() {
		return area;
	}

	/**
	 * @return the subarea
	 */
	public SubArea getSubarea() {
		return subarea;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the z
	 */
	public int getZ() {
		return z;
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
