package fr.aresrpg.eratz.domain.protocol;

import fr.aresrpg.eratz.domain.player.DofusAccount;

/**
 * 
 * @since
 */
public interface PacketParser {

	void parse(DofusAccount proxy, String packet);

}
