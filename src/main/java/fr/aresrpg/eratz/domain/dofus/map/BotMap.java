package fr.aresrpg.eratz.domain.dofus.map;

import fr.aresrpg.dofus.structures.map.DofusMap;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @since
 */
public class BotMap {

	private DofusMap dofusMap;
	private Set<Ressource> ressources = new HashSet<>();

	public BotMap(DofusMap m, Set<Ressource> res) {
		this.dofusMap = m;
		this.ressources = res;
	}

	/**
	 * @return the dofusMap
	 */
	public DofusMap getDofusMap() {
		return dofusMap;
	}

	public static BotMap fromDofusMap(DofusMap map) {
		return new BotMap(map, new HashSet<>());
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
