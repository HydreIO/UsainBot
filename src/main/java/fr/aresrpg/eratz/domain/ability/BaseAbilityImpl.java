package fr.aresrpg.eratz.domain.ability;

import fr.aresrpg.dofus.structures.item.Object;
import fr.aresrpg.eratz.domain.dofus.map.Zaap;
import fr.aresrpg.eratz.domain.dofus.map.Zaapi;
import fr.aresrpg.eratz.domain.dofus.player.Channel;
import fr.aresrpg.eratz.domain.dofus.player.Emot;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.player.Player;
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
	public boolean useItem(int slot) {
		// TODO
		return false;
	}

	@Override
	public boolean useItemInInv(int itemid) {
		// TODO
		return false;
	}

	@Override
	public void closeGui() {
		// TODO

	}

	@Override
	public void useCraftingMachine(int choice) {
		// TODO

	}

	@Override
	public BaseAbility setItemInHotBar(int itemId, int slot) {
		// TODO
		return null;
	}

	@Override
	public BaseAbility sit(boolean sit) {
		// TODO
		return null;
	}

	@Override
	public BaseAbility speak(Channel canal, String msg) {
		// TODO
		return null;
	}

	@Override
	public BaseAbility sendPm(String playername, String msg) {
		// TODO
		return null;
	}

	@Override
	public BaseAbility sendEmot(Emot emot) {
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
	public void freePod() {
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
	public void invitPlayerToGroup(String pname) {
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
	public void echangeWith(Player p) {
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
	public BaseAbility speakToNpc(int npcid) {
		// TODO
		return null;
	}

	@Override
	public BaseAbility buyToNpc(int npcid) {
		// TODO
		return null;
	}

	@Override
	public BaseAbility npcTalkChoice(int questionId, int responseId) {
		// TODO
		return null;
	}

	@Override
	public BuyResult npcBuyChoice(int itemId, int quantity) {
		// TODO
		return null;
	}

	@Override
	public void depositItemInChest(int... ids) {
		// TODO

	}

	@Override
	public String[] getItemsInBank() {
		// TODO
		return null;
	}

	@Override
	public void getItemFromBank(int itemId, int quantity) {
		// TODO

	}

	@Override
	public Object[] getItemInInventory() {
		// TODO
		return null;
	}

	@Override
	public Perso getPerso() {
		return this.perso;
	}

}
