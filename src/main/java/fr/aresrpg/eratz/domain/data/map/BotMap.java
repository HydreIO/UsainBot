package fr.aresrpg.eratz.domain.data.map;

import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;
import fr.aresrpg.eratz.infra.map.BotMapImpl;
import fr.aresrpg.eratz.infra.map.adapter.BotMapAdapter;
import fr.aresrpg.eratz.infra.map.dao.BotMapDao;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

/**
 * 
 * @since
 */
public interface BotMap {

	int getMapId();

	long getTimeMs();

	Trigger[] getTriggers(TriggerType type);

	void setTriggers(TriggerType type, Trigger[] triggers);

	ManchouMap getMap();

	default BotMapDao toDao() {
		return BotMapAdapter.IDENTITY.adaptTo((BotMapImpl) this);
	}

}
