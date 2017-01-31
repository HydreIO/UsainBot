package fr.aresrpg.eratz.domain.ia.path.zone.bucheron;

import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.path.Paths;
import fr.aresrpg.eratz.domain.ia.path.zone.HarvestZone;

/**
 * 
 * @since
 */
public class KaliptusZone extends HarvestZone {

	public KaliptusZone(BotPerso perso) {
		super(() -> perso, true, Interractable.KALIPTUS);
	}

	@Override
	protected boolean isValid(BotMap map) {
		return map.hasOne(getRessources());
	}

	@Override
	protected Paths getType() {
		return Paths.BUCHERON_KALIPTUS;
	}

}
