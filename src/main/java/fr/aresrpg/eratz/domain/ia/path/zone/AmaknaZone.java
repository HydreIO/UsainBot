package fr.aresrpg.eratz.domain.ia.path.zone;

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
		super(() -> perso, Interractable.NOYER, Interractable.CHENE, Interractable.CHATAIGNIER, Interractable.ERABLE, Interractable.MERISIER, Interractable.EBENE,
				Interractable.CHARME,
				Interractable.IF);
	}

	@Override
	protected boolean isValid(BotMap map) {
		MapDataBean data = MapsData.getData(map.getMapId());
		boolean hasSub = data.getSubareaId() == 12 || data.getSubareaId() == 170 || data.getSubareaId() == 11;
		return data.getAreaId() == 0 && hasSub && map.hasOne(getRessources());
	}

	@Override
	protected Paths getType() {
		return Paths.AMAKNA;
	}

}
