package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.std.chat.ChatServerHandler;

/**
 * 
 * @since
 */
public class BotChatServerHandler extends BotHandlerAbstract implements ChatServerHandler {

	/**
	 * @param perso
	 */
	public BotChatServerHandler(Perso perso) {
		super(perso);
	}

	@Override
	public void onSubscribeChannel(Chat[] added) {
		// TODO

	}

	@Override
	public void onUnsubscribe(Chat[] removed) {
		// TODO

	}

	@Override
	public void onMsg(Chat chat, int player, String pseudo, String msg) {
		// TODO

	}

}
