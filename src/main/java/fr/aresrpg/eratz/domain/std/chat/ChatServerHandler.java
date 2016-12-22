package fr.aresrpg.eratz.domain.std.chat;

import fr.aresrpg.dofus.structures.Chat;

/**
 * 
 * @since 
 */
public interface ChatServerHandler {

	void onSubscribeChannel(Chat[] added);
	
	void onUnsubscribe(Chat[] removed);
}
