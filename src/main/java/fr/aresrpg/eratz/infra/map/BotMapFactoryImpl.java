package fr.aresrpg.eratz.infra.map;

import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.map.BotMapFactory;
import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;

import java.util.*;

/**
 * 
 * @since
 */
public class BotMapFactoryImpl implements BotMapFactory {

	private int mapId;
	private String subId;
	private Map<TriggerType, Trigger[]> triggers = new HashMap<>();

	@Override
	public BotMapFactory setId(int id) {
		this.mapId = id;
		return this;
	}

	@Override
	public BotMapFactory setDate(String date) {
		this.subId = date;
		return this;
	}

	@Override
	public BotMapFactory withTriggers(TriggerType type, Trigger... triggers) {
		this.triggers.put(type, triggers);
		return this;
	}

	@Override
	public BotMap create() {
		Objects.requireNonNull(triggers, "Triggers must not be null !");
		return new BotMapImpl(mapId, subId, triggers);
	}

}
