/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.ia;

import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.data.dofus.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.object.Road;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Les routes sont des long chemin qui servent à ramener les bots au zaap astrub, point de départ pour tout les trajets
 * 
 * @since
 */
public class Roads {

	// je sais c'est pas terrible le consumer a la place de la direction mais j'en ai besoin si jamais on veut preciser une cellid
	public static final Road PLAINE_ROCHEUSE = builder().withMap(-16, -60, down()).withMap(-16, -59, down()).withMap(-16, -58, down()).withMap(-16, -57, down()).withMap(-16, -56, down())
			.withMap(-16, -55, down()).withMap(-16, -54, down()).withMap(-16, -53, down()).withMap(-16, -52, down()).withMap(-16, -51, down()).withMap(-16, -50, down()).withMap(-16, -49, down())
			.withMap(-16, -48, down()).withMap(-16, -47, down()).withMap(-16, -46, down()).withMap(-16, -45, down()).withMap(-16, -44, down()).withMap(-16, -43, down()).withMap(-16, -42, down())
			.withMap(-16, -41, down()).withMap(-16, -40, down()).withMap(-16, -39, down()).withMap(-16, -38, down()).withMap(-16, -37, down()).withMap(-16, -36, right()).withMap(-15, -36, right())
			.withMap(-14, -36, right()).withMap(-13, -36, right()).withMap(-12, -36, right())
			.withMap(-11, -36, right()) // intersection foire du trool
			.withMap(-10, -36, right()).withMap(-9, -36, right()).withMap(-8, -36, right()).withMap(-7, -36, right()).withMap(-6, -36, right()).withMap(-5, -36, right()).withMap(-4, -36, right())
			.withMap(-3, -36, down()).withMap(-3, -35, down()).withMap(-3, -34, down()).withMap(-3, -33, down()).withMap(-3, -32, down()).withMap(-3, -31, down()).withMap(-3, -30, down())
			.withMap(-3, -29, down()).withMap(-3, -28, down()).withMap(-3, -27, down()).withMap(-3, -26, down()).withMap(-3, -25, down()).withMap(-3, -24, down()).withMap(-3, -23, down())
			.withMap(-3, -22, down()).withMap(-3, -21, right()).withMap(-2, -21, down()).withMap(-2, -20, right())
			.withMap(-1, -20, down()) // porte gauche astrub
			.withMap(-1, -19, right()).withMap(0, -19, right()).withMap(1, -19, right()).withMap(2, -19, right()).withMap(3, -19, right()).build().setLabel("Plaine Rocheuse");

	public static final Road CHAMPS_ASTRUB = builder().withMap(6, -28, down()).withMap(6, -27, down()).withMap(6, -26, down()).withMap(6, -25, down()).withMap(6, -24, down()).withMap(6, -23, down())
			.withMap(6, -22, left()).withMap(5, -22, down()).withMap(5, -21, left()).withMap(4, -21, down()).withMap(4, -20, down()).build().setLabel("Champs d'Astrub");

	public static final Road AMAKNA = builder().withMap(3, 31, up()).withMap(3, 30, up()).withMap(3, 29, up()).withMap(3, 28, up()).withMap(3, 27, up()).withMap(3, 26, up()).withMap(3, 25, up())
			.withMap(3, 24, up()).withMap(3, 23, toCell(19)).withMap(3, 22, up()).withMap(3, 21, up()).withMap(3, 20, up()).withMap(3, 19, up()).withMap(3, 18, up()).withMap(3, 17, up())
			.withMap(3, 16, up()).withMap(3, 15, up()).withMap(3, 14, up()).withMap(3, 13, up()).withMap(3, 12, up()).withMap(3, 11, up()).withMap(3, 10, up()).withMap(3, 9, up()).withMap(3, 8, up())
			.withMap(3, 7, up()).withMap(3, 6, up()).withMap(3, 5, up()).withMap(3, 4, up()).withMap(3, 3, up()).withMap(3, 2, up()).withMap(3, 1, up()).withMap(3, 0, up()).withMap(3, -1, up())
			.withMap(3, -2, up()).withMap(3, -3, right()).withMap(4, -3, up()).withMap(4, -4, up()).withMap(4, -5, up()).withMap(4, -6, up()).withMap(4, -7, up()).withMap(4, -8, up())
			.withMap(4, -9, up()).withMap(4, -10, up()).withMap(4, -11, up()).withMap(4, -12, up()).withMap(4, -13, up()).withMap(4, -14, toCell(23)).withMap(4, -15, up()).withMap(4, -17, up())
			.withMap(4, -18, up()).build().setLabel("Amakna");

	public static final Road[] ALL = { PLAINE_ROCHEUSE, CHAMPS_ASTRUB, AMAKNA };

	public static RoadBuilder builder() {
		return new RoadBuilder();
	}

	public static Road nearestRoad(Perso perso) {
		Road near = null;
		int dist = Integer.MAX_VALUE;
		BotMap map = perso.getMapInfos().getMap();
		for (Road r : ALL) {
			Node no = r.getNearest(map);
			if (near == null || no.distanceManathan(map.getX(), map.getY()) < dist) near = r;
		}
		return near;
	}

	private static Consumer<Perso> up() {
		return p -> p.getNavigation().moveUp();
	}

	private static Consumer<Perso> down() {
		return p -> p.getNavigation().moveDown();
	}

	private static Consumer<Perso> left() {
		return p -> p.getNavigation().moveLeft();
	}

	private static Consumer<Perso> right() {
		return p -> p.getNavigation().moveRight();
	}

	private static Consumer<Perso> toCell(int cellid) {
		return p -> p.getNavigation().moveToCell(cellid, true);
	}

	public static class RoadBuilder {
		private Map<Node, Consumer<Perso>> path = new LinkedHashMap();

		public RoadBuilder withMap(int x, int y, Consumer<Perso> move) {
			path.put(new Node(x, y), move);
			return this;
		}

		public Road build() {
			return new Road(path);
		}
	}

}
