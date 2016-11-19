package fr.aresrpg.eratz.domain.player;

import fr.aresrpg.eratz.domain.proxy.DofusProxy;

import java.net.InetAddress;

public class DofusProxiedClient {
    private DofusProxy proxy;
    private String username;
    private InetAddress address;
    private DofusProxiedPlayer player;

    public DofusProxiedClient(DofusProxy proxy, String username, InetAddress address) {
        this.proxy = proxy;
        this.username = username;
        this.address = address;
    }

    /**
     * @return Dofus proxy
     */
    public DofusProxy getProxy() {
        return proxy;
    }

    /**
     * Set the dofus proxy
     *
     * @param proxy Dofus proxy
     */
    public void setProxy(DofusProxy proxy) {
        this.proxy = proxy;
    }

    /**
     * @return Username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return Address
     */
    public InetAddress getAddress() {
        return address;
    }

    /**
     * @return Player
     */
    public DofusProxiedPlayer getPlayer() {
        return player;
    }

    /**
     * Set the player
     *
     * @param player Player
     */
    public void setPlayer(DofusProxiedPlayer player) {
        this.player = player;
    }

    /**
     * @return If the player is online
     */
    public boolean isOnline() {
        return player != null;
    }

    /**
     * Send data to the server
     *
     * @param data Data
     */
    public void sendData(String data) {

    }
}
