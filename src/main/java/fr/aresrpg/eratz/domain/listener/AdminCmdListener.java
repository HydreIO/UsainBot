package fr.aresrpg.eratz.domain.listener;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.event.*;
import fr.aresrpg.commons.domain.util.Pair;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.path.Paths;
import fr.aresrpg.eratz.domain.util.BotConfig;
import fr.aresrpg.tofumanchou.domain.event.AdminCommandEvent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @since
 */
public class AdminCmdListener implements Listener {

	private static final AdminCmdListener instance = new AdminCmdListener();
	private static List<Pair<EventBus, Subscriber>> subs = new ArrayList<>();

	private AdminCmdListener() {
	}

	public static void register() {
		try {
			subs = Events.register(instance);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void unRegister() {
		subs.forEach(p -> p.getFirst().unsubscribe(p.getSecond()));
	}

	@Subscribe
	public void onCmd(AdminCommandEvent e) {
		String[] args = e.getCmd().split(" ");
		if (e.getCmd().isEmpty() || args.length == 0) return;
		BotPerso perso = BotFather.getPerso(e.getPerso());
		switch (args[0]) {
			case "bucheron":
				perso.startHarvest(Paths.FULL);
				break;
			case "stop":
				perso.stopBehavior();
				break;
			case "speak":
				BotConfig.AUTO_SPEAK = !BotConfig.AUTO_SPEAK;
				LOGGER.success("Autospeak " + (BotConfig.AUTO_SPEAK ? "Enabled" : "Disabled"));
				break;
			case "clever":
				String response = perso.getChatUtilities().getResponse(e.getCmd().substring(args[0].length() + 1));
				BotFather.broadcast(Chat.ADMIN, "Cleverbot: " + response);
				break;
			case "pktserver":
				try {
					((SocketChannel) perso.getPerso().getAccount().getConnection().getChannel()).write(ByteBuffer.wrap((e.getCmd().substring(args[0].length() + 1) + "\0\n").getBytes()));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				break;
			default:
				break;
		}
	}

}
