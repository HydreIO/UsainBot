package fr.aresrpg.eratz.domain.ia.path;

import fr.aresrpg.dofus.structures.item.Interractable;

import java.util.*;

/**
 * 
 * @since
 */
public abstract class Path {

	private List<Integer> maps = new ArrayList<>();
	private Set<Interractable> ressources = new HashSet<>();

	public Path() {
		init();
	}

	protected void addRessource(Interractable ress) {
		ressources.add(ress);
	}

	protected void addMap(int mapid) {
		maps.add(mapid);
	}

	protected abstract void init();

	/**
	 * @return the maps
	 */
	public List<Integer> getMaps() {
		return maps;
	}

	/**
	 * @return the ressources
	 */
	public Set<Interractable> getRessources() {
		return ressources;
	}

	public void fillMaps(Collection<Integer> list) {
		list.clear();
		list.addAll(maps);
	}

	public void fillRessources(Set<Interractable> set) {
		set.clear();
		set.addAll(ressources);
	}

}
