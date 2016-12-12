package fr.aresrpg.eratz.domain.ability;

import fr.aresrpg.dofus.structures.Exchange;
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

	// J'ai besoin du packet qui me donne tout mon inventaire et a chaque fois qu'une update de l'inventaire est produite
	// pareil pour la banque car je vais handle les deux pour stocker les items sur l'objet perso !
	// il faut aussi extraire le X et Y de la map dans le packet mapdata et le stocker dans l'Objet dofus map

	void sit(boolean sit); // s'assoir

	/**
	 * Parle au npc
	 * 
	 * @param npcname
	 * @return
	 */
	void speakToNpc(int npcid);

	/**
	 * achetter vendre au npc
	 * 
	 * @param npcid
	 * @return
	 */
	void buyToNpc(int npcid);

	/**
	 * Choisit une reponse du npc auquel le bot parle
	 * 
	 * @param choice
	 * @return
	 */
	void npcTalkChoice(int questionId, int responseId);

	/**
	 * Achete un item à un pnj
	 * 
	 * @param choice
	 *            place de l'item
	 * @param quantity
	 *            quantité
	 * @return true si l'item a été achetté, false si kama insufisant // check les kamas du joueur dans perso
	 */
	BuyResult npcBuyChoice(int itemId, int quantity);

	/**
	 * Utilise un item de l'inventaire du joueur
	 * 
	 * @param itemid
	 * @return
	 */
	boolean useItemInInv(int itemid);

	/**
	 * Utilise un zaap
	 * 
	 * @param cellid
	 *            l'id de la cellule ou aller pour prendre le zaap
	 * @param destination
	 *            le zaap de destination
	 * @throws ZaapException
	 *             si le bot n'a pas assez de kamas ou si la destination est
	 *             inconnue
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

	void moveItem(int itemId, int amount, Exchange from, Exchange to);

	/**
	 * Je sais pas si c un packet different, si c'est pas different alors suprimer la methode
	 * Utilise l'item de l'inventaire rapide correspondant au slot (emplacement des sorts hors combat)
	 * 
	 * @param slot
	 *            le slot
	 * @return true si l'item à été utilisé, false si le bot est a court de
	 *         cet item
	 */
	boolean useItem(int slot);

	/**
	 * Juste une interraction je pense
	 * 
	 * @param choice
	 */
	void useCraftingMachine(int choice);

	/**
	 * Je ne sais pas cb ya d'inventaire different mais on a pu voir EB et EV il me semble donc creer plusieurs method pour chaque ou un argument
	 * close l'inventaire craft/pnj ouvert
	 */
	void closeGui();

	void speak(Channel canal, String msg); // Impl note: si msg trop long split en plusieurs msg;

	void sendPm(String playername, String msg); // Impl note: si msg trop long split en plusieurs msg;

	void equip(int itemId); // equip un item

	void dismantle(int slot); // déséquiper

	void invitPlayerToGroup(String pname);

	void acceptGroupInvitation(boolean accept);

	void acceptDefiRequest(boolean accept);

	void echangeWith(Player p); // je prend un player car il faut etre sur la meme map pour echanger

	void acceptEchangeRequest(boolean accept);

	void acceptGuildInvitation(boolean accept); // OSEF

	void setItemInHotBar(int itemId, int slot); // peut etre useless je sais pas comment ça fonctionne, a suprimer si le packet use item est le meme !

	void sendEmot(Emot emot);

	// DEFAULT UTIL

	Perso getPerso();

	default boolean goAndOpenBank() {
		if (!isInBankMap()) {
			if (!goToZaap(Zaap.ASTRUB)) {
				getPerso().crashReport("n'a pas pu accéder à la banque | Impossible d'aller au zaap Astrub.");
				return false;
			}
			getPerso().getNavigation().moveDown(3).moveToCell(142);
		}
		speakToNpc(-2);
		npcTalkChoice(318, 259);
		return true;
	}

	default boolean isInBankMap() {
		return getPerso().getCurrentMap().getX() == 4 && getPerso().getCurrentMap().getZ() == -16;
	}

	default boolean goToZaap(Zaap zaap) { // si astrub prendre popo, sinon prendre popo + prendre zaap
		if (!useItem(BotPopo.RAPPEL.getSlot())) {
			getPerso().crashReport("n'a pas pu accéder au Zaap " + zaap.name() + " | Popo rappel épuisée.");
			return false;
		}
		if (zaap == Zaap.ASTRUB)
			return true; // par default la popo rapel renvoi zaap astrub
		try {
			useZaap(Zaap.ASTRUB, zaap);
		} catch (ZaapException e) {
			getPerso().crashReport(e.getMessage());
			return false;
		}
		return true;
	}

	default boolean goToZaapi(Zaapi zaapi) { // go ville puis go zaapi
		Zaapi current = zaapi.getCity() == City.BONTA ? Zaapi.BONTA_MILICE : Zaapi.BRAKMAR_MILICE;
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

	public static enum BuyResult {
		SUCCESS("Sucess"),
		NO_KAMA("Kamas insuffisants"),
		NO_PODS("Pods insuffisants");

		private String reason;

		private BuyResult(String reason) {
			this.reason = reason;
		}

		/**
		 * @return the reason
		 */
		public String getReason() {
			return reason;
		}
	}
}
