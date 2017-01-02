package fr.aresrpg.eratz.domain.data.player;

import java.util.*;

/**
 * 
 * @since
 */
public class Group {

	private String label;
	private BotPerso boss;
	private Set<BotPerso> members = new HashSet<>();

	public Group(String label, BotPerso boss) {
		this.boss = boss;
		this.label = label;
	}

	public void formGroup() {
		if (boss == null || !boss.isOnline()) throw new IllegalStateException("Unable to make group | The boss is not online");
		getMembers().stream().filter(Objects::nonNull).filter(BotPerso::isOnline).forEach(p -> boss.getPerso().invitPlayerToGroup(p.getPerso().getPseudo()));
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
	public BotPerso getBoss() {
		return boss;
	}

	/**
	 * @param boss
	 *            the boss to set
	 */
	public void setBoss(BotPerso boss) {
		this.boss = boss;
	}

	/**
	 * @return the members
	 */
	public Set<BotPerso> getMembers() {
		return members;
	}

	@Override
	public String toString() {
		return "Group [label=" + label + ", boss=" + boss + ", members=" + members + "]";
	}

}
