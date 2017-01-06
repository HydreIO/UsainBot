package fr.aresrpg.eratz.infra.map.adapter;

import fr.aresrpg.commons.domain.reflection.ParametrizedClass;
import fr.aresrpg.commons.domain.serialization.adapters.Adapter;
import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;
import fr.aresrpg.eratz.domain.util.UtilFunc;
import fr.aresrpg.eratz.infra.map.BotMapImpl;

import java.util.*;

/**
 * 
 * @since
 */
public class BotMapAdapter implements Adapter<BotMapImpl, Map<String, Object>> {

	public static final BotMapAdapter IDENTITY = new BotMapAdapter();
	private static final ParametrizedClass<BotMapImpl> IN = new ParametrizedClass<>(BotMap.class);
	private static final ParametrizedClass<Map<String, Object>> OUT = new ParametrizedClass<>(Map.class);
	private static final String MAPID = "mapid";
	private static final String DATE = "subid";
	private static final String TRIGGERS = "trig";

	@Override
	public Map<String, Object> adaptTo(BotMapImpl in) {
		Map<String, Object> map = new HashMap<>();
		map.put(MAPID, in.getMapId());
		map.put(DATE, in.getDate());
		map.put(TRIGGERS, Arrays.stream(TriggerType.values()).map(in::getTriggers).filter(Objects::nonNull).map(this::parseTriggers).reduce(ArrayUtils::concat).orElseGet(UtilFunc::emptyMapArray));
		return map;
	}

	private Map<String, Object>[] parseTriggers(Trigger[] triggers) {
		return Arrays.stream(triggers).map(TriggerAdapter.IDENTITY::adaptTo).toArray(HashMap[]::new);
	}

	private Map<TriggerType, Trigger[]> readTriggers(Trigger[] triggers) {
		Map<TriggerType, Trigger[]> map = new HashMap<>();
		Arrays.stream(TriggerType.values()).forEach(t -> map.put(t, new Trigger[0]));
		Arrays.stream(triggers).forEach(t -> {
			Trigger[] last = map.get(t.getType());
			Trigger[] copy = Arrays.copyOf(last, last.length + 1);
			copy[last.length] = t;
			map.put(t.getType(), copy);
		});
		return map;
	}

	@Override
	public BotMapImpl adaptFrom(Map<String, Object> out) {
		int mapid = (int) out.get(MAPID);
		String date = (String) out.get(DATE);
		Trigger[] array = Arrays.stream((Map<String, Object>[]) out.get(TRIGGERS)).map(TriggerAdapter.IDENTITY::adaptFrom).toArray(Trigger[]::new);
		return new BotMapImpl(mapid, date, readTriggers(array));
	}

	@Override
	public ParametrizedClass<BotMapImpl> getInType() {
		return IN;
	}

	@Override
	public ParametrizedClass<Map<String, Object>> getOutType() {
		return OUT;
	}

}
