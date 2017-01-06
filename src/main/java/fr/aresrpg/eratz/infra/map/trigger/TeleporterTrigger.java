package fr.aresrpg.eratz.infra.map.trigger;

import fr.aresrpg.eratz.domain.data.map.Destination;
import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @since
 */
public class TeleporterTrigger implements Trigger {

	private static final String TP = "type";
	private static final String DEST_MAP = "destmap";
	private static final String DEST_CELL = "destcell";

	private int cellId;
	private TeleportType type;
	private Destination dest;

	public TeleporterTrigger(int cell) {
		this.cellId = cell;
	}

	@Override
	public int getCellId() {
		return cellId;
	}

	/**
	 * @return the dest
	 */
	public Destination getDest() {
		return dest;
	}

	/**
	 * @param dest
	 *            the dest to set
	 */
	public void setDest(Destination dest) {
		this.dest = dest;
	}

	/**
	 * @param cellId
	 *            the cellId to set
	 */
	public void setCellId(int cellId) {
		this.cellId = cellId;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(TeleportType type) {
		this.type = type;
	}

	@Override
	public TriggerType getType() {
		return TriggerType.TELEPORT;
	}

	@Override
	public Map<String, Object> readDatas() {
		Map<String, Object> map = new HashMap<>();
		map.put(TP, type.ordinal());
		map.put(DEST_MAP, dest.getMapId());
		map.put(DEST_CELL, dest.getCellId());
		return map;
	}

	@Override
	public void writeDatas(Map<String, Object> datas) {
		this.type = TeleportType.values()[(int) datas.get(TP)];
		int dmap = (int) datas.get(DEST_MAP);
		int dcell = (int) datas.get(DEST_CELL);
		this.dest = Destination.create(dmap, dcell);
	}

	public static enum TeleportType {
		TELEPORTER,
		ZAAP,
		ZAAPI,
	}

	@Override
	public String toString() {
		return "TeleporterTrigger [cellId=" + cellId + ", type=" + type + ", dest=" + dest + "]";
	}

}
