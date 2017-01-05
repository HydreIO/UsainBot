package fr.aresrpg.eratz.domain.command;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.Paths;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.state.BotState;
import fr.aresrpg.eratz.domain.ia.behavior.harvest.type.Path;
import fr.aresrpg.eratz.domain.listener.HarvestListener;
import fr.aresrpg.tofumanchou.domain.Accounts;
import fr.aresrpg.tofumanchou.domain.command.Command;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;

import java.util.Arrays;

/**
 * 
 * @since
 */
public class BucheronCommand implements Command {

	@Override
	public String getCmd() {
		return "bucheron";
	}

	@Override
	public void trigger(String[] args) {
		System.out.println(Arrays.toString(args));
		if (args.length != 2) {
			LOGGER.error("bucheron <server> <playerName>");
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
		LOGGER.success("Starting bucheron for " + p.getPseudo());
		BotPerso perso = BotFather.getPerso(p);
		BotState st = perso.getBotState();
		perso.setCurrentPath(Paths.BUCHERON_AMAKNA);
		Path path = perso.getCurrentPath().getPath();
		path.fillCoords(st.path);
		path.fillRessources(st.ressources);
		HarvestListener.register();
		try {
			if (!HarvestListener.getInstance().harvestRessource(perso, perso.getPerso().getMap(), perso.getBotState())) perso.goToNextMap();
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

}
