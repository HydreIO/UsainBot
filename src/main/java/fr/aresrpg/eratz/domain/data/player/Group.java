package fr.aresrpg.eratz.domain.data.player;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.structures.game.FightSpawn;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.ia.path.zone.fight.FightZone;
import fr.aresrpg.eratz.domain.util.functionnal.FutureHandler;
import fr.aresrpg.tofumanchou.domain.data.entity.mob.MobGroup;
import fr.aresrpg.tofumanchou.infra.data.ManchouMap;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Predicate;
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

	public void setBehaviorRunning(boolean behaviorRunning) {
		this.behaviorRunning = behaviorRunning;
	}

	public CompletableFuture<?> startFight(FightZone zone, boolean bossIsBot) {
		LOGGER.debug("start group fight");
		if (!behaviorRunning) return CompletableFuture.completedFuture(null);
		regenIfNeeded();
		LOGGER.debug("regen ok");
		zone.sort();
		Predicate<MobGroup> isValid = zone::isValid;
		Threads.uSleep(1, TimeUnit.SECONDS);
		boss.cancelInvits();
		members.forEach(BotPerso::cancelInvits);
		LOGGER.debug("invit canceled");
		return goToMap(MapsManager.getMap(zone.getNextMap()), bossIsBot)
				.thenCompose(e -> waitAndJoinFight())
				.thenCombine(boss.getUtilities().fightNearestMobGroup(isValid), (a, b) -> b)
				.thenCompose(c -> {
					if (c == null || !c.booleanValue()) {
						resetWaiting();
						return (CompletionStage) startFight(zone, bossIsBot);
					}
					return boss.getMind().fight();
				}).handle(FutureHandler.handleEx()).thenCompose(v -> startFight(zone, bossIsBot));
	}

	private void regenIfNeeded() {
		boolean regen = false;
		if (boss.isRegenNeeded()) {
			regen = true;
			boss.getPerso().sit(true);
		}
		for (BotPerso pe : members)
			if (pe.isRegenNeeded()) {
				Threads.uSleep(Randoms.nextBetween(1, 2), TimeUnit.SECONDS);
				regen = true;
				pe.getPerso().sit(true);
			}
		if (regen) Threads.uSleep(60, TimeUnit.SECONDS);
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

	public void setMembersWaiting(Set<CompletableFuture> membersWaiting) {
		resetWaiting();
		this.membersWaiting.addAll(membersWaiting);
	}

	public void resetWaiting() {
		this.membersWaiting.forEach(m -> m.cancel(true));
		this.membersWaiting.clear();
	}

	/**
	 * Tout les membre du groupe vont se diriger vers la map
	 * 
	 * @param map
	 *            la map
	 * @param isBossABot
	 *            si le boss n'est pas un bot alors seul les membres iront vers la destination (utile quand on joue en personne sur le compte du boss)
	 * @return un completableFuture complété quand tout les membres sont arrivés
	 */
	public CompletableFuture<?> goToMap(BotMap map, boolean isBossABot) {
		Set<CompletableFuture> movements = new HashSet<>();
		if (isBossABot) movements.add(boss.getMind().moveToMap(map));
		members.forEach(p -> {
			LOGGER.debug("for " + p);
			if (p == null) return;
			Threads.uSleep(Randoms.nextBetween(1, 3), TimeUnit.SECONDS);
			movements.add(p.getMind().moveToMap(map));
		});
		return CompletableFuture.allOf(movements.stream().toArray(CompletableFuture[]::new));
	}

	public CompletableFuture<?> waitAndJoinFight() {
		Set<CompletableFuture> waiting = new HashSet<>();
		members.forEach(p -> {
			p.setBehaviorRunning(true);
			p.setBehavior(p.waitAndJoinGroupFight());
			waiting.add(p.getBehavior());
		});
		setMembersWaiting(waiting);
		LOGGER.debug("wait and join fight");
		return CompletableFuture.allOf(membersWaiting.stream().toArray(CompletableFuture[]::new));
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
