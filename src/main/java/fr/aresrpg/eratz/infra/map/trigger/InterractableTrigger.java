package fr.aresrpg.eratz.infra.map.trigger;

import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @since
 */
public class InterractableTrigger implements Trigger {

	private static final String INTT = "type";

	private String trigger = TriggerType.INTERRACTABLE.name();
	private int cellId;
	private String interract;

	public InterractableTrigger(int cell) {
		this.cellId = cell;
	}

	public InterractableTrigger(int cellId, int interractId) {
		this.cellId = cellId;
		Interractable interractable = Interractable.fromId(interractId);
		if (interractable == null) this.interract = String.valueOf(interractId);
		else this.interract = interractable.name();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		return obj instanceof Trigger && ((Trigger) obj).getCellId() == cellId;
	}

	@Override
	public int hashCode() {
		return cellId;
	}

	public Interractable getInterractable() {
		return Interractable.valueOf(interract);
	}

	public int getInterractableId() {
		int id = -1;
		try {
			id = Integer.parseInt(interract);
		} catch (Exception e) {}
		return id;
	}

	@Override
	public int getCellId() {
		return cellId;
	}

	@Override
	public TriggerType getType() {
		return TriggerType.INTERRACTABLE;
	}

	@Override
	public Map<String, Object> readDatas() {
		Map<String, Object> map = new HashMap<>();
		map.put(INTT, interract);
		return map;
	}

	@Override
	public void writeDatas(Map<String, Object> datas) {
		this.interract = (String) datas.get(INTT);
	}

	@Override
	public String toString() {
		return "InterractableTrigger [trigger=" + trigger + ", cellId=" + cellId + ", interract=" + interract + "]";
	}

}
