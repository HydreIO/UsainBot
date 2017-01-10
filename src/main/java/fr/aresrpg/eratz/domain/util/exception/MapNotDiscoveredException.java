package fr.aresrpg.eratz.domain.util.exception;

/**
 * 
 * @since
 */
public class MapNotDiscoveredException extends RuntimeException {

	private int mapId;

	public MapNotDiscoveredException(int mapid) {
		super("The map " + mapid + " is not discovered yet !");
	}

	public int getMapId() {
		return mapId;
	}

	@Override
	public String toString() {
		return "MapNotDiscoveredException [mapId=" + mapId + "]";
	}

}
