package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.dofus.util.Pathfinding.Node;

/**
 * 
 * @since
 */
public class BotNode extends Node {

	public int id;

	public BotNode(int x, int y, int cost, int id) {
		super(x, y);
		super.cost = cost;
		this.id = id;
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
