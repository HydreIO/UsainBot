package fr.aresrpg.eratz.domain.data.player.object;

import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.dofus.structures.PathDirection;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.data.dofus.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.Perso;

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
		for (Node n : maps.keySet())
			if (near == null || n.distanceManathan(map.getX(), map.getY()) < dist) near = n;
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

	private void joinNearest(Perso perso) {
		BotMap map = perso.getMapInfos().getMap();
		Node nearest = getNearest(map);
		List<Point> path = Pathfinding.getPath(map.getX(), map.getY(), nearest.getX(), nearest.getY(), false);
		val: for (Point p : path) {
			BotMap newmap = perso.getMapInfos().getMap();
			PathDirection dir = Pathfinding.getDirection(newmap.getX(), newmap.getY(), (int) p.getX(), (int) p.getY());
			moveWithDirection(perso, dir);
			for (int i = 0; i < 3; i++) {
				if (!perso.getMapInfos().getMap().equals(newmap)) break; // test pour savoir si le bot n'a pas réussi à changer de map
				tryUnblock(perso, dir); // on essaye une autre direction
				if (!perso.getMapInfos().getMap().equals(newmap)) break val; // changement map réussi, on sort pour recalculer un chemin
			}
			if (perso.getMapInfos().getMap().equals(newmap)) { // si tjr pas changé de map on abandonne
				perso.getAbilities().getBaseAbility().speak(Chat.ADMIN, "Imposible de rejoindre la route " + label + " ! Blocké en %pos%");
				perso.crashReport("Imposible de rejoindre la route désignée ! Blocké en [" + p.getX() + "," + p.getY() + "]");
			}
		}
	}

	private void tryUnblock(Perso perso, PathDirection lastDir) {

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
