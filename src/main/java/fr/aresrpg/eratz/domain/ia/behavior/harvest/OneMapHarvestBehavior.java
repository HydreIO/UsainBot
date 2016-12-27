package fr.aresrpg.eratz.domain.ia.behavior.harvest;

import fr.aresrpg.eratz.domain.data.dofus.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;

import java.awt.Point;

/**
 * 
 * @since
 */
public abstract class OneMapHarvestBehavior extends Behavior {

	/**
	 * @param perso
	 */
	public OneMapHarvestBehavior(Perso perso) {
		super(perso);
	}

	@Override
	public BehaviorStopReason start() {
		return null;
	}

	protected abstract Point getStartCoords();

	protected abstract void initMoves();

	protected abstract boolean isOnMap(BotMap map); // le seul moyen de reperer si on est sur la bonne map car dans le cas des mines les coords ne fonctionnent pas

}
