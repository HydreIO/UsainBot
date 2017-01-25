/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.data.player;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.dofus.protocol.Packet;
import fr.aresrpg.dofus.protocol.game.client.GameExtraInformationPacket;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.dofus.structures.game.FightSpawn;
import fr.aresrpg.dofus.util.DofusMapView;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.info.*;
import fr.aresrpg.eratz.domain.ia.Mind;
import fr.aresrpg.eratz.domain.ia.connection.ConnectionRunner;
import fr.aresrpg.eratz.domain.ia.fight.FightRunner;
import fr.aresrpg.eratz.domain.ia.harvest.HarvestRunner;
import fr.aresrpg.eratz.domain.ia.navigation.NavigationRunner;
import fr.aresrpg.eratz.domain.ia.path.Paths;
import fr.aresrpg.eratz.domain.ia.path.zone.fight.FightZone;
import fr.aresrpg.eratz.domain.ia.path.zone.harvest.HarvestZone;
import fr.aresrpg.eratz.domain.ia.waiter.WaitRunner;
import fr.aresrpg.eratz.domain.util.Closeable;
import fr.aresrpg.eratz.domain.util.Constants;
import fr.aresrpg.eratz.domain.util.functionnal.FutureHandler;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.ManchouPerso;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.function.Predicate;

public class BotPerso implements Closeable {

	private ManchouPerso perso;
	private Group group;

	private boolean online;

	private final Utilities utilities;
	private final ChatUtilities chatUtilities;
	private final FightUtilities fightUtilities;

	private final Mind mind;
	private final NavigationRunner navRunner;
	private final ConnectionRunner conRunner;
	private final HarvestRunner harRunner;
	private final FightRunner fiRunner;
	private final WaitRunner waRunner;

	private DofusMapView view;

	private boolean behaviorRunning;
	private CompletableFuture<?> behavior;

	public BotPerso(ManchouPerso perso) {
		this.perso = perso;
		this.utilities = new Utilities(this);
		this.chatUtilities = new ChatUtilities(this);
		this.fightUtilities = new FightUtilities(this);
		this.mind = new Mind(this);
		this.navRunner = new NavigationRunner(this);
		this.conRunner = new ConnectionRunner(this);
		this.harRunner = new HarvestRunner(this);
		this.fiRunner = new FightRunner(this);
		this.waRunner = new WaitRunner(this);
		this.view = new DofusMapView();
	}

	@Override
	public String toString() {
		return getPerso().toString();
	}

	@Override
	public void shutdown() {
	}

	public void startHarvest(Paths path) {
		behaviorRunning = true;
		HarvestZone zone = path.getHarvestPath(this);
		Executors.FIXED.execute(() -> {
			try {
				setBehavior(harvest(zone));
			} catch (Exception e) {
				LOGGER.error(e, "not handled");
			}
		});
	}

	public void startHarvestAndWait(Paths path) {
		behaviorRunning = true;
		HarvestZone zone = path.getHarvestPath(this);
		Executors.FIXED.execute(() -> {
			try {
				setBehavior(harvestAndWait(this, zone));
			} catch (Exception e) {
				LOGGER.error(e, "not handled");
			}
		});
	}

	public void startFight(Paths path) {
		behaviorRunning = true;
		FightZone zone = path.getFightPath(this);
		Executors.FIXED.execute(() -> {
			try {
				setBehavior(fight(zone));
			} catch (Exception e) {
				LOGGER.error(e, "not handled");
			}
		});
	}

	public void startFightWait(Paths path) {
		behaviorRunning = true;
		FightZone zone = path.getFightPath(this);
		Executors.FIXED.execute(() -> {
			try {
				setBehavior(fightAndWait(zone));
			} catch (Exception e) {
				LOGGER.error(e, "not handled");
			}
		});
	}

	public void setBehavior(CompletableFuture<?> behavior) {
		this.behavior = behavior;
	}

	/**
	 * @return the behavior
	 */
	public CompletableFuture<?> getBehavior() {
		return behavior;
	}

	public void setBehaviorRunning(boolean behaviorRunning) {
		this.behaviorRunning = behaviorRunning;
	}

	public void stopBehavior() {
		LOGGER.debug("Harvesting stoped !");
		this.behaviorRunning = false;
		behavior.cancel(true);
	}

	public void refreshMap() {
		sendPacketToServer(new GameExtraInformationPacket());
	}

	private CompletableFuture<?> harvest(HarvestZone zone) {
		LOGGER.debug("HARVEST cmd");
		if (!behaviorRunning) {
			LOGGER.debug("Behavior stopped !");
			return CompletableFuture.completedFuture(null);
		}
		mind.resetState();
		zone.sort();
		Threads.uSleep(1, TimeUnit.SECONDS);
		cancelInvits();
		return mind.harvest(zone.isPlayerJob(), zone.getRessources())
				.thenApply(h -> MapsManager.getMap(zone.getNextMap()))
				.thenCompose(mind::moveToMap)
				.handle(FutureHandler.handleEx()).thenCompose(c -> harvest(zone));
	}

	private CompletableFuture<?> harvestAndWait(BotPerso perso, HarvestZone zone) {
		LOGGER.debug("HARVEST AND WAIT");
		if (!behaviorRunning) return CompletableFuture.completedFuture(null);
		perso.getMind().resetState();
		BotMap map = MapsManager.getMap(zone.getNextMap());
		Threads.uSleep(1, TimeUnit.SECONDS);
		cancelInvits();
		return perso.getMind().harvest(zone.isPlayerJob(), zone.getRessources())
				.thenCompose(h -> perso.getMind().waitSpawn(zone.getRessources()))
				.thenApply(h -> map)
				.thenCompose(perso.getMind()::moveToMap)
				.handle(FutureHandler.handleEx()).thenCompose(c -> harvestAndWait(perso, zone));
	}

	private CompletableFuture<?> fight(FightZone zone) {
		LOGGER.debug("FIGHT cmd === 1");
		if (!behaviorRunning) return CompletableFuture.completedFuture(null);
		mind.resetState();
		if (getLifePercent() < 75) {
			LOGGER.debug("life percent = " + getLifePercent());
			Threads.uSleep(1, TimeUnit.SECONDS);
			getPerso().sit(true);
			Threads.uSleep(60, TimeUnit.SECONDS);
		}
		zone.sort();
		Threads.uSleep(1, TimeUnit.SECONDS);
		cancelInvits();
		return utilities.fightNearestMobGroup(zone::isValid)
				.thenCompose(c -> {
					if (c == null) return (CompletionStage) fight(zone);
					else if (c.booleanValue()) return (CompletionStage) mind.fight();
					else return mind.moveToMap(MapsManager.getMap(zone.getNextMap()));
				}).handle(FutureHandler.handleEx()).thenCompose(v -> fight(zone));
	}

	public void startRegenIfNeeded(int sec) {
		if (getLifePercent() < 75) {
			LOGGER.debug("life percent = " + getLifePercent());
			Threads.uSleep(1, TimeUnit.SECONDS);
			getPerso().sit(true);
			Threads.uSleep(sec, TimeUnit.SECONDS);
		}
	}

	public boolean isRegenNeeded() {
		LOGGER.warning("is regen needed ? " + getLifePercent());
		return getLifePercent() < 75;
	}

	private CompletableFuture<?> fightAndWait(FightZone zone) {
		LOGGER.debug("FIGHT AND WAIT");
		if (!behaviorRunning) return CompletableFuture.completedFuture(null);
		mind.resetState();
		Threads.uSleep(1, TimeUnit.SECONDS);
		cancelInvits();
		return utilities.fightNearestMobGroup(zone::isValid)
				.thenCompose(c -> {
					if (c == null) return (CompletionStage) fightAndWait(zone);
					else if (c.booleanValue()) return (CompletionStage) mind.fight();
					else return mind.waitMobSpawn(zone::isValid);
				}).handle(FutureHandler.handleEx()).thenCompose(c -> fightAndWait(zone));
	}

	public CompletableFuture<?> waitAndJoinGroupFight() {
		LOGGER.debug("WAIT AND JOIN");
		if (!behaviorRunning) return CompletableFuture.completedFuture(null);
		mind.resetState();
		Predicate<FightSpawn> canJoin = getGroup()::isMemberFight;
		return mind.waitFightSpawn(canJoin)
				.thenApply(e -> canJoin)
				.thenCompose(utilities::joinFight)
				.thenCompose(c -> {
					LOGGER.debug("fight joined ? booleanvalue = " + c);
					if (c.booleanValue()) return mind.fight();
					getPerso().sit(true);
					return CompletableFuture.completedFuture(null);
				}).handle(FutureHandler.handleEx());
	}

	public void cancelInvits() {
		if (perso.isDefied()) perso.cancelDefiInvit();
		if (perso.isInvitedExchange()) perso.cancelExchangeInvit();
		if (perso.isInvitedGrp()) perso.cancelGroupInvit();
		if (perso.isInvitedGuild()) perso.cancelGuildInvit();
	}

	/**
	 * @return the online
	 */
	public boolean isOnline() {
		return online;
	}

	/**
	 * @param online
	 *            the online to set
	 */
	public void setOnline(boolean online) {
		this.online = online;
	}

	public int getLifePercent() {
		if (getPerso().getLifeMax() == 0) return 100;
		return 100 * getPerso().getLife() / getPerso().getLifeMax();
	}

	/**
	 * @return the perso
	 */
	public ManchouPerso getPerso() {
		return perso;
	}

	public void speakIfAnnoyed() {
		if (isAnnoyed()) getPerso().speak(Chat.COMMON, Constants.getRandomAnnoyedSpeach());
	}

	public void sendPacketToServer(Packet... pkts) {
		Arrays.stream(pkts).forEach(this::sendPacketToServer);
	}

	public void sendPacketToServer(Packet pkt) {
		perso.sendPacketToServer(pkt);
	}

	public void sendPacketToServer(Packet pkt, int delay, TimeUnit unit) {
		Executors.SCHEDULED.schedule(() -> sendPacketToServer(pkt), delay, unit);
	}

	public void sendPacketToClient(Packet pkt, int delay, TimeUnit unit) {
		Executors.SCHEDULED.schedule(() -> sendPacketToClient(pkt), delay, unit);
	}

	public void sendPacketToClient(Packet... pkts) {
		if (!perso.isMitm()) throw new IllegalAccessError("This client is not a MITM !");
		Arrays.stream(pkts).forEach(this::sendPacketToClient);
	}

	public void sendPacketToClient(Packet pkt) {
		if (!perso.isMitm()) throw new IllegalAccessError("This client is not a MITM !");
		try {
			perso.getAccount().getProxy().getLocalConnection().send(pkt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isAnnoyed() {
		int annoyedCount = getPerso().getAnnoyedCount();
		if (annoyedCount > 5) {
			getPerso().resetAnnoyedCount();
			return true;
		}
		return false;
	}

	public void connectIn(long time, TimeUnit unit) {
		Executors.SCHEDULED.schedule(this::connect, time, unit);
	}

	public void connect() {
		perso.connect();
	}

	public boolean isInFight() {
		if (perso.getMap() == null) return false;
		return !perso.getMap().isEnded();
	}

	public boolean hasGroup() {
		return this.group != null;
	}

	/**
	 * @return the group
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

	/**
	 * @return the chatUtilities
	 */
	public ChatUtilities getChatUtilities() {
		return chatUtilities;
	}

	/**
	 * @return the fightUtilities
	 */
	public FightUtilities getFightUtilities() {
		return fightUtilities;
	}

	/**
	 * @return the mind
	 */
	public Mind getMind() {
		return mind;
	}

	/**
	 * @return the runner
	 */
	public NavigationRunner getNavRunner() {
		return navRunner;
	}

	/**
	 * @return the conRunner
	 */
	public ConnectionRunner getConRunner() {
		return conRunner;
	}

	/**
	 * @return the harRunner
	 */
	public HarvestRunner getHarRunner() {
		return harRunner;
	}

	/**
	 * @return the waRunner
	 */
	public WaitRunner getWaRunner() {
		return waRunner;
	}

	/**
	 * @return the fiRunner
	 */
	public FightRunner getFiRunner() {
		return fiRunner;
	}

	/**
	 * @return the view
	 */
	public DofusMapView getView() {
		return view;
	}

	/**
	 * @param view
	 *            the view to set
	 */
	public void setView(DofusMapView view) {
		this.view = view;
	}

	/**
	 * @return the utilities
	 */
	public Utilities getUtilities() {
		return utilities;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		return obj instanceof BotPerso && ((BotPerso) obj).perso.getUUID() == perso.getUUID();
	}

	@Override
	public int hashCode() {
		return (int) perso.getUUID();
	}

}
