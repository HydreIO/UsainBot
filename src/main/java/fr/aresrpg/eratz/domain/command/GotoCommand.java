package fr.aresrpg.eratz.domain.command;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.listener.GotoListener;
import fr.aresrpg.tofumanchou.domain.Accounts;
import fr.aresrpg.tofumanchou.domain.command.Command;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;

import java.util.Arrays;

/**
 * 
 * @since
 */
public class GotoCommand implements Command {

	@Override
	public String getCmd() {
		return "goto";
	}

	@Override
	public void trigger(String[] args) {
		if (args.length >= 3) {
			String[] coords = args[0].split(",");
			int x = Integer.parseInt(coords[0]);
			int y = Integer.parseInt(coords[1]);
			Perso perso = Accounts.getPersoWithPseudo(args[1], Server.valueOf(args[2].toUpperCase()));
			if (perso == null) {
				LOGGER.info("Player not found");
				return;
			}
			GotoListener.unRegister();
			LOGGER.info("Going to " + Arrays.toString(coords));
			BotPerso bp = BotFather.getPerso(perso);
			bp.getBotState().addPath(x, y);
			GotoListener.register();
			bp.goToNextMap();
			return;
		}
		LOGGER.error("Usage: goto <x,y> <pseudo> <server>");
	}

}
