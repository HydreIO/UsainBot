package fr.aresrpg.eratz.domain.std.info;

import fr.aresrpg.dofus.protocol.info.server.InfoCoordinatePacket.MovingPlayer;
import fr.aresrpg.dofus.structures.InfosMsgType;

/**
 * 
 * @since
 */
public interface InfoServerHandler {

	void onInfos(InfosMsgType type, int id, String msg);

	void onCompass(int x, int y);

	void onFollowedPlayerMove(MovingPlayer player);

}
