package fr.aresrpg.eratz.domain.data.player.object;

import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.util.Maps;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.data.dofus.map.BotMap;
import fr.aresrpg.eratz.domain.data.dofus.ressource.Interractable;

public class Ressource {
	private Cell cell;
	private Interractable type;

	public Ressource(Cell cell, Interractable type) {
		this.cell = cell;
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		return obj == null ? false : obj == this || (obj instanceof Cell && getCell().equals(obj));
	}

	/**
	 * @return the cell
	 */
	public Cell getCell() {
		return cell;
	}

	/** * @return the type */
	public Interractable getType() {
		return type;
	}

	public int getNeighborCell(BotMap map) {
		Node[] neighbors = Pathfinding.getNeighbors(new Node(getCell().getX(), getCell().getY()));
		for (Node n : neighbors) {
			int id = Maps.getId(n.getX(), n.getY(), map.getDofusMap().getWidth());
			Cell cell = map.getDofusMap().getCell(id);
			if (cell.isWalkeable(i -> !map.hasMobOn(i))) return id;
		}
		return -1;
	}

	/** * @return the spawned */
	public boolean isSpawned() {
		switch (getType()) {
			case BLE:
				return getCell().getLayerObject2Num() == 7511 && (getCell().getFrame() == 0 || getCell().getFrame() == 5);

			default:
				return false;
		}
	}

	@Override
	public String toString() {
		return "Ressource [cell=" + cell + ", type=" + type + "]";
	}

}