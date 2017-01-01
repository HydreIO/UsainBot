package fr.aresrpg.eratz.domain.data.player.object;

import static fr.aresrpg.eratz.domain.BotFather.LOGGER;

import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.structures.Orientation;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.data.player.BotPerso;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

/**
 * 
 * @since
 */
public class Road {

	private String label = "undefined";
	private Map<Node, Consumer<BotPerso>> maps = new LinkedHashMap<>();

	public Road(Map<Node, Consumer<BotPerso>> road) {
		this.maps = road;
	}

	public Road setLabel(String label) {
		this.label = label;
		return this;
	}

	public Node getNearest(BotMap map) {
		Node near = null;
		int dist = Integer.MAX_VALUE;
		for (Node n : maps.keySet()) {
			int di = n.distanceManathan(map.getX(), map.getY());
			if (near == null || di < dist) {
				dist = di;
				near = n;
			}
		}
		return near;
	}

	public boolean isOnRoad(BotPerso perso) {
		for (Node n : maps.keySet())
			if (perso.getPerso().getMap().isOnCoords(n.getX(), n.getY())) return true;
		return false;
	}

	public void takeRoad(BotPerso perso) {
		if (!isOnRoad(perso)) {
			Node nearest = getNearest(perso.getMapInfos().getMap());
			perso.getNavigation().joinCoords(nearest.getX(), nearest.getY());
		}
		LOGGER.success(perso.getPseudo() + " à rejoint la route !");
		boolean pos = false;
		BotMap map = perso.getMapInfos().getMap();
		for (Entry<Node, Consumer<BotPerso>> entry : maps.entrySet()) {
			Node current = entry.getKey();
			if (current.getX() == map.getX() && current.getY() == map.getY()) pos = true;
			if (!pos) continue;
			entry.getValue().accept(perso);
		}
	}

	/**
	 * @return the maps
	 */
	public Map<Node, Consumer<BotPerso>> getMaps() {
		return maps;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	private Orientation getDifferentDir(Orientation base) {
		Orientation dir = base;
		do {
			dir = Orientation.values()[Randoms.nextInt(Orientation.values().length - 1)];
		} while (isSame(dir, base));
		return dir;
	}

	private boolean isSame(Orientation dir, Orientation other) {
		if (dir == null || other == null) return false;
		switch (dir) {
			case DOWN:
			case DOWN_LEFT:
			case DOWN_RIGHT:
				return other == Orientation.DOWN || other == Orientation.DOWN_LEFT || other == Orientation.DOWN_RIGHT;
			case LEFT:
				return other == Orientation.LEFT;
			case RIGHT:
				return other == Orientation.RIGHT;
			case UP:
			case UP_LEFT:
			case UP_RIGHT:
				return other == Orientation.UP || other == Orientation.UP_LEFT || other == Orientation.UP_RIGHT;
		}
		return false;
	}

	private void moveWithDirection(BotPerso perso, Orientation dir) {
		switch (dir) {
			case DOWN:
			case DOWN_LEFT:
			case DOWN_RIGHT:
				perso.getPerso().moveDown();
				return;
			case LEFT:
				perso.getPerso().moveLeft();
				return;
			case RIGHT:
				perso.getPerso().moveRight();
				return;
			case UP:
			case UP_LEFT:
			case UP_RIGHT:
				perso.getPerso().moveUp();
				return;
		}
	}

}
