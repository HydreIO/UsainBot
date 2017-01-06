package fr.aresrpg.eratz.infra.map;

import fr.aresrpg.eratz.domain.data.map.trigger.*;
import fr.aresrpg.eratz.infra.map.trigger.InterractableTrigger;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger;

import java.util.Map;

/**
 * 
 * @since
 */
public class TriggerFactoryImpl implements TriggerFactory {

	private TriggerType type;
	private int cell;
	private Map<String, Object> datas;

	@Override
	public TriggerFactory setCellId(int cell) {
		this.cell = cell;
		return this;
	}

	@Override
	public TriggerFactory setType(TriggerType type) {
		this.type = type;
		return this;
	}

	@Override
	public TriggerFactory setDatas(Map<String, Object> datas) {
		this.datas = datas;
		return this;
	}

	@Override
	public Trigger create() {
		if (type == null) throw new NullPointerException("The type is null !");
		Trigger tr = null;
		switch (type) {
			case INTERRACTABLE:
				tr = new InterractableTrigger(cell);
				break;
			case TELEPORT:
				tr = new TeleporterTrigger(cell);
				break;
			default:
				throw new IllegalArgumentException("The type is not handled ! " + type);
		}
		tr.writeDatas(datas);
		return tr;
	}

}
