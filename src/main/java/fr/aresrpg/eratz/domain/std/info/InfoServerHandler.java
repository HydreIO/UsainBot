package fr.aresrpg.eratz.domain.std.info;

import fr.aresrpg.dofus.protocol.info.server.InfoCoordinatePacket.MovingPlayer;
import fr.aresrpg.dofus.structures.InfosMessage;

/**
 * 
 * @since
 */
public interface InfoServerHandler {

	void onInfos(InfosMessage msg, String data);

	void onCompass(int x, int y);

	void onFollowedPlayerMove(MovingPlayer player);

}
