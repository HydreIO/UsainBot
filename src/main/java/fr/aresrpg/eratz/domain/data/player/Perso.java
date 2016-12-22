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

import fr.aresrpg.dofus.protocol.DofusConnection;
import fr.aresrpg.dofus.protocol.Packet;
import fr.aresrpg.dofus.protocol.ProtocolRegistry.Bound;
import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.dofus.structures.server.DofusServer;
import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.dofus.util.DofusMapView;
import fr.aresrpg.eratz.domain.TheBotFather;
import fr.aresrpg.eratz.domain.data.AccountsManager;
import fr.aresrpg.eratz.domain.data.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.data.dofus.player.*;
import fr.aresrpg.eratz.domain.data.player.info.*;
import fr.aresrpg.eratz.domain.data.player.inventory.Inventory;
import fr.aresrpg.eratz.domain.data.player.inventory.PlayerInventory;
import fr.aresrpg.eratz.domain.data.player.object.Group;
import fr.aresrpg.eratz.domain.data.player.state.AccountState;
import fr.aresrpg.eratz.domain.ia.ability.move.Navigation;
import fr.aresrpg.eratz.domain.ia.ability.move.NavigationImpl;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.mind.BaseMind;
import fr.aresrpg.eratz.domain.ia.mind.Mind;
import fr.aresrpg.eratz.domain.io.handler.bot.BotPacketHandler;
import fr.aresrpg.eratz.domain.util.Closeable;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Perso implements Closeable {

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
	private final PvpInfo pvpInfos = new PvpInfo(this);

	private final Server server;
	private final Inventory inventory = new PlayerInventory(this);

	private Group group;
	private Behavior currentBehavior;

	public Perso(int id, String pseudo, Account account, BotJob job, Classe classe, Genre sexe, Server srv) {
		this.id = id;
		this.pseudo = pseudo;
		this.account = account;
		this.botInfos.setBotJob(job);
		this.server = srv;
	}

	public Perso(int id, String pseudo, Account account, Classe classe, Genre sexe, Server srv) {
		this(id, pseudo, account, null, classe, sexe, srv);
	}

	@Override
	public void shutdown() {
		getAccount().setState(AccountState.OFFLINE);
		mind.shutdown();
		abilities.shutdown();
		logInfos.shutdown();
		botInfos.shutdown();
		mapInfos.shutdown();
		fightInfos.shutdown();
		statsInfos.shutdown();
		pvpInfos.shutdown();
	}

	/**
	 * @return the mind
	 */
	public Mind getMind() {
		return mind;
	}

	/**
	 * @return the currentBehavior
	 */
	public Behavior getCurrentBehavior() {
		return currentBehavior;
	}

	/**
	 * @param currentBehavior
	 *            the currentBehavior to set
	 */
	public void setCurrentBehavior(Behavior currentBehavior) {
		this.currentBehavior = currentBehavior;
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
	 * @return the pvpInfos
	 */
	public PvpInfo getPvpInfos() {
		return pvpInfos;
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

	public void sendPacketToServer(Packet... pkts) {
		Arrays.stream(pkts).forEach(this::sendPacketToServer);
	}

	public void sendPacketToServer(Packet pkt) {
		try {
			getAccount().getRemoteConnection().send(pkt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendPacketToServer(Packet pkt, int delay, TimeUnit unit) {
		Executors.SCHEDULED.schedule(() -> sendPacketToServer(pkt), delay, unit);
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

	public DofusServer getDofusServer() {
		if (this.server == Server.ERATZ) return AccountsManager.ERATZ;
		else return AccountsManager.HENUAL;
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
	public Server getServer() {
		return server;
	}

	/**
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param pseudo
	 *            the pseudo to set
	 */
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	/**
	 * @return the debugView
	 */
	public DofusMapView getDebugView() {
		return debugView;
	}

	public void connectIn(long time, TimeUnit unit) {
		Executors.SCHEDULED.schedule(this::connect, time, unit);
	}

	public void connect() {
		Account a = getAccount();
		if (a.getState() == AccountState.CONNECTING) return;
		a.setState(AccountState.CONNECTING);
		LOGGER.info("Connecting " + getPseudo());
		if (a.isClientOnline()) {
			LOGGER.error("The account of " + getPseudo() + " is already online | No need to connect the bot");
			return;
		}
		if (a.isBotOnline()) {
			LOGGER.error("The bot " + a.getCurrentPlayed().getPseudo() + " is already online | you need to deconnect it first");
			return;
		}
		try {
			SocketChannel channel = SocketChannel.open(TheBotFather.SERVER_ADRESS);
			a.setCurrentPlayed(this);
			BotPacketHandler han = new BotPacketHandler(this);
			han.setAccount(getAccount());
			a.setRemoteConnection(new DofusConnection<>(getPseudo(), channel, han, Bound.SERVER));
			Executors.FIXED.execute(a::readRemote);
		} catch (IOException e) {
			e.printStackTrace(); // test debug
			shutdown();
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
		disconnect(msg);
	}

	private int quantityOf(int id, Set<Item> set) { // pr item non stackable
		return (int) set.stream().filter(i -> i.getItemTypeId() == id).count();
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
	 */
	public void disconnect(String reason) {
		Executors.FIXED.execute(() -> {
			if (getAccount().isClientOnline())
				throw new IllegalAccessError("Unable to disconnect " + getPseudo() + " ! | A client is online");
			LOGGER.info("Disconnecting " + getPseudo());
			Account a = getAccount();
			a.setCurrentPlayed(null);
			a.getRemoteConnection().closeConnection();
			a.setState(AccountState.OFFLINE);
			LOGGER.success(getPseudo() + " disconnected. | " + reason);
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
				+ ", botInfos=" + botInfos + ", mapInfos=" + mapInfos + ", fightInfos=" + fightInfos + ", statsInfos=" + statsInfos + ", debugView=" + debugView + ", pvpInfos=" + pvpInfos
				+ ", server=" + server + ", inventory=" + inventory + ", group=" + group + "]";
	}

}
