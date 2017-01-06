package fr.aresrpg.eratz.domain.ia.behavior;

import fr.aresrpg.dofus.structures.item.Interractable;

import java.awt.Point;
import java.util.*;

/**
 * 
 * @since
 */
public abstract class Path {

	private List<Point> coords = new ArrayList<>();
	private Set<Interractable> ressources = new HashSet<>();

	public Path() {
		init();
	}

	protected void addRessource(Interractable ress) {
		ressources.add(ress);
	}

	protected void addCoord(int x, int y) {
		coords.add(new Point(x, y));
	}

	protected abstract void init();

	/**
	 * @return the coords
	 */
	public List<Point> getCoords() {
		return coords;
	}

	/**
	 * @return the ressources
	 */
	public Set<Interractable> getRessources() {
		return ressources;
	}

	public void fillCoords(List<Point> list) {
		list.clear();
		list.addAll(coords);
	}

	public void fillRessources(Set<Interractable> set) {
		set.clear();
		set.addAll(ressources);
	}

}
