package fr.aresrpg.eratz.domain.behavior.move;

import fr.aresrpg.eratz.domain.behavior.Behavior;

/**
 * 
 * @since
 */
public interface PathBehavior extends Behavior {

	PathBehavior getPathToReachCurrentPath(); // dans le cas ou il faut un trajet pour atteindre la boucle de cet implementation de trajet

}
