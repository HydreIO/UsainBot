package fr.aresrpg.eratz.domain.ia.mind;

import fr.aresrpg.eratz.domain.data.dofus.item.DofusItems;
import fr.aresrpg.eratz.domain.data.dofus.item.DofusItems2;
import fr.aresrpg.eratz.domain.data.dofus.map.Path;
import fr.aresrpg.eratz.domain.data.player.Perso;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * 
 * @since
 */
public interface Mind {

	/**
	 * Harvest a ressource
	 * 
	 * @param path
	 *            the path
	 * @param quantity
	 *            ressource amount (the bot will not harvest more than his maxpods)
	 * @return the mind for chaining
	 */
	Mind thenHarvest(Path path, int quantity);

	default Mind thenHarvest(Path path) {
		return thenHarvest(path, Integer.MAX_VALUE);
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
	Mind thenDepositToBank();

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
	 * Wait indefinitly until new actions
	 * 
	 * @return the mind for chaining
	 */
	Mind thenIdle();

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

}
