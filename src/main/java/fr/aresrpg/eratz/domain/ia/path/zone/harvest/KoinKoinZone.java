package fr.aresrpg.eratz.domain.ia.path.zone.harvest;

import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.path.Paths;

/**
 * 
 * @since
 */
public class KoinKoinZone extends HarvestZone {

	public KoinKoinZone(BotPerso perso) {
		super(() -> perso, false, Interractable.PECHE_CANARD);
	}

	@Override
	protected boolean isValid(BotMap map) {
		return map.getMapId() == 3335;
	}

	@Override
	protected Paths getType() {
		return Paths.PECHE_KOIN_KOIN;
	}

}
