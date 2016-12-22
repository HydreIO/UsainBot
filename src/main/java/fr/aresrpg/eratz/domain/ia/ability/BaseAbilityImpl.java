package fr.aresrpg.eratz.domain.ia.ability;

import fr.aresrpg.dofus.protocol.dialog.client.DialogCreatePacket;
import fr.aresrpg.dofus.protocol.emote.client.EmoteUsePacket;
import fr.aresrpg.dofus.protocol.friend.client.*;
import fr.aresrpg.dofus.protocol.game.actions.GameActions;
import fr.aresrpg.dofus.protocol.game.actions.client.GameDuelAction;
import fr.aresrpg.dofus.protocol.game.actions.client.GameRefuseDuelAction;
import fr.aresrpg.dofus.protocol.game.client.GameClientActionPacket;
import fr.aresrpg.dofus.protocol.party.PartyRefusePacket;
import fr.aresrpg.dofus.protocol.party.client.PartyInvitePacket;
import fr.aresrpg.dofus.structures.Emotes;
import fr.aresrpg.eratz.domain.data.dofus.map.Zaap;
import fr.aresrpg.eratz.domain.data.dofus.map.Zaapi;
import fr.aresrpg.eratz.domain.data.dofus.player.Channel;
import fr.aresrpg.eratz.domain.data.dofus.player.Emot;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbilityState.InvitationState;
import fr.aresrpg.eratz.domain.util.BotThread;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;
import fr.aresrpg.eratz.domain.util.exception.ZaapException;

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
		if ((getPerso().getBotInfos().isSit() && !sit) || (!getPerso().getBotInfos().isSit() && sit)) getPerso().sendPacketToServer(new EmoteUsePacket().setEmot(Emotes.SIT));
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

	}

	@Override
	public void npcTalkChoice(int questionId, int responseId) {
		// TODO

	}

	@Override
	public BuyResult npcBuyChoice(int itemId, int quantity) {
		// TODO
		return null;
	}

	@Override
	public BaseAbility useZaap(Zaap current, Zaap destination) throws ZaapException {
		// TODO
		return null;
	}

	@Override
	public boolean useZaapi(Zaapi current, Zaapi destination) {
		// TODO
		return false;
	}

	@Override
	public void moveItem(int itemId, int amount) {
		// TODO

	}

	@Override
	public void moveKama(int amount) {
		// TODO

	}

	@Override
	public boolean useItem(int itemuid) {
		// TODO
		return false;
	}

	@Override
	public void useCraftingMachine(int choice) {
		// TODO

	}

	@Override
	public void closeGui() {
		// TODO

	}

	@Override
	public void speak(Channel canal, String msg) {
		// TODO

	}

	@Override
	public void sendPm(String playername, String msg) {
		// TODO

	}

	@Override
	public void equip(int itemId) {
		// TODO

	}

	@Override
	public void dismantle(int slot) {
		// TODO

	}

	@Override
	public void acceptGroupInvitation(boolean accept) {
		// TODO

	}

	@Override
	public void acceptDefiRequest(boolean accept) {
		// TODO

	}

	@Override
	public void echangeWith(int id) {
		// TODO

	}

	@Override
	public void acceptEchangeRequest(boolean accept) {
		// TODO

	}

	@Override
	public void acceptGuildInvitation(boolean accept) {
		// TODO

	}

	@Override
	public void setItemInHotBar(int itemId, int slot) {
		// TODO

	}

	@Override
	public void sendEmot(Emot emot) {
		// TODO

	}

	@Override
	public InvitationState invitPlayerToGroup(String pname) {
		PartyInvitePacket pkt = new PartyInvitePacket();
		pkt.setPname(pname);
		getStates().currentToInvite = pname;
		getPerso().sendPacketToServer(pkt);
		getPerso().getAbilities().getBaseAbility().getStates().partyInvit = InvitationState.AWAITING;
		getBotThread().pause(Thread.currentThread());
		return getStates().partyInvit;
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
	public InvitationState defiPlayer(int id) {
		GameDuelAction action = new GameDuelAction(id);
		getPerso().getAbilities().getBaseAbility().getStates().currentToDefie = id;
		GameClientActionPacket ga = new GameClientActionPacket(GameActions.DUEL, action);
		getPerso().sendPacketToServer(ga);
		while (getPerso().getAbilities().getBaseAbility().getStates().defiInvit == null)
			;
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
			;
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
