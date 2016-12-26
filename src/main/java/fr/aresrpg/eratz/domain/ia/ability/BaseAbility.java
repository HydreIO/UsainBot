package fr.aresrpg.eratz.domain.ia.ability;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.dofus.protocol.exchange.client.ExchangeMoveItemsPacket.MovedItem;
import fr.aresrpg.dofus.structures.*;
import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.eratz.domain.data.dofus.item.DofusItems;
import fr.aresrpg.eratz.domain.data.dofus.item.DofusItems2;
import fr.aresrpg.eratz.domain.data.dofus.map.*;
import fr.aresrpg.eratz.domain.data.dofus.player.Smiley;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.object.Road;
import fr.aresrpg.eratz.domain.ia.Roads;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbilityState.InvitationState;
import fr.aresrpg.eratz.domain.util.BotThread;
import fr.aresrpg.eratz.domain.util.Closeable;
import fr.aresrpg.eratz.domain.util.exception.ZaapException;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public interface BaseAbility extends Closeable {

	void sit(boolean sit); // s'assoir

	BotThread getBotThread();

	/**
	 * Parle au npc
	 * 
	 * @param npcname
	 * @return
	 */
	void speakToNpc(int npcid);

	void followGroupMember(int player);

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

	void addFriend(String name);

	void removeFriend(String name);

	void getFriendList();

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
	 * Utilise un zaap
	 * 
	 * @param cellid
	 *            l'id de la cellule ou aller pour prendre le zaap
	 * @param destination
	 *            le zaap de destination
	 * @throws ZaapException
	 *             si le bot n'a pas assez de kamas ou si la destination est
	 *             inconnue
	 */
	void useZaap(Zaap current, Zaap destination) throws ZaapException;

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

	void moveItem(MovedItem items);

	void moveKama(int amount);

	/**
	 * Utilise un item
	 * 
	 * @param itemuid
	 * @return true si l'item à été utilisé
	 */
	boolean useItem(long itemuid);

	default boolean useItemWithType(int itemType) {
		Item itemByType = getPerso().getInventory().getItemByType(itemType);
		if (itemByType == null) return false;
		return useItem(itemByType.getUid());
	}

	default boolean useItem(DofusItems item) {
		return useItemWithType(item.getId());
	}

	default boolean useItem(DofusItems2 item) {
		return useItemWithType(item.getId());
	}

	void interract(Skills s, int cell);

	void dialogLeave();

	void exchangeLeave();

	void confirmExchange();

	void speak(Chat canal, String msg); // Impl note: si msg trop long split en plusieurs msg;

	void sendPm(String playername, String msg); // Impl note: si msg trop long split en plusieurs msg;

	void equip(EquipmentPosition pos, int itemId); // equip un item

	void dismantle(EquipmentPosition pos); // déséquiper

	/**
	 * Invite un joueur dans un groupe et attend une réponse
	 * 
	 * @param pname
	 *            nom du joueur
	 * @return true si le joueur a accepté l'invitation
	 */
	InvitationState invitPlayerToGroup(String pname);

	InvitationState invitPlayerToGroupAndCancel(String name, long cancelAfter, TimeUnit unit);

	void acceptGroupInvitation(boolean accept);

	InvitationState defiPlayer(int id);

	InvitationState defiPlayerAndCancel(int id, long cancelAfter, TimeUnit unit);

	void acceptDefiRequest(int playerid, boolean accept);

	void echangeWith(int id); // doit être blockant jusqu'a ce qu'un packet accept ou cancel arrive

	void acceptEchangeRequest(boolean accept);

	void acceptGuildInvitation(boolean accept);

	void setItemInHotBar(int itemId, int slot); // peut etre useless je sais pas comment ça fonctionne, a suprimer si le packet use item est le meme !

	void sendSmiley(Smiley emot);

	// DEFAULT UTIL

	Perso getPerso();

	BaseAbilityState getStates();

	default void goAndOpenBank() {
		if (!isInBankMap()) {
			Road nearestRoad = Roads.nearestRoad(getPerso().getMapInfos().getMap());
			nearestRoad.takeRoad(getPerso());
			getPerso().getNavigation().moveDown(3).moveToCell(142, true);
		}
		Threads.uSleep(2, TimeUnit.SECONDS);
		speakToNpc(-2);
		npcTalkChoice(318, 259);
	}

	default boolean isInBankMap() {
		BotMap map = getPerso().getMapInfos().getMap();
		if (!map.isOnCoords(4, -16)) return false;
		Cell c = map.getDofusMap().getCell(322);
		return c.getMovement() == 0;
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
