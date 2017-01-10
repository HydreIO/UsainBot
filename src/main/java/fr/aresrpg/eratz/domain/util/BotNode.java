package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.dofus.util.Pathfinding.Node;

/**
 * 
 * @since
 */
public class BotNode extends Node {

	public int id;
	private int cellId; // la cell d'arrivée sur la map, ou la cell du joueur. sans ça le pathfinding est impossible car je veut pouvoir calculer pour chaque node (map) si il peut aller vers une autre (donc si le chemin est pas bloqué)

	public BotNode(int x, int y, int cost, int id, int cellid) {
		super(x, y);
		super.cost = cost;
		this.cellId = cellid;
		this.id = id;
	}

	public BotNode(int x, int y, int id, int cellId) {
		super(x, y);
		super.cost = 0;
		this.id = id;
	}

	/**
	 * @return the cellId
	 */
	public int getCellId() {
		return cellId;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		return obj instanceof BotNode && ((BotNode) obj).id == id;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return "BotNode [id=" + id + "]" + super.toString();
	}

}
