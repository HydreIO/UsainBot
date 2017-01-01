package fr.aresrpg.eratz.domain.ia.mind;

import fr.aresrpg.eratz.domain.data.dofus.map.Path;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.util.Closeable;
import fr.aresrpg.eratz.domain.util.ThreadBlocker;
import fr.aresrpg.tofumanchou.domain.data.enums.*;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * 
 * @since
 */
public interface Mind extends Closeable {

	Mind thenFollow(int toFollow);

	/**
	 * Harvest a ressource
	 * 
	 * @param path
	 *            the path
	 * @return the mind for chaining
	 */
	Mind thenHarvest(Path path);

	Mind keepItems(int... itemsType);

	default Mind keepItems(DofusItems... items) {
		return keepItems(Arrays.stream(items).mapToInt(DofusItems::getId).toArray());
	}

	default Mind keepItems(DofusItems2[] items) {
		return keepItems(Arrays.stream(items).mapToInt(DofusItems2::getId).toArray());
	}

	/**
	 * Try to drop a ressource
	 * 
	 * @param item
	 *            the ressource
	 * @param quantity
	 *            the amount
	 * @return the mind for chaining
	 */
	Mind thenDrop(int item, int quantity);

	default Mind thenDrop(int item) {
		return thenDrop(item, Integer.MAX_VALUE);
	}

	default Mind thenDrop(DofusItems item, int quantity) {
		return thenDrop(item.getId(), quantity);
	}

	default Mind thenDrop(DofusItems item) {
		return thenDrop(item.getId());
	}

	default Mind thenDrop(DofusItems2 item, int quantity) {
		return thenDrop(item.getId(), quantity);
	}

	default Mind thenDrop(DofusItems2 item) {
		return thenDrop(item.getId());
	}

	/**
	 * Try to craft an item
	 * 
	 * @param path
	 *            the path
	 * @param quantity
	 *            the amount of item
	 * @return the mind for chaining
	 */
	Mind thenCraft(Path path, int quantity);

	default Mind thenCraft(Path path) {
		return thenCraft(path, Integer.MAX_VALUE);
	}

	/**
	 * Depose all inventory in bank
	 * 
	 * @return the mind for chaining
	 */
	Mind thenDepositToBank(Bank bank);

	default Mind thenDepositToNearestBank() {
		int y = getPerso().getMapInfos().getMap().getY();
		Bank b = Bank.ASTRUB;
		if (y > 12) b = Bank.SUFOKIA;
		else if (y > -7) b = Bank.AMAKNA;
		return thenDepositToBank(b);
	}

	/**
	 * Fight mobs
	 * 
	 * @param path
	 *            the path
	 * @return the mind for chaining
	 */
	Mind thenFight(Path path);

	/**
	 * Try to feed all pets
	 * 
	 * @return the mind for chaining
	 */
	Mind thenFeedPets();

	/**
	 * Go sell items
	 * 
	 * @param path
	 *            the path
	 * @return the mind for chaining
	 */
	Mind thenSell(Path path);

	/**
	 * Disconnect the bot a certain amount of time
	 * 
	 * @param reason
	 *            the reason
	 * @param timeToStayOffline
	 *            the time in seconds
	 * @return the mind for chaining
	 */
	default Mind thenDisconnect(String reason) {
		thenDisconnectIf(reason, p -> true);
		return this;
	}

	/**
	 * Disconnect the bot if the condition is valid
	 * 
	 * @param reason
	 *            the reason
	 * @param condition
	 *            the condition
	 * @return the mind for chaining
	 */
	Mind thenDisconnectIf(String reason, Predicate<Perso> condition);

	/**
	 * Switch to the given perso
	 * 
	 * @param perso
	 *            the perso to switch
	 * @return the mind for chaining
	 */
	Mind thenConnect(Perso perso);

	/**
	 * Reconnect the perso
	 * 
	 * @return the mind for chaining
	 */
	Mind thenReconnect();

	/**
	 * Wait the given time
	 * 
	 * @param time
	 *            the time
	 * @param unit
	 *            the unit
	 * @return the mind for chaining
	 */
	Mind thenWait(long time, TimeUnit unit);

	/**
	 * Infinite loop the actions
	 */
	void thenRestart();

	/**
	 * The mind will automatically depose items in bank when bots reach the pod limit, add items in the set to inform the bot that he need to keep these items in his inventory
	 * 
	 * @return items to keep
	 */
	Set<Integer> getItemToKeep();

	/**
	 * Start actions
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	void process() throws InterruptedException, ExecutionException;

	boolean isRunning();

	ThreadBlocker getBlocker();

	Perso getPerso();
}
