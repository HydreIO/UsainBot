package fr.aresrpg.eratz.domain.ia.ability;

import fr.aresrpg.eratz.domain.data.dofus.map.Zaap;
import fr.aresrpg.eratz.domain.data.dofus.map.Zaapi;
import fr.aresrpg.eratz.domain.data.dofus.player.Channel;
import fr.aresrpg.eratz.domain.data.dofus.player.Emot;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.util.exception.ZaapException;

/**
 * 
 * @since
 */
public class BaseAbilityImpl implements BaseAbility {

	private Perso perso;

	public BaseAbilityImpl(Perso perso) {
		this.perso = perso;
	}

	@Override
	public void sit(boolean sit) {

	}

	@Override
	public void speakToNpc(int npcid) {
		// TODO

	}

	@Override
	public void buyToNpc(int npcid) {
		// TODO

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
		// TODO
		return false;
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
	public void invitToGuild(String pname) {
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

}
