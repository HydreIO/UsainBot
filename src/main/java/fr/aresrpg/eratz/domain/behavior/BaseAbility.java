package fr.aresrpg.eratz.domain.behavior;

import fr.aresrpg.eratz.domain.dofus.npc.Npc;
import fr.aresrpg.eratz.domain.dofus.player.Channel;
import fr.aresrpg.eratz.domain.dofus.player.Emot;
import fr.aresrpg.eratz.domain.player.Player;

/**
 * 
 * @since
 */
public interface BaseAbility {

	void regenLife(int percent);

	void sit(boolean sit);

	void speak(Channel canal, String msg);

	void sendPm(String playername, String msg);

	void sendEmot(Emot emot);

	void echangeWith(Player p);

	void echangeWith(Npc npc);

	void openBank();

	void speakToNpc(Npc npc);

}
