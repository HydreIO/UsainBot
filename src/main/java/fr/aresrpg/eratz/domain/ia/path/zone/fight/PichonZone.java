package fr.aresrpg.eratz.domain.ia.path.zone.fight;

import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.path.Paths;
import fr.aresrpg.tofumanchou.domain.data.entity.mob.MobGroup;
import fr.aresrpg.tofumanchou.domain.data.enums.DofusMobs;

/**
 * 
 * @since
 */
public class PichonZone extends FightZone {

	/**
	 * @param playerPosition
	 */
	public PichonZone(BotPerso perso) {
		super(() -> perso);
	}

	@Override
	protected boolean isValid(BotMap map) {
		return hasArea(map, 18) && hasSubArea(map, 335);
	}

	@Override
	protected Paths getType() {
		return Paths.FIGHT_PICHON_ASTRUB;
	}

	@Override
	public boolean isValid(MobGroup group) {
		if (group == null) return false;
		return !hasMob(DofusMobs.PICHON_ORANGE, group);
	}

}
