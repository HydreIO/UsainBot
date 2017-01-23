package fr.aresrpg.eratz.domain.ia.path.zone.harvest;

import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.path.Paths;
import fr.aresrpg.tofumanchou.domain.data.MapsData;
import fr.aresrpg.tofumanchou.domain.data.MapsData.MapDataBean;

/**
 * 
 * @since
 */
public class AmaknaZone extends HarvestZone {

	public AmaknaZone(BotPerso perso) {
		super(() -> perso, true, Interractable.NOYER, Interractable.CHENE, Interractable.CHATAIGNIER, Interractable.ERABLE, Interractable.MERISIER, Interractable.EBENE,
				Interractable.CHARME,
				Interractable.IF);
	}

	@Override
	protected boolean isValid(BotMap map) {
		MapDataBean data = MapsData.getData(map.getMapId());
		if (map.getMapId() == 1332) return false; // bug chelou du pathfinding
		return hasArea(map, 0) && hasSubArea(map, 12, 170, 11) && map.hasOne(getRessources());
	}

	@Override
	protected Paths getType() {
		return Paths.BUCHERON_AMAKNA;
	}

}
