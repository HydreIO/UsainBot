package fr.aresrpg.eratz.domain.command;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.Paths;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.state.BotState;
import fr.aresrpg.eratz.domain.ia.behavior.Path;
import fr.aresrpg.tofumanchou.domain.Accounts;
import fr.aresrpg.tofumanchou.domain.command.Command;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;

import java.util.Arrays;

/**
 * 
 * @since
 */
public class FightCommand implements Command {

	@Override
	public String getCmd() {
		return "fight";
	}

	@Override
	public void trigger(String[] args) {
		System.out.println(Arrays.toString(args));
		if (args.length != 2) {
			LOGGER.error("fight <server> <playerName>");
			return;
		}
		Server srv = Server.fromName(args[0]);
		if (srv == null) {
			LOGGER.error("The server '" + args[0] + "' is invalid");
			return;
		}
		Perso p = Accounts.getPersoWithPseudo(args[1], srv);
		if (p == null) {
			LOGGER.error("The perso '" + args[1] + "' is not found");
			return;
		}
		LOGGER.success("Starting fight for " + p.getPseudo());
		BotPerso perso = BotFather.getPerso(p);
		BotState st = perso.getBotState();
		perso.setCurrentPath(Paths.PICHON);
		Path path = perso.getCurrentPath().getPath();
		path.fillCoords(st.path);
		perso.goToNextMap();
	}

}
