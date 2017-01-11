package fr.aresrpg.eratz.infra.map.adapter;

import fr.aresrpg.commons.domain.reflection.ParametrizedClass;
import fr.aresrpg.commons.domain.serialization.adapters.Adapter;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.util.Compressor;
import fr.aresrpg.eratz.domain.data.map.trigger.Trigger;
import fr.aresrpg.eratz.domain.data.map.trigger.TriggerType;
import fr.aresrpg.eratz.infra.map.BotMapImpl;
import fr.aresrpg.eratz.infra.map.dao.BotMapDao;
import fr.aresrpg.eratz.infra.map.dao.TriggerDao;

import java.util.*;
import java.util.stream.Collectors;

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
				in.getTriggers().values().stream().reduce((a, b) -> {
					a.addAll(b);
					return a;
				}).get().stream().map(TriggerAdapter.IDENTITY::adaptTo).toArray(TriggerDao[]::new), Compressor.compressCells(in.getMap().getProtocolCells()));
	}

	public Map<TriggerType, Set<Trigger>> readTriggers(Set<Trigger> triggers) {
		Map<TriggerType, Set<Trigger>> map = new HashMap<>();
		for (Trigger t : triggers) {
			Set<Trigger> set = map.get(t.getType());
			if (set == null) map.put(t.getType(), set = new HashSet<>());
			set.add(t);
		}
		return map;
	}

	@Override
	public BotMapImpl adaptFrom(BotMapDao out) {
		DofusMap dofusMap = new DofusMap(out.getMapid(), out.getDate(), out.getWidth(), out.getHeight(), out.getMusicId(), out.getCapabilities(), out.isOutdoor(), out.getBackground(),
				Compressor.uncompressCells(out.getCells()));
		return new BotMapImpl(out.getMapid(), out.getDate(), readTriggers(Arrays.stream(out.getTriggers()).map(TriggerAdapter.IDENTITY::adaptFrom).collect(Collectors.toSet())), dofusMap);
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
