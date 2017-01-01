/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.data.player;

import fr.aresrpg.dofus.protocol.Packet;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.eratz.domain.data.player.info.ChatInfo;
import fr.aresrpg.eratz.domain.data.player.object.Group;
import fr.aresrpg.eratz.domain.data.player.state.PlayerState;
import fr.aresrpg.eratz.domain.ia.mind.BaseMind;
import fr.aresrpg.eratz.domain.ia.mind.Mind;
import fr.aresrpg.eratz.domain.util.Closeable;
import fr.aresrpg.tofumanchou.domain.data.item.Item;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.ManchouPerso;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class BotPerso implements Closeable {

	private final Mind mind = new BaseMind(this);
	private ManchouPerso perso;
	private Group group;
	private PlayerState state;
	private ScheduledFuture sch;
	private boolean online;

	private ChatInfo chatInfos = new ChatInfo(this);

	public BotPerso(ManchouPerso perso) {
		this.perso = perso;
		this.sch = Executors.SCHEDULER.register(this::scheduledActions, 10, TimeUnit.SECONDS);
	}

	public void scheduledActions() { // methode éxécutée toute les 10s, utile pour divers petit check

		perso.useRessourceBags();
	}

	@Override
	public void shutdown() {
		sch.cancel(true);
		mind.shutdown();
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

	public void respondTo(String msg, Chat c) {
		String resp = chatInfos.getResponse(msg);
		if (resp != null) perso.speak(c, resp);
	}

	/**
	 * @return the chatInfos
	 */
	public ChatInfo getChatInfos() {
		return chatInfos;
	}

	/**
	 * @return the perso
	 */
	public ManchouPerso getPerso() {
		return perso;
	}

	/**
	 * @return the mind
	 */
	public Mind getMind() {
		return mind;
	}

	/**
	 * @return the state
	 */
	public PlayerState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(PlayerState state) {
		this.state = state;
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

	public boolean isAtAstrubZaap() {
		return perso.getMap().isOnCoords(4, -19);
	}

	public void connectIn(long time, TimeUnit unit) {
		Executors.SCHEDULED.schedule(this::connect, time, unit);
	}

	public void connect() {
		perso.connect();
	}

	public boolean isInFight() {
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

	private int quantityOf(int id, Set<Item> set) { // pr item non stackable
		return (int) set.stream().filter(i -> i.getTypeId() == id).count();
	}

	public boolean inventoryContainsItem(long itemUid) {
		return perso.getInventory().getContents().containsKey(itemUid);
	}

	public int getQuantityInInventoryOf(long itemUid) {
		Item i = perso.getInventory().getItem(itemUid);
		return i == null ? 0 : i.getAmount();
	}

	public int getQuantityInBanqueOf(long itemUid) {
		Item i = perso.getAccount().getBank().getItem(itemUid);
		return i == null ? 0 : i.getAmount();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		return obj instanceof BotPerso && ((BotPerso) obj).perso.getUUID() == perso.getUUID();
	}

}
