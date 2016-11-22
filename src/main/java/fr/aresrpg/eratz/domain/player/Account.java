package fr.aresrpg.eratz.domain.player;

import fr.aresrpg.eratz.domain.proxy.Proxy;

import java.net.InetSocketAddress;

public class Account {
	private Proxy proxy;
	private InetSocketAddress adress;
	private String username;
	private String pass;
	private Perso player;

	public Account(String username, String pass) {
		this.username = username;
		this.pass = pass;
	}

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @return the adress
	 */
	public InetSocketAddress getAdress() {
		return adress;
	}

	/**
	 * @return Dofus proxy
	 */
	public Proxy getProxy() {
		return proxy;
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
	 * @return Player
	 */
	public Perso getPlayer() {
		return player;
	}

	/**
	 * Set the player
	 *
	 * @param player
	 *            Player
	 */
	public void setPlayer(Perso player) {
		this.player = player;
	}

	/**
	 * @return If the player is online
	 */
	public boolean isOnline() {
		return player != null;
	}

	@Override
	public String toString() {
		return "Account[user:***" + username.substring(4) + "|pass:***]";
	}

}
