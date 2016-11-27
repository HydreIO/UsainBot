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
import fr.aresrpg.dofus.protocol.ProtocolRegistry.Bound;
import fr.aresrpg.eratz.domain.TheBotFather;
import fr.aresrpg.eratz.domain.handler.BotHandler;
import fr.aresrpg.eratz.domain.player.state.AccountState;
import fr.aresrpg.eratz.domain.proxy.Proxy;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

import java.nio.channels.SocketChannel;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Account {
	private Proxy proxy;
	private String username;
	private String pass;
	private List<Perso> persos = new ArrayList<>();
	private Perso currentPlayed;
	private String currentHc;
	private AccountState state = AccountState.OFFLINE;
	private DofusConnection remoteConnection;

	public Account(String username, String pass) {
		this.username = username;
		this.pass = pass;
	}

	public void connect(Perso perso) {
		this.state = AccountState.BOT_ONLINE;
		Executors.FIXED.execute(() -> {
			try {
				System.out.println("[" + Instant.now().toString() + "] Connection de " + perso.getPseudo());
				SocketChannel channel = SocketChannel.open(TheBotFather.SERVER_ADRESS);
				setCurrentPlayed(perso);
				DofusConnection connection = new DofusConnection(perso.getPseudo(), channel, new BotHandler(this), Bound.SERVER);
				setRemoteConnection(connection);
				while (getState() == AccountState.BOT_ONLINE)
					connection.read();
			} catch (Exception e) {
				this.state = AccountState.OFFLINE;
				System.out.println("Bot déconnecté."); // test debug
				e.printStackTrace(); // test debug
			}
		});
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(AccountState state) {
		this.state = state;
	}

	/**
	 * @return the remoteConnection
	 */
	public DofusConnection getRemoteConnection() {
		return remoteConnection;
	}

	/**
	 * @param remoteConnection
	 *            the remoteConnection to set
	 */
	public void setRemoteConnection(DofusConnection remoteConnection) {
		this.remoteConnection = remoteConnection;
	}

	/**
	 * @param currentHc
	 *            the currentHc to set
	 */
	public void setCurrentHc(String currentHc) {
		this.currentHc = currentHc;
	}

	/**
	 * @return the currentHc
	 */
	public String getCurrentHc() {
		return currentHc;
	}

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @return Dofus proxy
	 */
	public Proxy getProxy() {
		return proxy;
	}

	/**
	 * @return the state
	 */
	public AccountState getState() {
		return state;
	}

	/**
	 * @return the clientOnline
	 */
	public boolean isClientOnline() {
		return getState() == AccountState.CLIENT_IN_GAME || getState() == AccountState.CLIENT_IN_REALM;
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
	 * Set the dofus proxy
	 *
	 * @param proxy
	 *            Dofus proxy
	 */
	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
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
		return "Account[user:***" + username.substring(4) + "|pass:***]";
	}

}
