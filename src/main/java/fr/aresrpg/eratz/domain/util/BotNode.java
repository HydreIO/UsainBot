package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger;

/**
 * 
 * @since
 */
public class BotNode extends Node {

	public int id;
	private TeleporterTrigger trigger; // ce trigger représente le trigger qui à été utilisé pour aller sur cette node, et donc la cell id de la node = trigger.getDest().getCellId();

	public BotNode(int x, int y, int cost, TeleporterTrigger trigger) {
		super(x, y);
		super.cost = cost;
		this.trigger = trigger;
		this.id = trigger.getDest().getCellId();
	}

	public BotNode(int x, int y, TeleporterTrigger trigger) {
		this(x, y, 0, trigger);
	}

	/**
	 * @return the cellId
	 */
	public TeleporterTrigger getTrigger() {
		return trigger;
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
