package fr.aresrpg.eratz.domain.mind;

import fr.aresrpg.eratz.domain.dofus.item.Items;
import fr.aresrpg.eratz.domain.player.Path;
import fr.aresrpg.eratz.domain.player.Perso;

import java.util.Set;
import java.util.concurrent.ExecutionException;
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
	 *            ressource amount
	 * @return the mind for chaining
	 */
	Mind thenHarvest(Path path, int quantity);

	/**
	 * Try to drop a ressource
	 * 
	 * @param item
	 *            the ressource
	 * @param quantity
	 *            the amount
	 * @return the mind for chaining
	 */
	Mind thenDrop(Items item, int quantity);

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
	 * @param item
	 *            the item to sell
	 * @return the mind for chaining
	 */
	Mind thenSell(Items item);

	/**
	 * Disconnect the bot a certain amount of time
	 * 
	 * @param reason
	 *            the reason
	 * @param timeToStayOffline
	 *            the time in seconds
	 * @return the mind for chaining
	 */
	default Mind thenDisconnect(String reason, int timeToStayOffline) {
		thenDisconnectIf(reason, timeToStayOffline, p -> true);
		return this;
	}

	/**
	 * Disconnect the bot
	 * 
	 * @param reason
	 *            the reason
	 * @return the mind for chaining
	 */
	default Mind thenDisconnect(String reason) {
		thenDisconnect(reason, -1);
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
	default Mind thenDisconnectIf(String reason, Predicate<Perso> condition) {
		thenDisconnectIf(reason, -1, condition);
		return this;
	}

	/**
	 * Disconnect the bot if the condition is valid
	 * 
	 * @param reason
	 *            the reason
	 * @param timeToStayOffline
	 *            the time to stay offline
	 * @param condition
	 *            the condition
	 * @return the mind for chaining
	 */
	Mind thenDisconnectIf(String reason, int timeToStayOffline, Predicate<Perso> condition);

	/**
	 * Infinite loop the actions
	 */
	void thenRestart();

	/**
	 * The mind will automatically depose items in bank when bots reach the pod limit, add items in the set to inform the bot that he need to keep these items in his inventory
	 * 
	 * @return items to keep
	 */
	Set<Items> getItemToKeep();

	/**
	 * Start actions
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	void process() throws InterruptedException, ExecutionException;

}
