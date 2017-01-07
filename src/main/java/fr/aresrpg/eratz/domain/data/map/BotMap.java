package fr.aresrpg.eratz.domain.data.map;

import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;

/**
 * 
 * @since
 */
public interface BotMap {

	int getMapId();

	int getX();

	int getY();

	int getWidth();

	int getHeight();

	long getTimeMs();

	Trigger[] getTriggers(TriggerType type);

}
