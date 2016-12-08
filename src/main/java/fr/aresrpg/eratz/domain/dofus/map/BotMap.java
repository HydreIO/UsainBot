package fr.aresrpg.eratz.domain.dofus.map;

import fr.aresrpg.dofus.structures.map.DofusMap;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @since
 */
public class BotMap {

	private int x, z;
	private DofusMap dofusMap;
	private Set<Ressource> ressources = new HashSet<>();

	/**
	 * @return the dofusMap
	 */
	public DofusMap getDofusMap() {
		return dofusMap;
	}

	/**
	 * @return the ressources
	 */
	public Set<Ressource> getRessources() {
		return ressources;
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

}
