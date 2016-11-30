package fr.aresrpg.eratz.domain.behavior;

import fr.aresrpg.dofus.structures.character.Item;
import fr.aresrpg.eratz.domain.dofus.map.*;
import fr.aresrpg.eratz.domain.dofus.player.*;
import fr.aresrpg.eratz.domain.player.Player;

/**
 * 
 * @since
 */
public interface BaseAbility {

	BaseAbility useItem(int slot);

	BaseAbility setItemInHotBar(int itemId, int slot);

	BaseAbility sit(boolean sit);

	BaseAbility speak(Channel canal, String msg);

	BaseAbility sendPm(String playername, String msg);

	BaseAbility sendEmot(Emot emot);

	BaseAbility goToZaap(Zaap zaap); // si astrub prendre popo, sinon prendre popo + prendre zaap

	BaseAbility goToZaapi(Zaapi zaapi);

	BaseAbility goToCity(City city);

	BaseAbility depositToBank();

	BaseAbility equip(int itemId);

	BaseAbility dismantle(int slot); // déséquiper

	BaseAbility echangeWith(Player p);

	BaseAbility speakToNpc(String npcname);

	BaseAbility npcTalkChoice(int choice);

	Item[] depositInventoryInChest(InventoryType inv);

}
