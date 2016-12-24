package fr.aresrpg.eratz.domain.data.player.object;

import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.dofus.structures.PathDirection;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.data.dofus.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.Roads;
import fr.aresrpg.eratz.domain.ia.Roads.MapRestriction;

import java.awt.Point;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;

/**
 * 
 * @since
 */
public class Road {

	private String label = "undefined";
	private Map<Node, Consumer<Perso>> maps = new LinkedHashMap<>();

	public Road(Map<Node, Consumer<Perso>> road) {
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

	public boolean isOnRoad(Perso perso) {
		BotMap map = perso.getMapInfos().getMap();
		for (Node n : maps.keySet())
			if (n.getX() == map.getX() && n.getY() == map.getY()) return true;
		return false;
	}

	public void takeRoad(Perso perso) {
		if (!isOnRoad(perso)) joinNearest(perso);
		boolean pos = false;
		BotMap map = perso.getMapInfos().getMap();
		for (Entry<Node, Consumer<Perso>> entry : maps.entrySet()) {
			Node current = entry.getKey();
			if (current.getX() == map.getX() && current.getY() == map.getY()) pos = true;
			if (!pos) continue;
			entry.getValue().accept(perso);
		}
	}

	/**
	 * @return the maps
	 */
	public Map<Node, Consumer<Perso>> getMaps() {
		return maps;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	private void joinNearest(Perso perso) {
		BotMap map = perso.getMapInfos().getMap();
		Node nearest = getNearest(map);
		List<Point> path = Pathfinding.getMapPath(map.getX(), map.getY(), nearest.getX(), nearest.getY(), Roads::canMove);
		if (path == null) {
			perso.getAbilities().getBaseAbility().speak(Chat.ADMIN, "Impossible de rejoindre la route " + label + " ! Blocké en %pos%");
			perso.crashReport("Impossible de rejoindre la route désignée ! Blocké en [" + map.getX() + "," + map.getY() + "]");
			return;
		}
		path.remove(0);
		for (Point p : path) {
			BotMap newmap = perso.getMapInfos().getMap();
			PathDirection dir = Pathfinding.getDirectionForMap(newmap.getX(), newmap.getY(), (int) p.getX(), (int) p.getY());
			if (dir == null) {
				perso.crashReport("Impossible de trouver la direction pour aller de [" + newmap.getX() + "," + newmap.getY() + "] vers [" + p.x + "," + p.y + "]");
				return;
			}
			moveWithDirection(perso, dir);
			if (perso.getBotInfos().isBlockedOnACell()) {
				MapRestriction res = Roads.getRestriction(new Point(newmap.getX(), newmap.getY()));
				res.setCantMove(dir);
				joinNearest(perso);
				return;
			}
		}
	}

	private PathDirection getDifferentDir(PathDirection base) {
		PathDirection dir = base;
		do {
			dir = PathDirection.values()[Randoms.nextInt(PathDirection.values().length - 1)];
		} while (isSame(dir, base));
		return dir;
	}

	private boolean isSame(PathDirection dir, PathDirection other) {
		if (dir == null || other == null) return false;
		switch (dir) {
			case DOWN:
			case DOWN_LEFT:
			case DOWN_RIGHT:
				return other == PathDirection.DOWN || other == PathDirection.DOWN_LEFT || other == PathDirection.DOWN_RIGHT;
			case LEFT:
				return other == PathDirection.LEFT;
			case RIGHT:
				return other == PathDirection.RIGHT;
			case UP:
			case UP_LEFT:
			case UP_RIGHT:
				return other == PathDirection.UP || other == PathDirection.UP_LEFT || other == PathDirection.UP_RIGHT;
		}
		return false;
	}

	private void moveWithDirection(Perso perso, PathDirection dir) {
		switch (dir) {
			case DOWN:
			case DOWN_LEFT:
			case DOWN_RIGHT:
				perso.getNavigation().moveDown();
				return;
			case LEFT:
				perso.getNavigation().moveLeft();
				return;
			case RIGHT:
				perso.getNavigation().moveRight();
				return;
			case UP:
			case UP_LEFT:
			case UP_RIGHT:
				perso.getNavigation().moveUp();
				return;
		}
	}

}
