package fr.aresrpg.eratz.domain.command;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.tofumanchou.domain.Accounts;
import fr.aresrpg.tofumanchou.domain.command.Command;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

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
			int id = Integer.parseInt(args[0]);
			Perso perso = Accounts.getPersoWithPseudo(args[1], Server.valueOf(args[2].toUpperCase()));
			if (perso == null) {
				LOGGER.info("Player not found");
				return;
			}
			BotMap map = MapsManager.getMap(id);
			if (map == null) {
				LOGGER.error("Map inconnue !");
				return;
			}
			BotPerso bp = BotFather.getPerso(perso);
			LOGGER.info("Going to " + map.getMap().getInfos());
			Executors.FIXED.execute(() -> {
				bp.getMind().moveToMap(map).whenComplete((val, ex) -> LOGGER.error(ex)).join();
				BotFather.broadcast(Chat.MEETIC, perso.getPseudo() + " est arrivé à destination ! " + map.getMap().getInfos());
			});
			return;
		}
		LOGGER.error("Usage: goto <mapid> <pseudo> <server>");
	}

}
