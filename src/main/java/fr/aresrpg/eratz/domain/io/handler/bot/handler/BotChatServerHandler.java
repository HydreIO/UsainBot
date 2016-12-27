package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.commons.domain.log.AnsiColors.AnsiColor;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.eratz.domain.data.AccountsManager;
import fr.aresrpg.eratz.domain.data.MindManager;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.std.chat.ChatServerHandler;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

import java.util.concurrent.TimeUnit;

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
	}

	@Override
	public void onMsg(Chat chat, int player, String pseudo, String msg) {
		switch (chat) {
			case PRIVATE:
			case PM_RECEIVE:
			case PARTY:
				Perso p = AccountsManager.getInstance().getPerso(pseudo);
				if (p == null) {
					LOGGER.warning(AnsiColor.WHITE + pseudo + ": " + msg);
					break;
				}
				String[] cmd = msg.split(" ");
				switch (cmd[0]) {
					case "follow":
						Executors.FIXED.execute(() -> {
							getPerso().getGroup().formGroup();
							MindManager.getInstance().followPlayer(getPerso(), player);
						});
						break;
					case "ble":
						Executors.FIXED.execute(() -> {
							MindManager.getInstance().lvlUpPaysan(getPerso());
						});
						break;
				}
				break;

			default:
				break;
		}
		
		// cleverbot
		if (player == getPerso().getId() || !getPerso().canRespond()) return;
		switch (chat) {
			case COMMON:
				if (Randoms.nextInt(5) == 1)
					getPerso().respondTo(msg, chat);
				break;
			case PM_RECEIVE:
			case PRIVATE:
				if (Randoms.nextBool()) Executors.SCHEDULED.schedule(() -> getPerso().getAbilities().getBaseAbility().sendPm(pseudo, getPerso().getChatInfos().getResponse(msg)),
						Randoms.nextBetween(3, 8), TimeUnit.SECONDS);
				break;
			default:
				break;
		}
	}

}
