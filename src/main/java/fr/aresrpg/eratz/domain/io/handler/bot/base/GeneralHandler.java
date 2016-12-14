package fr.aresrpg.eratz.domain.io.handler.bot.base;

import fr.aresrpg.eratz.domain.data.player.Player;

/**
 * 
 * @since
 */
public interface GeneralHandler {

	void onEchangeRequest(Player p);

	void onDefiRequest(Player p);

	void onGroupInvitRequest(String name);

	void onGuildInvitRequest(String name);
	
	void onInventory();

}
