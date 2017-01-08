package fr.aresrpg.eratz.infra.map.adapter;

import fr.aresrpg.commons.domain.reflection.ParametrizedClass;
import fr.aresrpg.commons.domain.serialization.adapters.Adapter;
import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.util.Compressor;
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
	private static final ParametrizedClass<BotMapImpl> IN = new ParametrizedClass<>(BotMapImpl.class);
	private static final ParametrizedClass<BotMapDao> OUT = new ParametrizedClass<>(BotMapDao.class);

	@Override
	public BotMapDao adaptTo(BotMapImpl in) {
		return new BotMapDao(in.getMapId(), in.getTimeMs(), in.getMap().getX(), in.getMap().getY(), in.getMap().getWidth(), in.getMap().getHeight(), in.getMap().getMusicId(),
				in.getMap().getCapabilities(), in.getMap().isOutdoor(), in.getMap().getBackgroundId(),
				parseArray(in.getTriggers().values().stream().reduce(ArrayUtils::concat).orElse(null)), Compressor.compressCells(in.getMap().getProtocolCells()));
	}

	public TriggerDao[] parseArray(Trigger[] triggers) {
		if (triggers == null) return new TriggerDao[0];
		return Arrays.stream(triggers).map(TriggerAdapter.IDENTITY::adaptTo).toArray(TriggerDao[]::new);
	}

	public Map<TriggerType, Trigger[]> readTriggers(Trigger[] triggers) {
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
		DofusMap dofusMap = new DofusMap(out.getMapid(), out.getDate(), out.getWidth(), out.getHeight(), out.getMusicId(), out.getCapabilities(), out.isOutdoor(), out.getBackground(),
				Compressor.uncompressCells(out.getCells()));
		return new BotMapImpl(out.getMapid(), out.getDate(), readTriggers(Arrays.stream(out.getTriggers()).map(TriggerAdapter.IDENTITY::adaptFrom).toArray(Trigger[]::new)), dofusMap);
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
