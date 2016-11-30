package fr.aresrpg.eratz.domain.ability;

import fr.aresrpg.dofus.structures.character.Item;
import fr.aresrpg.eratz.domain.dofus.map.*;
import fr.aresrpg.eratz.domain.dofus.player.*;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.player.Player;
import fr.aresrpg.eratz.domain.util.exception.ZaapException;

/**
 * 
 * @since
 */
public interface BaseAbility {

	// AWAITING PACKETS

	/**
	 * Utilise l'item de l'inventaire rapide correspondant au slot
	 * 
	 * @param slot
	 *            le slot
	 * @return true si l'item à été utilisé, false si le bot est a court de cet item
	 */
	boolean useItem(int slot);

	BaseAbility setItemInHotBar(int itemId, int slot);

	BaseAbility sit(boolean sit);

	BaseAbility speak(Channel canal, String msg);

	BaseAbility sendPm(String playername, String msg);

	BaseAbility sendEmot(Emot emot);

	/**
	 * Utilise un zaap
	 * 
	 * @param cellid
	 *            l'id de la cellule ou aller pour prendre le zaap
	 * @param destination
	 *            le zaap de destination
	 * @throws ZaapException
	 *             si le bot n'a pas assez de kamas ou si la destination est inconnue
	 * @return l'ability
	 */
	BaseAbility useZaap(Zaap current, Zaap destination) throws ZaapException;

	/**
	 * Utilise un zaapi
	 * 
	 * @param current
	 *            zaapi actuel
	 * @param destination
	 *            destination
	 * @return true si le bot a pu se déplacer, false si il n'a pas les kamas
	 */
	boolean useZaapi(Zaapi current, Zaapi destination);

	BaseAbility freePod(); // impl note: liberer les pod en detruisant les ressources en trop (faudra faire des predicates canDestroy(ressource) pour pas faire de connerie)

	BaseAbility equip(int itemId);

	BaseAbility dismantle(int slot); // déséquiper

	BaseAbility echangeWith(Player p);

	BaseAbility speakToNpc(String npcname);

	BaseAbility npcTalkChoice(int choice);

	Item[] depositInventoryInChest(InventoryType inv);

	// DEFAULT UTIL

	Perso getPerso();

	default boolean goToZaap(Zaap zaap) { // si astrub prendre popo, sinon prendre popo + prendre zaap
		if (!useItem(BotPopo.RAPPEL.getSlot())) {
			getPerso().crashReport("n'a pas pu accéder au Zaap " + zaap.name() + " | Popo rappel épuisée.");
			return false;
		}
		if (zaap == Zaap.ASTRUB) return true; // par default la popo rapel renvoi zaap astrub
		try {
			useZaap(Zaap.ASTRUB, zaap);
		} catch (ZaapException e) {
			getPerso().crashReport(e.getMessage());
			return false;
		}
		return true;
	}

	default boolean goToZaapi(Zaapi zaapi) { // go ville puis go zaapi
		if (!goToCity(zaapi.getCity())) {
			getPerso().crashReport("n'a pas pu accéder au Zaapi " + zaapi.name() + " | Impossible d'aller dans la ville");
			return false;
		}
		Zaapi current = zaapi.getCity() == City.BONTA ? Zaapi.BONTA_MILICE : zaapi.BRAKMAR_MILICE;
		if (!useZaapi(current, zaapi)) {
			getPerso().crashReport("n'a pas pu accéder au Zaapi " + zaapi.name() + " | Kamas insufisant !");
			return false;
		}
		return true;
	}

	default boolean goToCity(City city) { // prendre popo
		if (!useItem(BotPopo.byCity(city).getSlot())) {
			getPerso().crashReport("n'a pas pu accéder à la ville " + city + " | Popo épuisée.");
			return false;
		}
		return true;
	}
}
