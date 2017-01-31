package fr.aresrpg.eratz.domain.data.player;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.dofus.structures.game.FightSpawn;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public class Group {

	private String label;
	private BotPerso boss;
	private Set<BotPerso> members = new HashSet<>();

	private Set<CompletableFuture> membersWaiting = new HashSet<>();

	private boolean behaviorRunning;
	private CompletableFuture<?> behavior;

	public Group(String label, BotPerso boss) {
		this.boss = boss;
		this.label = label;
	}

	public void formGroup() {
		if (boss == null || !boss.isOnline()) throw new IllegalStateException("Unable to make group | The boss is not online");
		getMembers().stream().filter(Objects::nonNull).filter(BotPerso::isOnline).forEach(p -> boss.getPerso().invitPlayerToGroup(p.getPerso().getPseudo()));
	}

	public boolean isMemberFight(FightSpawn fight) {
		if (fight == null) return false;
		if (fight.getId() == boss.getPerso().getUUID()) return true;
		for (BotPerso p : members)
			if (p.getPerso().getUUID() == fight.getId()) return true;
		return false;
	}

	private void startWhenAllJoined() {
		while (!fightCanStart(boss.getPerso().getMap()))
			Threads.uSleep(1, TimeUnit.SECONDS);
		boss.getPerso().beReady(true);
	}

	private boolean fightCanStart(ManchouMap map) {
		if (map.isTeamFull(boss.getPerso().getTeam())) return true;
		for (BotPerso p : members)
			if (!p.isInFight()) return false;
		return boss.isInFight();
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

	private String printMembers() {
		return members.stream().map(b -> b.getPerso().getPseudo()).collect(Collectors.joining(", "));
	}

	@Override
	public String toString() {
		return "Group [label=" + label + ", boss=" + boss.getPerso().getPseudo() + ", members=" + printMembers() + "]";
	}

}
