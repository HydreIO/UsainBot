/*******************************************************************************
 * BotFather (C) - Dofus 1.29 bot
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.io.handler.impl.bot;

import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.io.handler.BaseServerPacketHandler;
import fr.aresrpg.eratz.domain.io.handler.impl.bot.handler.*;

/**
 * 
 * @since
 */
public class BotPacketHandler extends BaseServerPacketHandler {

	/**
	 * @param perso
	 */
	public BotPacketHandler(Perso perso) {
		super(perso);
		addAccountHandlers(new BotAccountServerHandler(perso));
		addGameActionHandlers(new BotGameActionServerHandler(perso));
		addGameHandlers(new BotGameServerHandler(perso));
		addInfoHandlers(new BotInfoServerHandler(perso));
	}

}
