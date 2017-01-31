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
import fr.aresrpg.dofus.util.DofusMapView;
import fr.aresrpg.eratz.domain.ai.Layers;
import fr.aresrpg.eratz.domain.data.player.info.*;
import fr.aresrpg.eratz.domain.util.Closeable;
import fr.aresrpg.eratz.domain.util.Constants;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.ManchouCell;
import fr.aresrpg.tofumanchou.infra.data.ManchouPerso;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class BotPerso implements Closeable {

	private ManchouPerso perso;
	private Group group;

	private boolean online;

	private final Utilities utilities;
	private final ChatUtilities chatUtilities;
	private final FightUtilities fightUtilities;
	private final HarvestUtilities harvestUtilities;
	private final MapUtilities mapUtilities;

	private DofusMapView view;

	private Layers layers;

	private boolean behaviorRunning;
	private CompletableFuture<?> behavior;

	public BotPerso(ManchouPerso perso) {
		this.perso = perso;
		this.utilities = new Utilities(this);
		this.chatUtilities = new ChatUtilities(this);
		this.fightUtilities = new FightUtilities(this);
		this.harvestUtilities = new HarvestUtilities(this);
		this.mapUtilities = new MapUtilities(this);
		this.layers = new Layers(this);
		this.view = new DofusMapView();
	}

	@Override
	public String toString() {
		return getPerso().toString();
	}

	@Override
	public void shutdown() {
	}

	public Layers getLayers() {
		return layers;
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

	public ManchouCell getCellAt(int cellid) {
		return getPerso().getMap().getCells()[cellid];
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
	 * @return the harvestUtilities
	 */
	public HarvestUtilities getHarvestUtilities() {
		return harvestUtilities;
	}

	/**
	 * @return the mapUtilities
	 */
	public MapUtilities getMapUtilities() {
		return mapUtilities;
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
