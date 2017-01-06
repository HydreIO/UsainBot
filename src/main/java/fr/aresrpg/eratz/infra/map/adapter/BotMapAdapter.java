package fr.aresrpg.eratz.infra.map.adapter;

import fr.aresrpg.commons.domain.reflection.ParametrizedClass;
import fr.aresrpg.commons.domain.serialization.adapters.Adapter;
import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;
import fr.aresrpg.eratz.infra.map.BotMapImpl;
import fr.aresrpg.eratz.infra.map.dao.BotMapDao;
import fr.aresrpg.eratz.infra.map.dao.TriggerDao;

import java.util.*;

/**
 * 
 * @since
 */
public class BotMapAdapter implements Adapter<BotMapImpl, BotMapDao> {

	public static final BotMapAdapter IDENTITY = new BotMapAdapter();
	private static final ParametrizedClass<BotMapImpl> IN = new ParametrizedClass<>(BotMap.class);
	private static final ParametrizedClass<BotMapDao> OUT = new ParametrizedClass<>(Map.class);
	private static final String MAPID = "mapid";
	private static final String DATE = "subid";
	private static final String TRIGGERS = "trig";

	@Override
	public BotMapDao adaptTo(BotMapImpl in) {
		return new BotMapDao(in.getMapId(), in.getDate(),
				Arrays.stream(TriggerType.values()).map(in::getTriggers).filter(Objects::nonNull).map(this::parseTriggers).reduce(ArrayUtils::concat).orElseGet(() -> new TriggerDao[0]));
	}

	private TriggerDao[] parseTriggers(Trigger[] triggers) {
		return Arrays.stream(triggers).map(TriggerAdapter.IDENTITY::adaptTo).toArray(TriggerDao[]::new);
	}

	private Trigger[] parseDaos(TriggerDao[] daos) {
		return Arrays.stream(daos).map(TriggerAdapter.IDENTITY::adaptFrom).toArray(Trigger[]::new);
	}

	private Map<TriggerType, Trigger[]> readTriggers(Trigger[] triggers) {
		Map<TriggerType, Trigger[]> map = new HashMap<>();
		Arrays.stream(triggers).forEach(t -> {
			Trigger[] last = map.get(t.getType());
			if (last == null) map.put(t.getType(), last = new Trigger[0]);
			Trigger[] copy = Arrays.copyOf(last, last.length + 1);
			copy[last.length] = t;
			map.put(t.getType(), copy);
		});

		return map;
	}

	@Override
	public BotMapImpl adaptFrom(BotMapDao out) {
		Trigger[] array = Arrays.stream(out.getTriggers()).map(TriggerAdapter.IDENTITY::adaptFrom).toArray(Trigger[]::new);
		return new BotMapImpl(out.getMapid(), out.getDate(), readTriggers(array));
	}

	@Override
	public ParametrizedClass<BotMapImpl> getInType() {
		return IN;
	}

	@Override
	public ParametrizedClass<BotMapDao> getOutType() {
		return OUT;
	}

}
