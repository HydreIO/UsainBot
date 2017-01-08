package fr.aresrpg.eratz.domain.data.player.state;

import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.dofus.structures.Orientation;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.tofumanchou.domain.data.enums.DofusMobs;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

import java.awt.Point;
import java.util.*;

public class BotState {
	public List<Point> path = new ArrayList<>();
	public Set<Interractable> ressources = new HashSet<>();
	public Set<DofusMobs> mobs = new HashSet<>();

	// harvest
	public boolean changeMap;
	public boolean canGoIndoor;
	public boolean goToBank;

	public int currentRessource = -1;
	public Point needToGo = null;
	public Set<Point> lastBlockedMap = new HashSet<>();
	public Pair<ManchouMap, Pair<Integer, Orientation>> lastCellMoved = null;
	public List<ManchouMap> visitedMaps = new ArrayList<>();

	public void addPath(int x, int y) {
		path.add(new Point(x, y));
	}
}