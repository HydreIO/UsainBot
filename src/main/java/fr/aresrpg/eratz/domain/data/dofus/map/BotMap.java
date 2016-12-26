package fr.aresrpg.eratz.domain.data.dofus.map;

import fr.aresrpg.dofus.protocol.game.movement.*;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.util.StringJoiner;
import fr.aresrpg.eratz.domain.data.MapsData;
import fr.aresrpg.eratz.domain.data.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.data.player.object.Ressource;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 
 * @since
 */
public class BotMap {

	private DofusMap dofusMap;
	private String area;
	private String subarea;
	private int x;
	private int y;
	private Set<Ressource> ressources = new HashSet<>();
	private Set<Fight> fights = new HashSet<>();
	private CopyOnWriteArraySet<MovementPlayer> players = new CopyOnWriteArraySet<>();
	private CopyOnWriteArraySet<MovementMonsterGroup> mobs = new CopyOnWriteArraySet<>();
	private CopyOnWriteArraySet<MovementNpc> npcs = new CopyOnWriteArraySet<>();

	public BotMap(DofusMap m) {
		this.dofusMap = m;
		Point coords = MapsData.getCoords(m.getId());
		this.x = coords.x;
		this.y = coords.y;
		this.area = MapsData.getArea(m.getId());
		this.subarea = MapsData.getSubArea(m.getId());
	}

	public String getInfos() {
		return area + " (" + subarea + ")[" + x + "," + y + "]";
	}

	public boolean hasMobOn(int cellid) {
		for (MovementMonsterGroup g : mobs)
			if (g.getCellId() == cellid) return true;
		return false;
	}

	public String getCoordsInfos() {
		return new StringJoiner(",", "[", "]").add(x).add(y).toString();
	}

	public void entityMove(int id, int cellid) {
		if (id > 1000) playerMove(id, cellid);
		else {
			monsterMove(id, cellid);
			npcMove(id, cellid);
		}
	}

	public void playerMove(int id, int cellid) {
		for (MovementPlayer i : players)
			if (i.getId() == id) {
				i.setCellId(cellid);
				return;
			}
	}

	public boolean isOnMap(int id) {
		for (MovementPlayer p : players)
			if (p.getId() == id) return true;
		return false;
	}

	public void monsterMove(int id, int cellid) {
		for (MovementMonsterGroup i : mobs)
			if (i.getId() == id) i.setCellId(cellid);
	}

	public void npcMove(int id, int cellid) {
		for (MovementNpc i : npcs)
			if (i.getId() == id) i.setCellId(cellid);
	}

	public void entityUpdate(MovementAction action) {
		if (action instanceof MovementPlayer) updatePlayer((MovementPlayer) action);
		else if (action instanceof MovementMonsterGroup) updateMobs((MovementMonsterGroup) action);
		else if (action instanceof MovementNpc) updateNpc((MovementNpc) action);
		else throw new IllegalArgumentException(action + " has not a valid type");
	}

	private void updatePlayer(MovementPlayer player) {
		players.remove(player);
		players.add(player);
	}

	public void updateMobs(MovementMonsterGroup group) {
		mobs.remove(group);
		mobs.add(group);
	}

	public void updateNpc(MovementNpc npc) {
		npcs.remove(npc);
		npcs.add(npc);
	}

	public boolean isOnPoint(Point p) {
		return isOnCoords(p.x, p.y);
	}

	public boolean isOnCoords(int x, int y) {
		return x == this.x && y == this.y;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area
	 *            the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the subarea
	 */
	public String getSubarea() {
		return subarea;
	}

	/**
	 * @param subarea
	 *            the subarea to set
	 */
	public void setSubarea(String subarea) {
		this.subarea = subarea;
	}

	/**
	 * @return the mobs
	 */
	public CopyOnWriteArraySet<MovementMonsterGroup> getMobs() {
		return mobs;
	}

	/**
	 * @param mobs
	 *            the mobs to set
	 */
	public void setMobs(CopyOnWriteArraySet<MovementMonsterGroup> mobs) {
		this.mobs = mobs;
	}

	/**
	 * @return the npcs
	 */
	public CopyOnWriteArraySet<MovementNpc> getNpcs() {
		return npcs;
	}

	/**
	 * @param npcs
	 *            the npcs to set
	 */
	public void setNpcs(CopyOnWriteArraySet<MovementNpc> npcs) {
		this.npcs = npcs;
	}

	/**
	 * @param dofusMap
	 *            the dofusMap to set
	 */
	public void setDofusMap(DofusMap dofusMap) {
		this.dofusMap = dofusMap;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @param players
	 *            the players to set
	 */
	public void setPlayers(CopyOnWriteArraySet<MovementPlayer> players) {
		this.players = players;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return the players
	 */
	public Set<MovementPlayer> getPlayers() {
		return players;
	}

	public String getNameOf(int playerId) {
		for (MovementPlayer p : getPlayers())
			if (p.getId() == playerId) return p.getPseudo();
		return "notFound";
	}

	public int getIdOf(String name) {
		for (MovementPlayer p : getPlayers())
			if (p.getPseudo().equalsIgnoreCase(name)) return p.getId();
		return 0;
	}

	public void removeActor(int id) {
		if (id > 1000) removePlayer(id);
		else {
			removeMob(id);
			removeNpc(id);
		}
	}

	public void removePlayer(int id) {
		players.removeIf(p -> p.getId() == id);
	}

	public void removeMob(int id) {
		mobs.removeIf(p -> p.getId() == id);
	}

	public void removeNpc(int id) {
		npcs.removeIf(p -> p.getId() == id);
	}

	/**
	 * @return the fights
	 */
	public Set<Fight> getFights() {
		return fights;
	}

	/**
	 * @param ressources
	 *            the ressources to set
	 */
	public void setRessources(Set<Ressource> ressources) {
		this.ressources = ressources;
	}

	/**
	 * @param fights
	 *            the fights to set
	 */
	public void setFights(Set<Fight> fights) {
		this.fights = fights;
	}

	/**
	 * @return the dofusMap
	 */
	public DofusMap getDofusMap() {
		return dofusMap;
	}

	public static BotMap fromDofusMap(DofusMap map) {
		return new BotMap(map);
	}

	/**
	 * @return the ressources
	 */
	public Set<Ressource> getRessources() {
		return ressources;
	}

	@Override
	public String toString() {
		return "BotMap [dofusMap=" + dofusMap + ", x=" + x + ", y=" + y + ", ressources=" + ressources + ", fights=" + fights + ", players=" + players + ", mobs=" + mobs + ", npcs=" + npcs + "]";
	}

}
