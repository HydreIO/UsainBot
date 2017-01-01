package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.commons.domain.log.AnsiColors.AnsiColor;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.eratz.domain.data.AccountsManager;
import fr.aresrpg.eratz.domain.data.MindManager;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.std.chat.ChatServerHandler;
import fr.aresrpg.eratz.domain.util.Constants;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

import java.time.Instant;
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
		if (player == getPerso().getId() || !getPerso().canTalk()) return;
		switch (chat) {
			case PM_RECEIVE:
			case PRIVATE:
				if (getPerso().canTalk() && Instant.ofEpochMilli(getPerso().getChatInfos().getLastSpeak()).plusSeconds(20).isAfter(Instant.now())) {
					if (msg.contains("bot"))
						Executors.SCHEDULED.schedule(() -> getPerso().getAbilities().getBaseAbility().sendPm(pseudo, Constants.NO_BOT_SENTENCE.get(Randoms.nextInt(Constants.NO_BOT_SENTENCE.size()))),
								Randoms.nextBetween(2, 4), TimeUnit.SECONDS);
					else Executors.SCHEDULED.schedule(() -> getPerso().getAbilities().getBaseAbility().sendPm(pseudo, getPerso().getChatInfos().getResponse(msg)),
							Randoms.nextBetween(3, 8), TimeUnit.SECONDS);
				}
				break;
			default:
				break;
		}
	}

}
