package fr.aresrpg.eratz.domain.data.map;

import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;

/**
 * 
 * @since
 */
public interface BotMap {

	int getMapId();

	String getDate();

	Trigger[] getTriggers(TriggerType type);

}
