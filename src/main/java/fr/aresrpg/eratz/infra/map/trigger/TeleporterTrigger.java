package fr.aresrpg.eratz.infra.map.trigger;

import fr.aresrpg.eratz.domain.data.map.Destination;
import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;
import fr.aresrpg.eratz.infra.map.DestinationImpl;

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

	private String trigger = TriggerType.TELEPORT.name();
	private int cellId;
	private TeleportType type;
	private Destination dest;

	public TeleporterTrigger(int cell) {
		this.cellId = cell;
	}

	public TeleporterTrigger(int cellId, TeleportType type, Destination dest) {
		this.cellId = cellId;
		this.type = type;
		this.dest = dest;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		return obj instanceof TeleporterTrigger && ((TeleporterTrigger) obj).cellId == cellId;
	}

	@Override
	public int hashCode() {
		return cellId;
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

	public TeleportType getTeleportType() {
		return this.type;
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
		map.put(TP, type.name());
		map.put(DEST_MAP, dest.getMapId());
		map.put(DEST_CELL, dest.getCellId());
		return map;
	}

	@Override
	public void writeDatas(Map<String, Object> datas) {
		this.type = TeleportType.valueOf((String) datas.get(TP));
		int dmap = (int) datas.get(DEST_MAP);
		int dcell = (int) datas.get(DEST_CELL);
		this.dest = new DestinationImpl(dmap, dcell);
	}

	public static enum TeleportType {
		MAP_TP,
		ZAAP,
		ZAAPI,
	}

	@Override
	public String toString() {
		return "TeleporterTrigger [cellId=" + cellId + ", type=" + type + ", dest=" + dest + "]";
	}

}
