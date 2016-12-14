package fr.aresrpg.eratz.domain.data.player.object;

import fr.aresrpg.eratz.domain.data.player.Perso;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @since
 */
public class Group {

	private Perso boss;
	private Set<Perso> members = new HashSet<>();

	public Group(Perso boss) {
		this.boss = boss;
	}

	public void formGroup() {
		if (boss == null || !boss.getAccount().isActive()) throw new IllegalStateException("Unable to make group | The boss is not online");
		getMembers().forEach(p -> boss.getAbilities().getBaseAbility().invitPlayerToGroup(p.getPseudo()));
	}

	/**
	 * @return the boss
	 */
	public Perso getBoss() {
		return boss;
	}

	/**
	 * @param boss
	 *            the boss to set
	 */
	public void setBoss(Perso boss) {
		this.boss = boss;
	}

	/**
	 * @return the members
	 */
	public Set<Perso> getMembers() {
		return members;
	}

	@Override
	public String toString() {
		return "Group [boss=" + boss + ", members=" + members + "]";
	}

}
