/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.player;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.dofus.protocol.DofusConnection;
import fr.aresrpg.dofus.protocol.ProtocolRegistry.Bound;
import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.dofus.structures.server.*;
import fr.aresrpg.dofus.util.DofusMapView;
import fr.aresrpg.eratz.domain.TheBotFather;
import fr.aresrpg.eratz.domain.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ability.craft.CraftAbility;
import fr.aresrpg.eratz.domain.ability.fight.FightAbility;
import fr.aresrpg.eratz.domain.ability.harvest.HarvestAbility;
import fr.aresrpg.eratz.domain.ability.move.Navigation;
import fr.aresrpg.eratz.domain.ability.move.NavigationImpl;
import fr.aresrpg.eratz.domain.ability.sell.SellAbility;
import fr.aresrpg.eratz.domain.behavior.Behavior;
import fr.aresrpg.eratz.domain.behavior.fight.FightBehavior;
import fr.aresrpg.eratz.domain.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.dofus.player.*;
import fr.aresrpg.eratz.domain.handler.bot.BotHandler;
import fr.aresrpg.eratz.domain.option.fight.FightInfo;
import fr.aresrpg.eratz.domain.player.state.AccountState;
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

	private final AbilityInfo abilities = new AbilityInfo(this);
	private final LogInfo logInfos = new LogInfo(this);
	private final BotInfo botInfos = new BotInfo(this);
	private final MapInfo mapInfos = new MapInfo(this);
	private final DofusMapView debugView = new DofusMapView();
	private final DofusServer server;
	private final Inventory inventory = new PlayerInventory(this);

	public Perso(int id, String pseudo, Account account, BotJob job, Classe classe, Genre sexe, Server srv) {
		this.id = id;
		this.pseudo = pseudo;
		this.account = account;
		this.botInfos.setBotJob(job);
		this.server = new DofusServer(srv.getId(), ServerState.ONLINE, 0, true);
		for (Spells s : Spells.values())
			if (s.getClasse() == getClasse()) spells.put(s, new Spell(s));
	}

	public Perso(int id, String pseudo, Account account, Classe classe, Genre sexe, Server srv) {
		this(id, pseudo, account, null, classe, sexe, srv);
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
	 * @return the sellAbility
	 */
	public SellAbility getSellAbility() {
		return sellAbility;
	}

	/**
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * @param inventory
	 *            the inventory to set
	 */
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	/**
	 * @param maxPods
	 *            the maxPods to set
	 */
	public void setMaxPods(int maxPods) {
		this.maxPods = maxPods;
	}

	/**
	 * @param usedPods
	 *            the usedPods to set
	 */
	public void setUsedPods(int usedPods) {
		this.usedPods = usedPods;
	}

	/**
	 * @return the fightInfos
	 */
	public FightInfo getFightInfos() {
		return fightInfos;
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

	public boolean allGroupIsInFight(Fight f) {
		for (Player p : getGroup())
			if (!f.hasPlayer(p)) return false;
		return true;
	}

	/**
	 * @return the fightAbaility
	 */
	public FightAbility getFightAbility() {
		return fightAbility;
	}

	/**
	 * @return the craftAbility
	 */
	public CraftAbility getCraftAbility() {
		return craftAbility;
	}

	public void crashReport(String msg) {
		getBaseAbility().speak(Channel.ADMIN, msg);
		disconnect(msg, -1);
	}

	private boolean containsObject(int id, Set<Item> set) {
		for (Item o : set)
			if (o.getUniqueId() == id) return true;
		return false;
	}

	private int quantityOf(int id, Set<Item> set) { // pr item non stackable
		return (int) set.stream().filter(i -> i.getUniqueId() == id).count();
	}

	public boolean inventoryContainsItem(int id) {
		return containsObject(id, getInventory().getContents());
	}

	public int getQuantityInInventoryOf(int itemId) {
		return quantityOf(itemId, getInventory().getContents());
	}

	public int getFreePods() {
		return getMaxPods() - getUsedPods();
	}

	public int getQuantityInBanqueOf(int itemId) {
		return quantityOf(itemId, getAccount().getBanque().getContents());
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
	 * @return the baseAbility
	 */
	public BaseAbility getBaseAbility() {
		return baseAbility;
	}

	/**
	 * @return the maxPods
	 */
	public int getMaxPods() {
		return maxPods;
	}

	/**
	 * @return the usedPods
	 */
	public int getUsedPods() {
		return usedPods;
	}

	public int getPodsPercent() {
		return getUsedPods() * 100 / getMaxPods();
	}

	public boolean changeFightBehavior(FightBehavior behavior) {
		if (currentFightBehavior != null) return false;
		this.currentFightBehavior = behavior;
		if (isInFight()) Executors.FIXED.execute(behavior);
		return true;
	}

	public void resetBehavior() {
		this.currentBehavior = null;
	}

	/**
	 * @return the currentBehavior
	 */
	public Behavior getCurrentBehavior() {
		return currentBehavior;
	}

	/**
	 * @return the harvestAbility
	 */
	public HarvestAbility getHarvestAbility() {
		return harvestAbility;
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

}
