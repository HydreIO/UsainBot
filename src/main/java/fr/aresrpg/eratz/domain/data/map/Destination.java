package fr.aresrpg.eratz.domain.data.map;

/**
 * 
 * @since
 */
public interface Destination {

	int getMapId();

	int getCellId();

	static Destination create(int mapid, int cellid) {
		return new Destination() {

			@Override
			public int getMapId() {
				return mapid;
			}

			@Override
			public int getCellId() {
				return cellid;
			}

			@Override
			public String toString() {
				return "[mapid=" + mapid + ", cell=" + cellid + "]";
			}
		};

	}

}
