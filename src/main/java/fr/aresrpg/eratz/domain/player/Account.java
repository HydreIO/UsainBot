/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.player;

import fr.aresrpg.dofus.protocol.DofusConnection;
import fr.aresrpg.eratz.domain.handler.bot.BotHandler;
import fr.aresrpg.eratz.domain.player.state.AccountState;
import fr.aresrpg.eratz.domain.util.Threads;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;
import fr.aresrpg.eratz.domain.util.config.Variables;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Account {
	private String username;
	private String pass;
	private List<Perso> persos = new ArrayList<>();
	private Perso currentPlayed;
	private Perso defaultBot;
	private AccountState state = AccountState.OFFLINE;
	private DofusConnection remoteConnection;
	private BotHandler botHandler = new BotHandler(this);
	private long lastConnection;

	public Account(String username, String pass) {
		this.username = username;
		this.pass = pass;
	}

	/**
	 * @return the remoteConnection
	 */
	public DofusConnection getRemoteConnection() {
		return remoteConnection;
	}

	public void connect(Perso perso) {
		perso.connect();
	}

	public void disconnect(Perso perso) {
		perso.disconnect();
	}

	/**
	 * Called when the client close the connection or crash
	 */
	public void notifyDisconnect() {
		System.out.println("Client disconnected !");
		setState(AccountState.OFFLINE);
		if (!Variables.CONNECT_BOT_ON_CLIENT_DECONNECTION || getDefaultBot() == null) return;
		Executors.FIXED.execute(() -> {
			System.out.println("Connecting bot on " + getDefaultBot().getPseudo() + " in " + Variables.SEC_AFTER_CRASH + "s !");
			Threads.sleep(Variables.SEC_AFTER_CRASH, TimeUnit.SECONDS);
			getDefaultBot().connect();
		});
	}

	/**
	 * Disconnect the current perso if there is one and connect the new<br>
	 * If there is a connected perso then the method wait the minimum configured time before connection to avoid auto ban
	 * 
	 * @param perso
	 *            the new perso to connect
	 */
	public void switchPerso(Perso perso) {
		if (getCurrentPlayed() != null) disconnect(getCurrentPlayed());
		long timeleft = (getLastConnection() + (Variables.SEC_AFTER_CRASH * 1000)) - System.currentTimeMillis();
		if (timeleft < 0)
			try {
			System.out.println("[ANTI-BAN] Reconnecting in " + Instant.ofEpochMilli(timeleft + 2000).getEpochSecond() + "s...");
			Thread.sleep(timeleft + 2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		perso.connect();
	}

	// ============= UTIL ===================

	/**
	 * Listen for packet from dofus while the bot is online and the client is offline
	 * 
	 * @throws IOException
	 *             if some I/O error occur
	 */
	public void readRemote() {
		while (isActive())
			try {
				getRemoteConnection().read();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**
	 * @return true if the bot or the client is online
	 */
	public boolean isActive() {
		return isBotOnline() || isClientOnline();
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
	 * @return the botHandler
	 */
	public BotHandler getBotHandler() {
		return botHandler;
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
		return "Account[user:*****" + username.substring(5) + "|pass:***]";
	}

}
