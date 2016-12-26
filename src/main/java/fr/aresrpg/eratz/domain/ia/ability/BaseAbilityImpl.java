package fr.aresrpg.eratz.domain.ia.ability;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.util.exception.NotImplementedException;
import fr.aresrpg.dofus.protocol.basic.client.BasicChatMessageSendPacket;
import fr.aresrpg.dofus.protocol.basic.client.BasicUseSmileyPacket;
import fr.aresrpg.dofus.protocol.dialog.DialogLeavePacket;
import fr.aresrpg.dofus.protocol.dialog.client.DialogCreatePacket;
import fr.aresrpg.dofus.protocol.dialog.client.DialogResponsePacket;
import fr.aresrpg.dofus.protocol.emote.client.EmoteUsePacket;
import fr.aresrpg.dofus.protocol.exchange.ExchangeLeavePacket;
import fr.aresrpg.dofus.protocol.exchange.client.*;
import fr.aresrpg.dofus.protocol.exchange.client.ExchangeMoveItemsPacket.MovedItem;
import fr.aresrpg.dofus.protocol.friend.client.*;
import fr.aresrpg.dofus.protocol.game.actions.GameAction;
import fr.aresrpg.dofus.protocol.game.actions.GameActions;
import fr.aresrpg.dofus.protocol.game.actions.client.*;
import fr.aresrpg.dofus.protocol.game.client.GameClientActionPacket;
import fr.aresrpg.dofus.protocol.item.client.ItemMovementPacket;
import fr.aresrpg.dofus.protocol.item.client.ItemUsePacket;
import fr.aresrpg.dofus.protocol.party.PartyRefusePacket;
import fr.aresrpg.dofus.protocol.party.client.*;
import fr.aresrpg.dofus.protocol.waypoint.client.ZaapUsePacket;
import fr.aresrpg.dofus.structures.*;
import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.eratz.domain.data.dofus.map.Zaap;
import fr.aresrpg.eratz.domain.data.dofus.map.Zaapi;
import fr.aresrpg.eratz.domain.data.dofus.player.Smiley;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbilityState.InvitationState;
import fr.aresrpg.eratz.domain.util.BotThread;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;
import fr.aresrpg.eratz.domain.util.exception.ZaapException;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class BaseAbilityImpl implements BaseAbility {

	private Perso perso;
	private BotThread botThread = new BotThread();
	private BaseAbilityState states = new BaseAbilityState();

	public BaseAbilityImpl(Perso perso) {
		this.perso = perso;
	}

	@Override
	public void shutdown() {
		states.currentToCrash = 0;
		states.currentToDefie = 0;
		states.currentToExchange = 0;
		states.currentToInvite = null;
		states.defiInvit = null;
		states.partyInvit = null;
		botThread = new BotThread();
	}

	@Override
	public void sit(boolean sit) {
		if (getPerso().getBotInfos().isSit() != sit) {
			getPerso().sendPacketToServer(new EmoteUsePacket().setEmot(Emotes.SIT));
			getPerso().getBotInfos().setSit(sit);
		}
	}

	@Override
	public void speakToNpc(int npcid) {
		DialogCreatePacket pkt = new DialogCreatePacket();
		pkt.setNpcId(npcid);
		getPerso().sendPacketToServer(pkt);
		getBotThread().pause(Thread.currentThread());
	}

	@Override
	public Perso getPerso() {
		return this.perso;
	}

	/**
	 * @return the botThread
	 */
	public BotThread getBotThread() {
		return botThread;
	}

	@Override
	public void buyToNpc(int npcid) {
		ExchangeRequestPacket pkt = new ExchangeRequestPacket(Exchange.NPC_SHOP, npcid);
		getPerso().sendPacketToServer(pkt);
		getBotThread().pause(Thread.currentThread());
	}

	@Override
	public void npcTalkChoice(int questionId, int responseId) {
		getPerso().sendPacketToServer(new DialogResponsePacket(questionId, responseId));
		getBotThread().pause();
	}

	@Override
	public BuyResult npcBuyChoice(int itemId, int quantity) {
		ExchangeBuyToNpcPacket pkt = new ExchangeBuyToNpcPacket(itemId, quantity);
		getPerso().sendPacketToServer(pkt);
		getBotThread().pause();
		return getStates().buyResult;
	}

	@Override
	public void useZaap(Zaap current, Zaap destination) throws ZaapException {
		GameInteractionAction action = new GameInteractionAction(current.getCellId(), Skills.UTILISER);
		getPerso().sendPacketToServer(new GameClientActionPacket(GameActions.INTERRACT, action));
		getBotThread().pause();
		ZaapUsePacket pkt = new ZaapUsePacket();
		pkt.setWaypointId(destination.getMapId());
		getPerso().sendPacketToServer(pkt);
		getBotThread().pause();
	}

	@Override
	public boolean useZaapi(Zaapi current, Zaapi destination) {
		throw new NotImplementedException();
	}

	@Override
	public void moveItem(MovedItem item) {
		Map<Long, Item> inv = getPerso().getInventory().getContents();
		if (item.getType() == ExchangeMove.ADD) {
			if (!inv.containsKey(item.getItemUid())) throw new IllegalArgumentException("Can't move item " + item + " | The inventory of the player doesn't contain it");
			Item mov = inv.get(item.getItemUid());
			if (item.getAmount() > mov.getQuantity()) throw new IllegalArgumentException("Can't move item " + item + " | There are not as many item in the inventory ! (" + mov.getQuantity() + ")");
			if (item.getAmount() < 0) throw new IllegalArgumentException("Unable to move " + item.getAmount() + " of " + item);
			if (item.getAmount() == mov.getQuantity()) inv.remove(item.getItemUid());
			else mov.setQuantity(mov.getQuantity() - item.getAmount());
		}
		ExchangeMoveItemsPacket pkt = new ExchangeMoveItemsPacket();
		Set<MovedItem> it = new HashSet<>();
		it.add(item);
		pkt.setItems(it);
		getPerso().sendPacketToServer(pkt);
	}

	@Override
	public void moveKama(int amount) {
		getPerso().getInventory().removeKamas(amount);
		ExchangeMoveKamasPacket pkt = new ExchangeMoveKamasPacket(amount);
		getPerso().sendPacketToServer(pkt);
		getBotThread().pause();
	}

	@Override
	public boolean useItem(long itemuid) {
		ItemUsePacket pkt = new ItemUsePacket();
		pkt.setItemId(itemuid);
		getPerso().sendPacketToServer(pkt);
		getBotThread().pause();
		return getStates().itemUsed;
	}

	@Override
	public void dialogLeave() {
		DialogLeavePacket pkt = new DialogLeavePacket();
		getPerso().sendPacketToServer(pkt);
		getBotThread().pause();
	}

	@Override
	public void exchangeLeave() {
		ExchangeLeavePacket pkt = new ExchangeLeavePacket();
		getPerso().sendPacketToServer(pkt);
		getBotThread().pause();
	}

	@Override
	public void confirmExchange() {
		ExchangeSendReadyPacket pkt = new ExchangeSendReadyPacket();
		getPerso().sendPacketToServer(pkt);
		getBotThread().pause();
	}

	@Override
	public void interract(Skills s, int cell) {
		GameInteractionAction action = new GameInteractionAction(cell, s);
		getPerso().sendPacketToServer(new GameClientActionPacket(GameActions.INTERRACT, action));
		getBotThread().pause();
	}

	@Override
	public void speak(Chat canal, String msg) {
		BasicChatMessageSendPacket pkt = new BasicChatMessageSendPacket();
		pkt.setChat(canal);
		pkt.setMsg(msg);
		getPerso().sendPacketToServer(pkt);
	}

	@Override
	public void sendPm(String playername, String msg) {
		BasicChatMessageSendPacket pkt = new BasicChatMessageSendPacket();
		pkt.setDest(playername);
		pkt.setMsg(msg);
		getPerso().sendPacketToServer(pkt);
	}

	@Override
	public void equip(EquipmentPosition pos, int itemId) {
		ItemMovementPacket pkt = new ItemMovementPacket();
		pkt.setItemid(itemId);
		pkt.setPosition(pos.getPosition());
		pkt.setQuantity(1);
		getPerso().sendPacketToServer(pkt);
		getBotThread().pause();
	}

	@Override
	public void dismantle(EquipmentPosition pos) {
		ItemMovementPacket pkt = new ItemMovementPacket();
		pkt.setItemid(getPerso().getInventory().getItemAtPos(pos).getUid());
		pkt.setPosition(EquipmentPosition.NO_EQUIPED.getPosition());
		pkt.setQuantity(1);
		getPerso().sendPacketToServer(pkt);
		getBotThread().pause();
	}

	@Override
	public void acceptGroupInvitation(boolean accept) {
		if (accept) getPerso().sendPacketToServer(new PartyAcceptPacket());
		else getPerso().sendPacketToServer(new PartyRefusePacket());
	}

	@Override
	public void acceptDefiRequest(int player, boolean accept) {
		GameAction a = accept ? new GameAcceptDuelAction(player) : new GameRefuseDuelAction(player);
		GameActions ac = accept ? GameActions.ACCEPT_DUEL : GameActions.REFUSE_DUEL;
		getPerso().sendPacketToServer(new GameClientActionPacket(ac, a));
	}

	@Override
	public void echangeWith(int id) {
		ExchangeRequestPacket pkt = new ExchangeRequestPacket(Exchange.EXCHANGE, id);
		getPerso().sendPacketToServer(pkt);
		getBotThread().pause();
	}

	@Override
	public void acceptEchangeRequest(boolean accept) {
		getPerso().sendPacketToServer(new ExchangeAcceptPacket());
	}

	@Override
	public void acceptGuildInvitation(boolean accept) {
		throw new NotImplementedException();

	}

	@Override
	public void setItemInHotBar(int itemId, int slot) {
		throw new NotImplementedException();
	}

	@Override
	public void sendSmiley(Smiley emot) {
		BasicUseSmileyPacket pkt = new BasicUseSmileyPacket();
		pkt.setSmileyId(emot.getId());
		getPerso().sendPacketToServer(pkt);
	}

	@Override
	public InvitationState invitPlayerToGroupAndCancel(String name, long cancelAfter, TimeUnit unit) {
		PartyInvitePacket pkt = new PartyInvitePacket();
		pkt.setPname(name);
		getStates().currentToInvite = name;
		getPerso().sendPacketToServer(pkt);
		getPerso().getAbilities().getBaseAbility().getStates().partyInvit = InvitationState.AWAITING;
		if (cancelAfter != 0) {
			Executors.SCHEDULED.schedule(getBotThread()::unpause, cancelAfter, unit);
			getBotThread().pause(Thread.currentThread());
		}
		InvitationState partyInvit = getStates().partyInvit;
		if (partyInvit == InvitationState.AWAITING) {
			getPerso().sendPacketToServer(new PartyRefusePacket());
			getStates().partyInvit = InvitationState.REFUSED;
		}
		return partyInvit;
	}

	@Override
	public InvitationState invitPlayerToGroup(String pname) {
		PartyInvitePacket pkt = new PartyInvitePacket();
		pkt.setPname(pname);
		getStates().currentToInvite = pname;
		getPerso().sendPacketToServer(pkt);
		getPerso().getAbilities().getBaseAbility().getStates().partyInvit = InvitationState.AWAITING;
		getBotThread().pause();
		return getStates().partyInvit;
	}

	@Override
	public void followGroupMember(int player) {
		PartyFollowPacket pkt = new PartyFollowPacket();
		pkt.setFollow(true);
		pkt.setPlayerId(player);
		getPerso().sendPacketToServer(pkt);
		getBotThread().pause();
	}

	@Override
	public InvitationState defiPlayer(int id) {
		GameDuelAction action = new GameDuelAction(id);
		getPerso().getAbilities().getBaseAbility().getStates().currentToDefie = id;
		GameClientActionPacket ga = new GameClientActionPacket(GameActions.DUEL, action);
		getPerso().sendPacketToServer(ga);
		while (getPerso().getAbilities().getBaseAbility().getStates().defiInvit == null)
			Threads.uSleep(50, TimeUnit.MILLISECONDS); // gentil cpu ! pas cramer !

		getBotThread().pause(Thread.currentThread());
		return getStates().defiInvit;
	}

	@Override
	public InvitationState defiPlayerAndCancel(int id, long cancelAfter, TimeUnit unit) {
		GameDuelAction action = new GameDuelAction(id);
		getPerso().getAbilities().getBaseAbility().getStates().currentToDefie = id;
		GameClientActionPacket ga = new GameClientActionPacket(GameActions.DUEL, action);
		getPerso().sendPacketToServer(ga);
		while (getPerso().getAbilities().getBaseAbility().getStates().defiInvit == null)
			Threads.uSleep(50, TimeUnit.MILLISECONDS); // gentil cpu ! pas cramer !

		if (cancelAfter != 0) {
			Executors.SCHEDULED.schedule(getBotThread()::unpause, cancelAfter, unit);
			getBotThread().pause(Thread.currentThread());
		}
		InvitationState duelInvit = getPerso().getAbilities().getBaseAbility().getStates().defiInvit;
		if (duelInvit == InvitationState.AWAITING) {
			GameRefuseDuelAction actionr = new GameRefuseDuelAction();
			actionr.setTargetId(getPerso().getId());
			getPerso().sendPacketToServer(new GameClientActionPacket(GameActions.REFUSE_DUEL, actionr));
		}
		return getStates().defiInvit;
	}

	@Override
	public BaseAbilityState getStates() {
		return this.states;
	}

	@Override
	public void addFriend(String name) {
		FriendAddPacket pkt = new FriendAddPacket();
		pkt.setName(name);
		getPerso().sendPacketToServer(pkt);
	}

	@Override
	public void removeFriend(String name) {
		FriendRemovePacket pkt = new FriendRemovePacket();
		pkt.setName(name);
		getPerso().sendPacketToServer(pkt);
	}

	@Override
	public void getFriendList() {
		getPerso().sendPacketToServer(new FriendGetListPacket());
	}

}
