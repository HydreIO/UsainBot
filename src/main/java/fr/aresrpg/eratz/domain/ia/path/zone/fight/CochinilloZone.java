package fr.aresrpg.eratz.domain.ia.path.zone.fight;

import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.path.Paths;
import fr.aresrpg.tofumanchou.domain.data.entity.mob.MobGroup;

/**
 * 
 * @since
 */
public class CochinilloZone extends FightZone {

	/**
	 * @param playerPosition
	 */
	public CochinilloZone(BotPerso perso) {
		super(() -> perso);
	}

	@Override
	protected boolean isValid(BotMap map) {
		return map.getMap().isOutdoor() && hasArea(map, 0) && hasSubArea(map, 11);
	}

	@Override
	protected Paths getType() {
		return Paths.FIGHT_COCHON_DE_LAIT;
	}

	@Override
	public boolean isValid(MobGroup group) {
		if (group == null) return false;
		return true;
	}

}
