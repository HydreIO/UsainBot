package fr.aresrpg.eratz.infra.map.trigger;

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

	private int cellId;
	private int interractId;

	public InterractableTrigger(int cell) {
		this.cellId = cell;
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
		map.put(INTT, interractId);
		return map;
	}

	@Override
	public void writeDatas(Map<String, Object> datas) {
		this.interractId = (int) datas.get(INTT);
	}

}
