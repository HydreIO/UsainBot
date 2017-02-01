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
public class FullZone extends HarvestZone {

	public FullZone(BotPerso perso) {
		super(() -> perso, true, Interractable.allWoodsBut(Interractable.FRENE));
	}

	@Override
	protected boolean isValid(BotMap map) {
		if (map.hasOne(Interractable.BOMBU, Interractable.OLIVIOLET)) return true;
		if (map.getMapId() == 1332) return false; // bug chelou du pathfinding
		return hasArea(map, 7, 8, 0) && hasSubArea(map, 43, 68, 69, 38, 12, 170, 11) && map.hasOne(getRessources());
	}

	@Override
	protected Paths getType() {
		return Paths.BUCHERON_FULL;
	}

}
