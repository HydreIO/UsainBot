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
import fr.aresrpg.eratz.domain.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ability.craft.CraftAbility;
import fr.aresrpg.eratz.domain.ability.fight.FightAbility;
import fr.aresrpg.eratz.domain.ability.harvest.HarvestAbility;
import fr.aresrpg.eratz.domain.ability.move.Navigation;
import fr.aresrpg.eratz.domain.behavior.Behavior;
import fr.aresrpg.eratz.domain.behavior.harvest.type.WheatHarvestBehavior;
import fr.aresrpg.eratz.domain.behavior.move.type.BankDepositPath;
import fr.aresrpg.eratz.domain.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.dofus.item.Items;
import fr.aresrpg.eratz.domain.dofus.item.Object;
import fr.aresrpg.eratz.domain.dofus.map.Map;
import fr.aresrpg.eratz.domain.dofus.player.*;
import fr.aresrpg.eratz.domain.handler.bot.BotHandler;
import fr.aresrpg.eratz.domain.option.fight.FightOptions;
import fr.aresrpg.eratz.domain.player.state.AccountState;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;
import fr.aresrpg.eratz.domain.util.config.Variables;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.time.Instant;
import java.util.*;

public class Perso extends Player {

	private Account account;
	private Map currentMap;
	private BotJob botJob;
	private Set<Player> group = new HashSet<>();
	private java.util.Map<Spells, Spell> spells = new HashMap<>();
	private Fight currentFight;
	private Behavior currentBehavior;
	private Navigation navigation;
	private BaseAbility baseAbility;
	private HarvestAbility harvestAbility;
	private CraftAbility craftAbility;
	private FightAbility fightAbaility;
	private final FightOptions fightOptions;
	private int maxPods;
	private int usedPods;

	public Perso(int id, String pseudo, Account account, BotJob job, Classe classe, Genre sexe) {
		super(id, pseudo, classe, sexe);
		this.account = account;
		this.botJob = job;
		this.fightOptions = new FightOptions(this);
		for (Spells s : Spells.values())
			if (s.getClasse() == getClasse()) spells.put(s, new Spell(s));
	}

	public Perso(int id, String pseudo, Account account, Classe classe, Genre sexe) {
		this(id, pseudo, account, null, classe, sexe);
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
			a.getBotHandler().notifyPlayerChange();
			a.setRemoteConnection(new DofusConnection<SocketChannel>(getPseudo(), channel, new BotHandler(a), Bound.SERVER));
			Executors.FIXED.execute(a::readRemote);
		} catch (IOException e) {
			a.setState(AccountState.OFFLINE);
			System.out.println("Bot crash."); // test debug
			e.printStackTrace(); // test debug
		}
		a.setLastConnection(System.currentTimeMillis());
	}

	/**
	 * @return the spells
	 */
	public java.util.Map<Spells, Spell> getSpells() {
		return spells;
	}

	/**
	 * @return the currentFight
	 */
	public Fight getCurrentFight() {
		return currentFight;
	}

	/**
	 * @param currentFight
	 *            the currentFight to set
	 */
	public void setCurrentFight(Fight currentFight) {
		this.currentFight = currentFight;
	}

	/**
	 * @return the fightOptions
	 */
	public FightOptions getFightOptions() {
		return fightOptions;
	}

	public boolean isInFight(Fight f) {
		return f == getCurrentFight();
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
		return fightAbaility;
	}

	/**
	 * @return the craftAbility
	 */
	public CraftAbility getCraftAbility() {
		return craftAbility;
	}

	public void crashReport(String msg) {
		getBaseAbility().speak(Channel.ADMIN, msg);
		disconnect();
	}

	public void harvestWheat(int quantity) {
		int harvested = 0;
		goEmptyInvInBanque(492, 8540, 577);
		while (harvested < quantity) {
			WheatHarvestBehavior harvester = new WheatHarvestBehavior(this, quantity);
			harvester.start();
			if (harvester.isQuantityHarvested()) break;
			if (harvester.isFullPod()) {
				harvested += getQuantityInInventoryOf(Items.BLE.getId());
				goEmptyInvInBanque(492, 8540, 577);
			}
		}
	}

	public boolean inventoryContainsItem(int id) {
		for (Object o : getBaseAbility().getItemInInventory())
			if (o.getTemplate().getID() == id) return true;
		return false;
	}

	public int getQuantityInInventoryOf(int itemId) {
		int item = 0;
		for (Object o : getBaseAbility().getItemInInventory())
			if (o.getTemplate().getID() == itemId) {
				item = o.getQuantity();
				break;
			}
		return item;
	}

	public int getFreePods() {
		return getMaxPods() - getUsedPods();
	}

	public void goEmptyInvInBanque(int... itemsToKeep) {
		new BankDepositPath(this, itemsToKeep).startPath();
	}

	public int getQuantityInBanqueOf(int itemId) {
		int item = 0;
		for (Object o : getBaseAbility().getObjectsInBank())
			if (o.getTemplate().getID() == itemId) {
				item = o.getQuantity();
				break;
			}
		return item;
	}

	public void disconnect() {
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
			System.out.println(getPseudo() + " disconnected.");
		}

	}

	public BotJob getBotJob() {
		return botJob;
	}

	public boolean hasBotJob() {
		return getBotJob() != null;
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

	/**
	 * @return the currentMap
	 */
	public Map getCurrentMap() {
		return currentMap;
	}

	/**
	 * Try to update the behavior and start it
	 * 
	 * @param b
	 *            the behavior
	 * @return true if the behavior was updated, false if there was already a
	 *         behavior in execution
	 */
	public boolean changeBehavior(Behavior b) {
		if (currentBehavior != null)
			return false;
		this.currentBehavior = b;
		Executors.FIXED.execute(b);
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
	 * @return the group
	 */
	public Set<Player> getGroup() {
		return group;
	}

	public boolean hasGroup() {
		return !getGroup().isEmpty();
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

}
