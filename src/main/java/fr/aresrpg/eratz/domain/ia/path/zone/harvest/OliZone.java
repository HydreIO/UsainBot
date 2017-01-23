package fr.aresrpg.eratz.domain.ia.path.zone.harvest;

import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.path.Paths;

/**
 * 
 * @since
 */
public class OliZone extends HarvestZone {

	public OliZone(BotPerso perso) {
		super(() -> perso, true, Interractable.OLIVIOLET);
	}

	@Override
	protected boolean isValid(BotMap map) {
		return map.hasOne(getRessources());
	}

	@Override
	protected Paths getType() {
		return Paths.BUCHERON_OLIVIOLET;
	}

}
