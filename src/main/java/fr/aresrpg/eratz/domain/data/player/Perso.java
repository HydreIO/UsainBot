/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.data.player;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.dofus.protocol.DofusConnection;
import fr.aresrpg.dofus.protocol.Packet;
import fr.aresrpg.dofus.protocol.ProtocolRegistry.Bound;
import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.dofus.structures.server.*;
import fr.aresrpg.dofus.util.DofusMapView;
import fr.aresrpg.eratz.domain.TheBotFather;
import fr.aresrpg.eratz.domain.data.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.data.dofus.player.*;
import fr.aresrpg.eratz.domain.data.player.info.*;
import fr.aresrpg.eratz.domain.data.player.inventory.Inventory;
import fr.aresrpg.eratz.domain.data.player.inventory.PlayerInventory;
import fr.aresrpg.eratz.domain.data.player.object.Group;
import fr.aresrpg.eratz.domain.data.player.state.AccountState;
import fr.aresrpg.eratz.domain.ia.ability.move.Navigation;
import fr.aresrpg.eratz.domain.ia.ability.move.NavigationImpl;
import fr.aresrpg.eratz.domain.ia.mind.BaseMind;
import fr.aresrpg.eratz.domain.ia.mind.Mind;
import fr.aresrpg.eratz.domain.io.handler.bot.BotHandler;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;
import fr.aresrpg.eratz.domain.util.config.Variables;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Perso {

	private final Account account;
	private int id;
	private String pseudo;

	private final Navigation navigation = new NavigationImpl(this);
	private final Mind mind = new BaseMind(this);

	private final AbilityInfo abilities = new AbilityInfo(this);
	private final LogInfo logInfos = new LogInfo(this);
	private final BotInfo botInfos = new BotInfo(this);
	private final MapInfo mapInfos = new MapInfo(this);
	private final FightInfo fightInfos = new FightInfo(this);
	private final StatsInfo statsInfos = new StatsInfo(this);
	private final DofusMapView debugView = new DofusMapView();
	private final DofusServer server;
	private final Inventory inventory = new PlayerInventory(this);

	private Group group;

	public Perso(int id, String pseudo, Account account, BotJob job, Classe classe, Genre sexe, Server srv) {
		this.id = id;
		this.pseudo = pseudo;
		this.account = account;
		this.botInfos.setBotJob(job);
		this.server = new DofusServer(srv.getId(), ServerState.ONLINE, 0, true);
	}

	public Perso(int id, String pseudo, Account account, Classe classe, Genre sexe, Server srv) {
		this(id, pseudo, account, null, classe, sexe, srv);
	}

	/**
	 * @return the mind
	 */
	public Mind getMind() {
		return mind;
	}

	/**
	 * @return the statsInfos
	 */
	public StatsInfo getStatsInfos() {
		return statsInfos;
	}

	/**
	 * @return the fightInfos
	 */
	public FightInfo getFightInfos() {
		return fightInfos;
	}

	/**
	 * @return the abilities
	 */
	public AbilityInfo getAbilities() {
		return abilities;
	}

	/**
	 * @return the logInfos
	 */
	public LogInfo getLogInfos() {
		return logInfos;
	}

	public void sendPacketToServer(Packet pkt) {
		try {
			getAccount().getRemoteConnection().send(pkt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the pseudo
	 */
	public String getPseudo() {
		return pseudo;
	}

	/**
	 * @return the botInfos
	 */
	public BotInfo getBotInfos() {
		return botInfos;
	}

	/**
	 * @return the mapInfos
	 */
	public MapInfo getMapInfos() {
		return mapInfos;
	}

	/**
	 * @return the server
	 */
	public DofusServer getServer() {
		return server;
	}

	/**
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * @return the debugView
	 */
	public DofusMapView getDebugView() {
		return debugView;
	}

	public void connect() {
		Account a = getAccount();
		if (a.getLastConnection() + (Variables.SEC_AFTER_CRASH * 1000) > System.currentTimeMillis())
			throw new IllegalAccessError("[ANTI-BAN] Connection refused, please wait at least "
					+ Variables.SEC_AFTER_CRASH + "s before every reconnection.");
		System.out.println("[" + Instant.now().toString() + "] Connecting " + getPseudo());
		if (a.isClientOnline())
			throw new IllegalAccessError(
					"The account of " + getPseudo() + " is already online | No need to connect the bot");
		if (a.isBotOnline())
			throw new IllegalAccessError("The bot " + a.getCurrentPlayed().getPseudo()
					+ " is already online | you need to deconnect it first");
		a.setState(AccountState.BOT_ONLINE);
		try {
			SocketChannel channel = SocketChannel.open(TheBotFather.SERVER_ADRESS);
			a.setCurrentPlayed(this);
			a.setRemoteConnection(new DofusConnection<>(getPseudo(), channel, new BotHandler(this), Bound.SERVER)); // fix temporaire via proxy handler pour corriger le bug du parse en mitm
			Executors.FIXED.execute(a::readRemote);
		} catch (IOException e) {
			a.setState(AccountState.OFFLINE);
			System.out.println("Bot crash."); // test debug
			e.printStackTrace(); // test debug
		}
		a.setLastConnection(System.currentTimeMillis());
	}

	public boolean isInFight(Fight f) {
		return f == getFightInfos().getCurrentFight();
	}

	public boolean isInFight() {
		return getFightInfos().getCurrentFight() != null;
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

	public void crashReport(String msg) {
		getAbilities().getBaseAbility().speak(Channel.ADMIN, msg);
		disconnect(msg, -1);
	}

	private int quantityOf(int id, Set<Item> set) { // pr item non stackable
		return (int) set.stream().filter(i -> i.getUniqueId() == id).count();
	}

	public boolean inventoryContainsItem(int id) {
		return getInventory().getContents().keySet().contains(id);
	}

	public int getQuantityInInventoryOf(int itemId) {
		Item i = getInventory().getItem(itemId);
		return i == null ? 0 : i.getQuantity();
	}

	public int getQuantityInBanqueOf(int itemId) {
		Item i = getAccount().getBanque().getItem(itemId);
		return i == null ? 0 : i.getQuantity();
	}

	public boolean allGroupIsInFight(Fight currentFight) {
		if (!hasGroup()) return false;
		for (Perso p : getGroup().getMembers())
			if (!p.isInFight(currentFight)) return false;
		return false;
	}

	/**
	 * Disconnect the player
	 * 
	 * @param reason
	 *            the reason of the disconnect
	 * @param timeToReco
	 *            the time in second before reconnection (set to -1 to stay offline) the bot (see config to disable auto reconnection)
	 */
	public void disconnect(String reason, int timeToReco) {
		Executors.FIXED.execute(() -> {
			if (getAccount().isClientOnline())
				throw new IllegalAccessError("Unable to disconnect " + getPseudo() + " ! | A client is online");
			System.out.println("Disconnecting " + getPseudo());
			Account a = getAccount();
			try {
				a.getRemoteConnection().close();
				a.setCurrentPlayed(null);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				a.setState(AccountState.OFFLINE);
				System.out.println(getPseudo() + " disconnected. | " + reason);
			}
			if (timeToReco == -1 || Variables.AUTO_RECONNECTION) {
				Threads.uSleep(timeToReco, TimeUnit.SECONDS);
			}
		});
	}

	/**
	 * @return the navigation
	 */
	public Navigation getNavigation() {
		return navigation;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		return obj instanceof Perso && ((Perso) obj).getId() == getId();
	}

	@Override
	public String toString() {
		return "Perso [account=" + account + ", id=" + id + ", pseudo=" + pseudo + ", navigation=" + navigation + ", mind=" + mind + ", abilities=" + abilities + ", logInfos=" + logInfos
				+ ", botInfos=" + botInfos + ", mapInfos=" + mapInfos + ", fightInfos=" + fightInfos + ", statsInfos=" + statsInfos + ", debugView=" + debugView + ", server=" + server + ", inventory="
				+ inventory + ", group=" + group + "]";
	}

}
