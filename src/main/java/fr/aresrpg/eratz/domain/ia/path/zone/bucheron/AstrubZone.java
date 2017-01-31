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
public class AstrubZone extends HarvestZone {

	public AstrubZone(BotPerso perso) {
		super(() -> perso, true, Interractable.allWoods());
	}

	@Override
	protected boolean isValid(BotMap map) {
		return hasArea(map, 18) && hasSubArea(map, 97) && map.hasOne(getRessources());

	}

	@Override
	protected Paths getType() {
		return Paths.BUCHERON_AMAKNA;
	}

}
