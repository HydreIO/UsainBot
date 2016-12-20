package fr.aresrpg.eratz.domain.data.dofus.map;

import fr.aresrpg.dofus.protocol.game.movement.*;
import fr.aresrpg.dofus.structures.map.DofusMap;
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
				i.setCellid(cellid);
				return;
			}
	}

	public void monsterMove(int id, int cellid) {
		for (MovementMonsterGroup i : mobs)
			if (i.getId() == id) i.setCellid(cellid);
	}

	public void npcMove(int id, int cellid) {
		for (MovementNpc i : npcs)
			if (i.getId() == id) i.setCellid(cellid);
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
