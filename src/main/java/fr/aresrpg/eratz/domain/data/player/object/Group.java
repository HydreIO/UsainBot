package fr.aresrpg.eratz.domain.data.player.object;

import fr.aresrpg.eratz.domain.data.player.Perso;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public class Group {

	private String label;
	private Perso boss;
	private Set<Perso> members = new HashSet<>();

	public Group(String label, Perso boss) {
		this.boss = boss;
		this.label = label;
	}

	public void formGroup() {
		if (boss == null || !boss.getAccount().isActive()) throw new IllegalStateException("Unable to make group | The boss is not online");
		getMembers().forEach(p -> boss.getAbilities().getBaseAbility().invitPlayerToGroup(p.getPseudo()));
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
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
		return "Group [label=" + label + ", boss=" + boss + ", members=" + members.stream().map(Perso::getPseudo).collect(Collectors.toList()) + "]";
	}

}
