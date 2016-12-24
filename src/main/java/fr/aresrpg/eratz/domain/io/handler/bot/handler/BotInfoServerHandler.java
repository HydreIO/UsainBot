package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import fr.aresrpg.dofus.protocol.game.client.GameCreatePacket;
import fr.aresrpg.dofus.protocol.info.server.InfoCoordinatePacket.MovingPlayer;
import fr.aresrpg.dofus.structures.InfosMessage;
import fr.aresrpg.dofus.structures.game.GameType;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.std.info.InfoServerHandler;

import java.awt.Point;

/**
 * 
 * @since
 */
public class BotInfoServerHandler extends BotHandlerAbstract implements InfoServerHandler {

	/**
	 * @param perso
	 */
	public BotInfoServerHandler(Perso perso) {
		super(perso);
	}

	@Override
	public void onInfos(InfosMessage msg, String data) {
		switch (msg) {
			case CURRENT_ADRESS:
				getPerso().sendPacketToServer(new GameCreatePacket().setGameType(GameType.SOLO));
				return;
			default:
				return;
		}

	}

	@Override
	public void onCompass(int x, int y) {
		getPerso().getBotInfos().setFollowedCoords(new Point(x, y));
	}

	@Override
	public void onFollowedPlayerMove(MovingPlayer player) {
	}

}
