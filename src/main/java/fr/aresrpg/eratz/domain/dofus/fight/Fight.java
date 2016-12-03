package fr.aresrpg.eratz.domain.dofus.fight;

import fr.aresrpg.eratz.domain.dofus.mob.Invocation;
import fr.aresrpg.eratz.domain.dofus.mob.Mob;
import fr.aresrpg.eratz.domain.player.Player;

import java.util.Set;

/**
 * 
 * @since
 */
public class Fight {

	private Set<Player> team0;
	private Set<Player> team1;
	private Set<Mob> mobs;
	private Set<Invocation> invocs;
	private boolean isBlocked;
	private boolean isSpecBlocked;

	/**
	 * @return the team0
	 */
	public Set<Player> getTeam0() {
		return team0;
	}

	/**
	 * @return the team1
	 */
	public Set<Player> getTeam1() {
		return team1;
	}

	/**
	 * @return the invocs
	 */
	public Set<Invocation> getInvocs() {
		return invocs;
	}

	/**
	 * @return the mobs
	 */
	public Set<Mob> getMobs() {
		return mobs;
	}

}
