package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import fr.aresrpg.dofus.structures.Friend;
import fr.aresrpg.eratz.domain.antibot.AntiBot;
import fr.aresrpg.eratz.domain.data.dofus.player.BotJob;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.std.friend.FriendServerHandler;

import java.util.List;

/**
 * 
 * @since
 */
public class BotFriendServerHandler extends BotHandlerAbstract implements FriendServerHandler {

	/**
	 * @param perso
	 */
	public BotFriendServerHandler(Perso perso) {
		super(perso);
	}

	@Override
	public void onOfflineFriends(List<String> offlines) {

	}

	@Override
	public void onOnlineFriends(List<Friend> friends) {
		if (AntiBot.ENABLED) if (getPerso().getBotInfos().getBotJob() == BotJob.CRASHER) friends.forEach(f -> AntiBot.getInstance().notifyCrash(getPerso(), f.getName()));
	}

}
