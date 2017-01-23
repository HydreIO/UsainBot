package fr.aresrpg.eratz.domain.ia.path.zone.fight;

import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.path.Paths;
import fr.aresrpg.tofumanchou.domain.data.enums.DofusMobs;

/**
 * 
 * @since
 */
public class ChampAstrubZone extends FightZone {

	/**
	 * @param playerPosition
	 */
	public ChampAstrubZone(BotPerso perso) {
		super(() -> perso);
	}

	@Override
	protected boolean isValid(BotMap map) {
		return hasArea(map, 18) && hasSubArea(map, 98);
	}

	@Override
	protected Paths getType() {
		return Paths.FIGHT_CHAMPS_ASTRUB;
	}

	@Override
	public boolean avoid(DofusMobs mob) {
		return false;
	}

}
