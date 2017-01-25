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
import fr.aresrpg.tofumanchou.infra.data.ManchouPerso;

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
				perso.startHarvest(Paths.BUCHERON_FULL);
				break;
			case "fight":
				perso.startFight(Paths.FIGHT_PICHON_ASTRUB);
				break;
			case "groupfight":
				perso.getGroup().formGroup();
				perso.getGroup().setBehaviorRunning(true);
				perso.setBehavior(perso.getGroup().startFight(Paths.FIGHT_COCHON_DE_LAIT.getFightPath(perso), true));
				break;
			case "stop":
				perso.stopBehavior();
				break;
			case "speak":
				BotConfig.AUTO_SPEAK = !BotConfig.AUTO_SPEAK;
				String msg = "Autospeak " + (BotConfig.AUTO_SPEAK ? "Enabled" : "Disabled");
				LOGGER.success(msg);
				BotFather.broadcast(Chat.MEETIC, msg);
				break;
			case "whoami":
				LOGGER.debug("Ndc = " + perso.getPerso().getAccount().getAccountName());
				LOGGER.debug("Id = " + perso.getPerso().getUUID());
				LOGGER.debug("Pseudo = " + perso.getPerso().getPseudo());
				LOGGER.debug("Lvl = " + perso.getPerso().getLevel());
				LOGGER.debug("Energie = " + perso.getPerso().getEnergy());
				LOGGER.debug("Pods libre = " + (perso.getPerso().getMaxPods() - perso.getPerso().getPods()));
				LOGGER.debug("Vie = " + perso.getPerso().getLife());
				LOGGER.debug("Xp restant = " + (perso.getPerso().getXpMax() - perso.getPerso().getXp()));
				LOGGER.debug("pos = " + perso.getPerso().getMap().getCoordsInfos());
				LOGGER.debug("cell = " + perso.getPerso().getMap().getCells()[perso.getPerso().getCellId()]);
				LOGGER.debug("Fight = " + !perso.getPerso().getMap().isEnded());
				LOGGER.debug("Inv = " + ((ManchouPerso) perso.getPerso()).getInventory().showContent());
				LOGGER.debug("Bank = " + ((ManchouPerso) perso.getPerso()).getAccount().getBank().showContent());
				LOGGER.debug("Outdoor = " + perso.getPerso().getMap().isOutdoor());
				LOGGER.debug("Map Width = " + perso.getPerso().getMap().getWidth());
				LOGGER.debug("Map Height = " + perso.getPerso().getMap().getHeight());
				LOGGER.debug("Jobs = " + perso.getPerso().getJobs());
				LOGGER.debug("Job = " + perso.getPerso().getJob());
				break;
			case "clever":
				String response = perso.getChatUtilities().getResponse(e.getCmd().substring(args[0].length() + 1));
				BotFather.broadcast(Chat.ADMIN, "Cleverbot: " + response);
				break;
			case "autofight":
				BotConfig.FIGHT_ENABLED = !BotConfig.FIGHT_ENABLED;
				String msgf = "AutoFight " + (BotConfig.FIGHT_ENABLED ? "Enabled" : "Disabled");
				LOGGER.success(msgf);
				BotFather.broadcast(Chat.MEETIC, msgf);
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
