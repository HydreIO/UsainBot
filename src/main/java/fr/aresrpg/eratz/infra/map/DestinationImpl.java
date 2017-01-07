package fr.aresrpg.eratz.infra.map;

import fr.aresrpg.eratz.domain.data.map.Destination;

/**
 * 
 * @since
 */
public class DestinationImpl implements Destination {

	private int mapId;
	private int cellId;

	/**
	 * @param mapId
	 * @param cellId
	 */
	public DestinationImpl(int mapId, int cellId) {
		this.mapId = mapId;
		this.cellId = cellId;
	}

	/**
	 * @param mapId
	 *            the mapId to set
	 */
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	/**
	 * @param cellId
	 *            the cellId to set
	 */
	public void setCellId(int cellId) {
		this.cellId = cellId;
	}

	@Override
	public int getMapId() {
		return mapId;
	}

	@Override
	public int getCellId() {
		return cellId;
	}

	@Override
	public String toString() {
		return "DestinationImpl [mapId=" + mapId + ", cellId=" + cellId + "]";
	}

}
