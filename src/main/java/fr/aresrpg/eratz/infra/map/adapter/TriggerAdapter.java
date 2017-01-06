package fr.aresrpg.eratz.infra.map.adapter;

import fr.aresrpg.commons.domain.reflection.ParametrizedClass;
import fr.aresrpg.commons.domain.serialization.adapters.Adapter;
import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.infra.map.TriggerFactoryImpl;
import fr.aresrpg.eratz.infra.map.dao.TriggerDao;

import java.util.Map;

/**
 * 
 * @since
 */
public class TriggerAdapter implements Adapter<Trigger, TriggerDao> {

	public static final TriggerAdapter IDENTITY = new TriggerAdapter();
	private static final ParametrizedClass<Trigger> IN = new ParametrizedClass<>(Trigger.class);
	private static final ParametrizedClass<TriggerDao> OUT = new ParametrizedClass<>(Map.class);
	private static final String TYPE = "type";
	private static final String CELL = "cell";
	private static final String DATAS = "datas";

	@Override
	public TriggerDao adaptTo(final Trigger in) {
		return new TriggerDao(in.getType(), in.getCellId(), in.readDatas());
	}

	@Override
	public Trigger adaptFrom(final TriggerDao out) {
		return new TriggerFactoryImpl().setType(out.getType()).setCellId(out.getCell()).setDatas(out.getDatas()).create();
	}

	@Override
	public ParametrizedClass<Trigger> getInType() {
		return IN;
	}

	@Override
	public ParametrizedClass<TriggerDao> getOutType() {
		return OUT;
	}

}
