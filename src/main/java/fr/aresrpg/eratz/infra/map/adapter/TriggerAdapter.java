package fr.aresrpg.eratz.infra.map.adapter;

import fr.aresrpg.commons.domain.reflection.ParametrizedClass;
import fr.aresrpg.commons.domain.serialization.adapters.Adapter;
import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;
import fr.aresrpg.eratz.infra.map.TriggerFactoryImpl;

import java.util.*;

/**
 * 
 * @since
 */
public class TriggerAdapter implements Adapter<Trigger, Map<String, Object>> {

	public static final TriggerAdapter IDENTITY = new TriggerAdapter();
	private static final ParametrizedClass<Trigger> IN = new ParametrizedClass<>(Trigger.class);
	private static final ParametrizedClass<Map<String, Object>> OUT = new ParametrizedClass<>(Map.class);
	private static final String TYPE = "type";
	private static final String CELL = "cell";
	private static final String DATAS = "datas";

	@Override
	public Map<String, Object> adaptTo(final Trigger in) {
		final Map<String, Object> map = new HashMap<>();
		map.put(TYPE, in.getType().ordinal());
		map.put(CELL, in.getCellId());
		map.put(DATAS, in.readDatas());
		return map;
	}

	@Override
	public Trigger adaptFrom(final Map<String, Object> out) {
		final TriggerType t = TriggerType.values()[(int) out.get(TYPE)];
		final int cell = (int) out.get(CELL);
		final Map<String, Object> datas = (Map<String, Object>) out.get(DATAS);
		Objects.requireNonNull(datas, "Invalid datas");
		return new TriggerFactoryImpl().setType(t).setCellId(cell).setDatas(datas).create();
	}

	@Override
	public ParametrizedClass<Trigger> getInType() {
		return IN;
	}

	@Override
	public ParametrizedClass<Map<String, Object>> getOutType() {
		return OUT;
	}

}
