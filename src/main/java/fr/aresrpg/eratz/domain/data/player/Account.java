/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.data.player;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.protocol.DofusConnection;
import fr.aresrpg.dofus.protocol.mount.client.MountPlayerPacket;
import fr.aresrpg.eratz.domain.data.GroupsManager;
import fr.aresrpg.eratz.domain.data.player.inventory.Banque;
import fr.aresrpg.eratz.domain.data.player.state.AccountState;
import fr.aresrpg.eratz.domain.data.player.state.PlayerState;
import fr.aresrpg.eratz.domain.ia.behavior.AntiAfkBehavior;
import fr.aresrpg.eratz.domain.ia.behavior.fight.type.PassTurnBehavior;
import fr.aresrpg.eratz.domain.io.proxy.Proxy;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Account {

	private Proxy proxy;
	private String username;
	private String pass;
	private List<Perso> persos = new ArrayList<>();
	private Set<String> friends = new HashSet();
	private Perso currentPlayed;
	private Perso defaultBot;
	private AccountState state = AccountState.OFFLINE;
	private DofusConnection remoteConnection;
	private long lastConnection;
	private Banque banque;

	public Account(String username, String pass) {
		this.username = username;
		this.pass = pass;
		this.banque = new Banque(this);
	}

	/**
	 * @return the proxy
	 */
	public Proxy getProxy() {
		return proxy;
	}

	public void notifyBotOnline() { // pour confirmer que le bot est bien en jeux
		setState(AccountState.BOT_ONLINE);
		if (!getCurrentPlayed().getMind().isRunning()) {
			Executors.FIXED.execute(() -> {
				try {
					getCurrentPlayed().getMind().process();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			});
			getCurrentPlayed().getFightInfos().setCurrentFightBehavior(new PassTurnBehavior(currentPlayed));
			getCurrentPlayed().setState(PlayerState.IDLE);
			Executors.FIXED.execute(new AntiAfkBehavior(currentPlayed, true)::start);
			GroupsManager.getInstance().updateGroups(currentPlayed);
		}
	}

	public void notifyMitmOnline() {
		if (!getCurrentPlayed().getMind().isRunning()) {
			Executors.FIXED.execute(() -> {
				try {
					getCurrentPlayed().getMind().process();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			});
			getCurrentPlayed().getFightInfos().setCurrentFightBehavior(new PassTurnBehavior(currentPlayed));
			Executors.FIXED.execute(new AntiAfkBehavior(currentPlayed, true)::start);
			getCurrentPlayed().sendPacketToServer(new MountPlayerPacket());
			GroupsManager.getInstance().updateGroups(getCurrentPlayed());
		}
		getCurrentPlayed().setState(PlayerState.IDLE);
	}

	/**
	 * @param proxy
	 *            the proxy to set
	 */
	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	/**
	 * @return the banque
	 */
	public Banque getBanque() {
		return banque;
	}

	/**
	 * @return the remoteConnection
	 */
	public DofusConnection getRemoteConnection() {
		return remoteConnection;
	}

	/**
	 * Disconnect the current perso if there is one and connect the new<br>
	 * If there is a connected perso then the method wait the minimum configured time before connection to avoid auto ban
	 * 
	 * @param perso
	 *            the new perso to connect
	 */
	public void switchPerso(Perso perso) {
		if (getCurrentPlayed() != null) getCurrentPlayed().disconnect("Switching to " + perso.getPseudo());
		LOGGER.info("Switching to " + perso.getPseudo() + " in ~5s");
		perso.connectIn(Randoms.nextBetween(4, 7), TimeUnit.SECONDS);
	}

	public void addPerso(Perso p) {
		if (getPersos().contains(p)) return;
		getPersos().add(p);
	}

	// ============= UTIL ===================

	/**
	 * Listen for packet from dofus
	 * 
	 * @throws IOException
	 *             if some I/O error occur
	 */
	public void readRemote() {
		try {
			getRemoteConnection().start();
		} catch (IOException e) {
			e.printStackTrace();
			setState(AccountState.OFFLINE);
			if (getCurrentPlayed() != null) getCurrentPlayed().connectIn(10, TimeUnit.SECONDS);
		}
	}

	/**
	 * @return true if the bot or the client is online
	 */
	public boolean isActive() {
		return isBotOnline() || isClientOnline();
	}

	public boolean isOffline() {
		return getState() == AccountState.OFFLINE;
	}

	/**
	 * @return true if the client is online
	 */
	public boolean isClientOnline() {
		return getState() == AccountState.CLIENT_IN_GAME || getState() == AccountState.CLIENT_IN_REALM;
	}

	/**
	 * @return true if the bot is online
	 */
	public boolean isBotOnline() {
		return getState() == AccountState.BOT_ONLINE;
	}

	// =========== GET / SET =================
	/**
	 * @return the lastConnection
	 */
	public long getLastConnection() {
		return lastConnection;
	}

	/**
	 * @param lastConnection
	 *            the lastConnection to set
	 */
	public void setLastConnection(long lastConnection) {
		this.lastConnection = lastConnection;
	}

	/**
	 * @return the defaultBot
	 */
	public Perso getDefaultBot() {
		return defaultBot;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(AccountState state) {
		this.state = state;
	}

	/**
	 * @param remoteConnection
	 *            the remoteConnection to set
	 */
	public void setRemoteConnection(DofusConnection remoteConnection) {
		this.remoteConnection = remoteConnection;
	}

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @return the state
	 */
	public AccountState getState() {
		return state;
	}

	/**
	 * @param currentPlayed
	 *            the currentPlayed to set
	 */
	public void setCurrentPlayed(Perso currentPlayed) {
		this.currentPlayed = currentPlayed;
	}

	/**
	 * @return the currentPlayed
	 */
	public Perso getCurrentPlayed() {
		return currentPlayed;
	}

	/**
	 * @param pass
	 *            the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param defaultBot
	 *            the defaultBot to set
	 */
	public void setDefaultBot(Perso defaultBot) {
		this.defaultBot = defaultBot;
	}

	/**
	 * @return Username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the persos
	 */
	public List<Perso> getPersos() {
		return persos;
	}

	/**
	 * @param persos
	 *            the persos to set
	 */
	public void setPersos(List<Perso> persos) {
		this.persos = persos;
	}

	@Override
	public String toString() {
		return "Account[user:" + hidePseudo() + "|pass:***]";
	}

	public String hidePseudo() {
		if (username.length() > 5) {
			return "*****" + username.substring(5);
		} else
			return "*****";
	}

}
