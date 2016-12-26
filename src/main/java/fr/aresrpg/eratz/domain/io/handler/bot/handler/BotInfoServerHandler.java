package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.dofus.protocol.game.client.GameCreatePacket;
import fr.aresrpg.dofus.protocol.info.server.InfoCoordinatePacket.MovingPlayer;
import fr.aresrpg.dofus.structures.InfosMessage;
import fr.aresrpg.dofus.structures.InfosMsgType;
import fr.aresrpg.dofus.structures.game.GameType;
import fr.aresrpg.eratz.domain.data.InfosData;
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
	public void onInfos(InfosMsgType type, int id, String msg) {
		LOGGER.success(InfosData.getMessage(type, id));
		if (InfosMessage.fromId(type, id) == InfosMessage.CURRENT_ADRESS) getPerso().sendPacketToServer(new GameCreatePacket().setGameType(GameType.SOLO));
	}

	@Override
	public void onCompass(int x, int y) {
		getPerso().getBotInfos().setFollowedCoords(new Point(x, y));
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onFollowedPlayerMove(MovingPlayer player) {
	}

}
