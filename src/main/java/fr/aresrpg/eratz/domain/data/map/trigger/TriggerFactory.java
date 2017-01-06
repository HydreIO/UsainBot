package fr.aresrpg.eratz.domain.data.map.trigger;

import java.util.Map;

/**
 * 
 * @since
 */
public interface TriggerFactory {

	TriggerFactory setCellId(int cell);

	TriggerFactory setType(TriggerType type);

	TriggerFactory setDatas(Map<String, Object> datas);

	Trigger create();

}
