package fr.aresrpg.eratz.domain.ia.ability;

import fr.aresrpg.dofus.protocol.dialog.client.DialogCreatePacket;
import fr.aresrpg.dofus.protocol.emote.client.EmoteUsePacket;
import fr.aresrpg.dofus.protocol.party.client.PartyInvitePacket;
import fr.aresrpg.eratz.domain.data.dofus.map.Zaap;
import fr.aresrpg.eratz.domain.data.dofus.map.Zaapi;
import fr.aresrpg.eratz.domain.data.dofus.player.Channel;
import fr.aresrpg.eratz.domain.data.dofus.player.Emot;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.util.BotThread;
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
	public void sit(boolean sit) {
		if ((getPerso().getBotInfos().isSit() && !sit) || (!getPerso().getBotInfos().isSit() && sit)) getPerso().sendPacketToServer(new EmoteUsePacket().setEmoteId(1));
	}

	@Override
	public void speakToNpc(int npcid) {
		DialogCreatePacket pkt = new DialogCreatePacket();
		pkt.setNpcId(npcid);
		getPerso().sendPacketToServer(pkt);
		getBotThread().pause(Thread.currentThread());
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
	public boolean invitPlayerToGroup(String pname) {
		PartyInvitePacket pkt = new PartyInvitePacket();
		pkt.setPname(pname);
		getPerso().getAbilities().getBaseAbility().getStates().currentInvited = pname;
		getPerso().sendPacketToServer(pkt);
		getBotThread().pause(Thread.currentThread());
		return getStates().partyInvitAccepted;
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
	public Perso getPerso() {
		return this.perso;
	}

	@Override
	public boolean invitPlayerToGroupAndCancel(String name, long cancelAfter, TimeUnit unit) {
		// TODO
		return false;
	}

	@Override
	public boolean defiPlayer(int id) {
		// TODO
		return false;
	}

	@Override
	public boolean defiPlayerAndCancel(int id, long cancelAfter, TimeUnit unit) {
		// TODO
		return false;
	}

	@Override
	public BaseAbilityState getStates() {
		return this.states;
	}

}
