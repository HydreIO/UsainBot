package fr.aresrpg.eratz.domain.data.map.trigger;

import java.util.Map;

/**
 * A trigger is an element on the map wich triggers actions (teleporters, ressources, pnj talking, zaap, etc..)
 * 
 * @since
 */
public interface Trigger {

	/**
	 * @return the interractable cell
	 */
	int getCellId();

	/**
	 * @return the type of the trigger
	 */
	TriggerType getType();

	Map<String, Object> readDatas();

	void writeDatas(Map<String, Object> datas);

}
