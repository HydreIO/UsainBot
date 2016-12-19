package fr.aresrpg.eratz.domain.io.handler.std.chat;

import fr.aresrpg.dofus.structures.Chat;

/**
 * 
 * @since 
 */
public interface ChatServerHandler {

	void onSubscribeChannel(Chat[] added);
	
	void onUnsubscribe(Chat[] removed);
}
