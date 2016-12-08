package fr.aresrpg.eratz.domain.dofus.map;

import fr.aresrpg.eratz.domain.dofus.ressource.Ressources;

public class Ressource {
	private int cellId;
	private Ressources type;
	private boolean spawned;

	public Ressource(int cellid, Ressources type, boolean spawned) {
		this.cellId = cellid;
		this.type = type;
		this.spawned = spawned;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Ressource)) return false;
		Ressource r = (Ressource) obj;
		return cellId == r.getCellId() && type == r.getType();
	}

	public Ressource(int cellid, Ressources type) {
		this(cellid, type, false);
	}

	/** * @return the cellId */
	public int getCellId() {
		return cellId;
	}

	/** * @return the type */
	public Ressources getType() {
		return type;
	}

	/** * @return the spawned */
	public boolean isSpawned() {
		return spawned;
	}

	/** * @param spawned * the spawned to set */
	public void setSpawned(boolean spawned) {
		this.spawned = spawned;
	}

	public void setSpawned() {
		setSpawned(true);
	}
}