package fr.aresrpg.eratz.domain.dofus.fight;

import fr.aresrpg.eratz.domain.dofus.mob.Invocation;
import fr.aresrpg.eratz.domain.dofus.mob.Mob;
import fr.aresrpg.eratz.domain.player.Player;

/**
 * 
 * @since
 */
public class Target {

	private Player player;
	private Mob mob;
	private Invocation invoc;
	private boolean isEnnemy;

	private Target(Player p, Mob m, Invocation i) {
		this.player = p;
		this.mob = m;
		this.invoc = i;
	}

	/**
	 * @return the isEnnemy
	 */
	public boolean isEnnemy() {
		return isEnnemy;
	}

	/**
	 * @param isEnnemy
	 *            the isEnnemy to set
	 */
	private Target setEnnemy(boolean isEnnemy) {
		this.isEnnemy = isEnnemy;
		return this;
	}

	public static Target fromPlayer(Player p, boolean isennemy) {
		return new Target(p, null, null).setEnnemy(isennemy);
	}

	public static Target fromMob(Mob m) {
		return new Target(null, m, null);
	}

	public static Target fromInvocation(Invocation i, boolean isennemy) {
		return new Target(null, null, i).setEnnemy(isennemy);
	}

	public boolean isPlayer() {
		return player != null;
	}

	public boolean isMob() {
		return mob != null;
	}

	public boolean isInvoc() {
		return invoc != null;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the invoc
	 */
	public Invocation getInvoc() {
		return invoc;
	}

	/**
	 * @return the mob
	 */
	public Mob getMob() {
		return mob;
	}
}
