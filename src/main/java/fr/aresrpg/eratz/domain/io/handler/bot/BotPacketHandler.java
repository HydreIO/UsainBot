/*******************************************************************************
 * BotFather (C) - Dofus 1.29 bot
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.io.handler.bot;

import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.io.handler.BaseServerPacketHandler;
import fr.aresrpg.eratz.domain.io.handler.bot.handler.*;

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
		addPartyHandlers(new BotPartyServerHandler(perso));
		addFriendHandlers(new BotFriendServerHandler(perso));
		addDialogHandlers(new BotDialogServerHandler(perso));
		addExchangeHandlers(new BotExchangeServerHandler(perso));
		addItemHandlers(new BotItemServerHandler(perso));
		addChatHandlers(new BotChatServerHandler(perso));
		addJobHandlers(new BotJobServerHandler(perso));
		addSpellHandlers(new BotSpellServerHandler(perso));
	}

}
