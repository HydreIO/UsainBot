package fr.aresrpg.eratz.domain.data.map;

import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;
import fr.aresrpg.eratz.domain.util.BotNode;
import fr.aresrpg.eratz.infra.map.BotMapImpl;
import fr.aresrpg.eratz.infra.map.adapter.BotMapAdapter;
import fr.aresrpg.eratz.infra.map.dao.BotMapDao;
import fr.aresrpg.eratz.infra.map.trigger.TeleporterTrigger;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

import java.util.Set;

/**
 * 
 * @since
 */
public interface BotMap {

	int getMapId();

	long getTimeMs();

	Set<Trigger> getTriggers(TriggerType type);

	void setTriggers(TriggerType type, Set<Trigger> triggers);

	ManchouMap getMap();

	default BotNode toNode(TeleporterTrigger triggerUsed) {
		return new BotNode(getMap().getX(), getMap().getY(), 0, triggerUsed);
	}

	default BotMapDao toDao() {
		return BotMapAdapter.IDENTITY.adaptTo((BotMapImpl) this);
	}

}
