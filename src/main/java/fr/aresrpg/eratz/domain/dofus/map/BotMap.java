package fr.aresrpg.eratz.domain.dofus.map;

import fr.aresrpg.dofus.protocol.game.movement.*;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.eratz.domain.dofus.fight.Fight;

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
	 * @return the players
	 */
	public Set<MovementPlayer> getPlayers() {
		return players;
	}

	public void removePlayer(int id) {
		players.removeIf(p -> p.getId() == id);
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
