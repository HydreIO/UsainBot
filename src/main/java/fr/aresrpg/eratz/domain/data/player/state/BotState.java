package fr.aresrpg.eratz.domain.data.player.state;

import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.tofumanchou.domain.data.enums.DofusMobs;

import java.awt.Point;
import java.util.*;

public class BotState {
	public List<Point> path = new ArrayList<>();
	public Set<Interractable> ressources = new HashSet<>();
	public Set<DofusMobs> mobs = new HashSet<>();

	public boolean changeMap;
	public boolean harvest;
	public boolean fightSelectedMobs;
	public boolean joinFights;
	public boolean fightAllMob;
	public boolean canGoIndoor;
	public boolean goToBank;

	public int currentRessource = -1;
	public Point needToGo = null;

	public void addPath(int x, int y) {
		path.add(new Point(x, y));
	}
}