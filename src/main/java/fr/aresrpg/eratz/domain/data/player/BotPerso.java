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
import fr.aresrpg.dofus.util.DofusMapView;
import fr.aresrpg.eratz.domain.data.player.info.*;
import fr.aresrpg.eratz.domain.ia.Mind;
import fr.aresrpg.eratz.domain.ia.connection.ConnectionRunner;
import fr.aresrpg.eratz.domain.ia.harvest.HarvestRunner;
import fr.aresrpg.eratz.domain.ia.navigation.NavigationRunner;
import fr.aresrpg.eratz.domain.util.Closeable;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;
import fr.aresrpg.tofumanchou.infra.data.ManchouPerso;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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

	private DofusMapView view;

	public BotPerso(ManchouPerso perso) {
		this.perso = perso;
		this.utilities = new Utilities(this);
		this.chatUtilities = new ChatUtilities(this);
		this.fightUtilities = new FightUtilities(this);
		this.mind = new Mind(this);
		this.navRunner = new NavigationRunner(this);
		this.conRunner = new ConnectionRunner(this);
		this.harRunner = new HarvestRunner(this);
		this.view = new DofusMapView();
	}

	@Override
	public void shutdown() {
		perso.useRessourceBags();
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

	/**
	 * @return the perso
	 */
	public ManchouPerso getPerso() {
		return perso;
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
