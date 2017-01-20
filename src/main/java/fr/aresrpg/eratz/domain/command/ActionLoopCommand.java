package fr.aresrpg.eratz.domain.command;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.tofumanchou.domain.Accounts;
import fr.aresrpg.tofumanchou.domain.command.Command;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class ActionLoopCommand implements Command {

	@Override
	public String getCmd() {
		return "action";
	}

	@Override
	public void trigger(String[] args) {
		if (args.length != 0) {
			switch (args[0]) {
				case "koin-buy":
					if (args.length < 4) break;
					Perso persot = Accounts.getPersoWithPseudo(args[1], Server.valueOf(args[2].toUpperCase()));
					int loop = Integer.parseInt(args[3]);
					if (persot == null) {
						LOGGER.info("Player not found");
						return;
					}
					BotPerso botp = BotFather.getPerso(persot);
					for (int i = 0; i < loop; i++) {
						botp.getPerso().speakToNpc(-1);
						botp.getPerso().npcTalkChoice(1661, 1305);
						botp.getPerso().leaveDialog();
						Threads.uSleep(200, TimeUnit.MILLISECONDS);
					}
					return;
				case "koin-sell":
					if (args.length < 4) break;
					Perso perso = Accounts.getPersoWithPseudo(args[1], Server.valueOf(args[2].toUpperCase()));
					int loo = Integer.parseInt(args[3]);
					if (perso == null) {
						LOGGER.info("Player not found");
						return;
					}
					BotPerso bp = BotFather.getPerso(perso);
					for (int i = 0; i < loo; i++) {
						bp.getPerso().speakToNpc(-1);
						bp.getPerso().npcTalkChoice(1661, 1334);
						bp.getPerso().npcTalkChoice(1698, 1335);
						bp.getPerso().leaveDialog();
						Threads.uSleep(200, TimeUnit.MILLISECONDS);
					}
					return;
				default:
					return;
			}
		}
		LOGGER.error("Usage: action <koin-buy | koin-sell> <pseudo> <server> <loop>");
	}

}
