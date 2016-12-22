package fr.aresrpg.eratz.domain.std.friend;

import fr.aresrpg.dofus.structures.Friend;

import java.util.List;

/**
 * 
 * @since
 */
public interface FriendServerHandler {

	void onOfflineFriends(List<String> offlines);

	void onOnlineFriends(List<Friend> friends);

}
