/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.data;

import fr.aresrpg.dofus.structures.Orientation;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;
import fr.aresrpg.tofumanchou.infra.data.ManchouPerso;

import java.awt.Point;
import java.util.*;
import java.util.function.Predicate;

/**
 * Les routes sont des long chemin qui servent à ramener les bots au zaap astrub, point de départ pour tout les trajets
 * 
 * @since
 */
public class Roads {

	private static Map<Point, MapRestriction> mapRestrictions = new HashMap<>();
	private static Map<ManchouMap, Set<Integer>> cellRestrictions = new HashMap<>();
	private static final Roads roads = new Roads();

	private Roads() {
		resetRestrictions();
	}

	public static void resetRestrictions() {
		mapRestrictions.clear();
		addRestriction(27, -45, false, true, true, true); // grobe
		addRestriction(29, -47, true, true, true, false); // grobe
		addRestriction(11, 29, true, true, true, false); // sufokia
		addRestriction(11, 22, true, false, true, true); // sufokia
		addRestriction(12, -21, true, true, true, false); // pandala
		addRestriction(5, 18, true, false, true, true); // noyer
		addRestriction(5, 7, false, true, true, true); // dj tofu fuck
		addRestriction(4, 6, true, true, true, false); // dj tofu
		addRestriction(5, 5, true, false, true, true); // dj tofu
		addRestriction(6, 6, true, true, false, true); // dj tofu
	}

	private static void addRestriction(int x, int y, boolean up, boolean down, boolean left, boolean right) {
		MapRestriction r = new MapRestriction(x, y);
		r.setMoveDown(down);
		r.setMoveLeft(left);
		r.setMoveRight(right);
		r.setMoveUp(up);
		mapRestrictions.put(new Point(x, y), r);
	}

	/**
	 * @return the mapRestrictions
	 */
	public static Map<Point, MapRestriction> getMapRestrictions() {
		return mapRestrictions;
	}

	public static MapRestriction getRestriction(Point p) {
		MapRestriction r = getMapRestrictions().get(p);
		if (r == null) getMapRestrictions().put(p, r = new MapRestriction(p.x, p.y));
		return r;
	}

	public static boolean canMove(Point from, Point to) {
		MapRestriction r = getRestriction(from);
		Orientation dir = Pathfinding.getDirectionForMap(from.x, from.y, to.x, to.y);
		return r.canMove(dir);
	}

	/**
	 * @param map
	 * @param cellid
	 * @return true si la cell peut etre use en tant que teleporter
	 */
	public static boolean canUseToTeleport(ManchouMap map, int cellid) {
		if (!cellRestrictions.containsKey(map)) return true;
		Set<Integer> set = cellRestrictions.get(map);
		return !set.contains(cellid);
	}

	/**
	 * Notifie qu'une cell est un fake teleporter et ne peut donc pas être use
	 * 
	 * @param map
	 * @param cellid
	 */
	public static void notifyCantUse(ManchouMap map, int cellid) {
		Set<Integer> set = cellRestrictions.get(map);
		if (set == null) cellRestrictions.put(map, set = new HashSet<>());
		set.add(cellid);
	}

	public static void checkMap(ManchouMap m, BotPerso perso) {
		ManchouPerso p = perso.getPerso();
		Point point = new Point(m.getX(), m.getY());
		Predicate<Integer> pred = i -> canUseToTeleport(m, i);
		if (p.getUpTp(pred.negate()) == -1) getRestriction(point).setMoveUp(false);
		if (p.getDownTp(pred.negate()) == -1) getRestriction(point).setMoveDown(false);
		if (p.getLeftTp(pred.negate()) == -1) getRestriction(point).setMoveLeft(false);
		if (p.getRightTp(pred.negate()) == -1) getRestriction(point).setMoveRight(false);
	}

	public static class MapRestriction {
		private int x, y;
		private boolean moveUp = true, moveDown = true, moveLeft = true, moveRight = true;

		public MapRestriction(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public static MapRestriction fromPoint(Point p) {
			return new MapRestriction(p.x, p.y);
		}

		public boolean canMove(Orientation dir) { // return true si le chemin n'est pas bloqué
			switch (dir) {
				case DOWN:
				case DOWN_LEFT:
				case DOWN_RIGHT:
					return canMoveDown();
				case LEFT:
					return canMoveLeft();
				case RIGHT:
					return canMoveRight();
				case UP:
				case UP_LEFT:
				case UP_RIGHT:
					return canMoveUp();
			}
			return true;
		}

		public void setCantMove(Orientation dir) { // indique qu'on ne peut pas aller dans cette direction depuis la case actuelle
			switch (dir) {
				case DOWN:
				case DOWN_LEFT:
				case DOWN_RIGHT:
					moveDown = false;
					return;
				case LEFT:
					moveLeft = false;
					return;
				case RIGHT:
					moveRight = false;
					return;
				case UP:
				case UP_LEFT:
				case UP_RIGHT:
					moveUp = false;
					return;
			}
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) return false;
			if (obj == this) return true;
			return obj instanceof MapRestriction && ((MapRestriction) obj).posEquals(this);
		}

		public boolean posEquals(MapRestriction m) {
			return m.x == x && m.y == y;
		}

		/**
		 * @return the x
		 */
		public int getX() {
			return x;
		}

		/**
		 * @param x
		 *            the x to set
		 */
		public void setX(int x) {
			this.x = x;
		}

		/**
		 * @return the y
		 */
		public int getY() {
			return y;
		}

		/**
		 * @param y
		 *            the y to set
		 */
		public void setY(int y) {
			this.y = y;
		}

		/**
		 * @return the moveUp
		 */
		public boolean canMoveUp() {
			return moveUp;
		}

		/**
		 * @param moveUp
		 *            the moveUp to set
		 */
		public void setMoveUp(boolean moveUp) {
			this.moveUp = moveUp;
		}

		/**
		 * @return the moveDown
		 */
		public boolean canMoveDown() {
			return moveDown;
		}

		/**
		 * @param moveDown
		 *            the moveDown to set
		 */
		public void setMoveDown(boolean moveDown) {
			this.moveDown = moveDown;
		}

		/**
		 * @return the moveLeft
		 */
		public boolean canMoveLeft() {
			return moveLeft;
		}

		/**
		 * @param moveLeft
		 *            the moveLeft to set
		 */
		public void setMoveLeft(boolean moveLeft) {
			this.moveLeft = moveLeft;
		}

		/**
		 * @return the moveRight
		 */
		public boolean canMoveRight() {
			return moveRight;
		}

		/**
		 * @param moveRight
		 *            the moveRight to set
		 */
		public void setMoveRight(boolean moveRight) {
			this.moveRight = moveRight;
		}

		@Override
		public String toString() {
			return "MapRestriction [x=" + x + ", y=" + y + ", moveUp=" + moveUp + ", moveDown=" + moveDown + ", moveLeft=" + moveLeft + ", moveRight=" + moveRight + "]";
		}

	}

}
