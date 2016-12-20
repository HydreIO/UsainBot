package fr.aresrpg.eratz.domain.data.player.object;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.dofus.structures.map.Cell;
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

	/** * @return the spawned */
	public boolean isSpawned() {
		switch (getType()) {
			case BLE:
				LOGGER.info("Le layer object 2 du blé = " + getCell().getLayerObject2Num());
				return getCell().getLayerObject2Num() == 2;

			default:
				return false;
		}
	}

}