package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.eratz.domain.ia.Interrupt;
import fr.aresrpg.tofumanchou.infra.data.ManchouCell;

import java.awt.Point;

/**
 * 
 * @since
 */
public class ComparatorUtil {

	public static int comparingDistanceToPlayer(boolean diagonals, int playerCell, ManchouCell first, ManchouCell second) {
		return diagonals ? first.distance(playerCell) - second.distance(playerCell) : first.distanceManathan(playerCell) - second.distanceManathan(playerCell);
	}

	public static int comparingDistanceToPlayer(boolean diagonals, Point playerPos, Point pf, Point ps) {
		return diagonals
				? ((pf.x - playerPos.x) * (pf.x - playerPos.x) + (pf.y - playerPos.y) * (pf.y - playerPos.y))
						- ((ps.x - playerPos.x) * (ps.x - playerPos.x) + (ps.y - playerPos.y) * (ps.y - playerPos.y))
				: (Math.abs(pf.x - playerPos.x) + Math.abs(pf.y - playerPos.y)) - (Math.abs(ps.x - playerPos.x) + Math.abs(ps.y - playerPos.y));
	}

	public static int comparingInterrupt(Interrupt first, Interrupt second) {
		return first.getPriority() - second.getPriority();
	}

}
