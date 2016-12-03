package fr.aresrpg.eratz.domain.behavior.move;

import fr.aresrpg.eratz.domain.behavior.Behavior;
import fr.aresrpg.eratz.domain.player.Perso;

/**
 * 
 * @since
 */
public abstract class PathBehavior extends Behavior {

	/**
	 * @param perso
	 */
	public PathBehavior(Perso perso) {
		super(perso);
	}

	public abstract PathBehavior getPathToReachCurrentPath(); // dans le cas ou il faut un trajet pour atteindre la boucle de cet implementation de trajet

	public abstract void startPath();

	@Override
	public void start() {
		if (getPathToReachCurrentPath() != null) getPathToReachCurrentPath().start();
		startPath();
	}

}
