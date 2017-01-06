package fr.aresrpg.eratz.domain.data.map;

import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;

/**
 * 
 * @since
 */
public interface BotMapFactory {

	BotMapFactory setId(int id);

	BotMapFactory setDate(String date);

	BotMapFactory withTriggers(TriggerType type, Trigger... triggers);

	BotMap create();

}
