package fr.aresrpg.eratz.domain.player;

import fr.aresrpg.eratz.domain.proxy.Proxy;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Account {
	private Proxy proxy;
	private InetSocketAddress adress;
	private String username;
	private String pass;
	private List<Perso> persos = new ArrayList<>();
	private Perso currentPlayed;
	private boolean clientOnline; // dofus client online
	private boolean online; // bot online

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
	 * @return the clientOnline
	 */
	public boolean isClientOnline() {
		return clientOnline;
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
	 * @param clientOnline
	 *            the clientOnline to set
	 */
	public void setClientOnline(boolean clientOnline) {
		this.clientOnline = clientOnline;
	}

	/**
	 * @param adress
	 *            the adress to set
	 */
	public void setAdress(InetSocketAddress adress) {
		this.adress = adress;
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
