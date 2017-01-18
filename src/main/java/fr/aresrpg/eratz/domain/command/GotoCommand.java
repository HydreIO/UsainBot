package fr.aresrpg.eratz.domain.command;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.tofumanchou.domain.Accounts;
import fr.aresrpg.tofumanchou.domain.command.Command;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;
import fr.aresrpg.tofumanchou.domain.data.enums.Bank;

import java.util.concurrent.TimeUnit;

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
		if (args.length >= 4) {
			int id = Integer.parseInt(args[1]);
			Perso perso = Accounts.getPersoWithPseudo(args[2], Server.valueOf(args[3].toUpperCase()));
			BotPerso bp = BotFather.getPerso(perso);
			if (perso == null) {
				LOGGER.info("Player not found");
				return;
			}
			switch (args[0]) {
				case "map":
					BotMap map = MapsManager.getMap(id);
					if (map == null) {
						LOGGER.error("Map inconnue !");
						return;
					}
					LOGGER.info("Going to " + map.getMap().getInfos());
					bp.getMind().moveToMap(map).thenRunAsync(() -> {
						BotFather.broadcast(Chat.ADMIN, perso.getPseudo() + " est arrivé à destination ! " + map.getMap().getInfos());
						if (id == Bank.BONTA.getMapId()) {
							bp.getUtilities().openBank();
							Threads.uSleep(1, TimeUnit.SECONDS);
							bp.getUtilities().depositBank();
						}
					});
					return;
				case "cell":
					LOGGER.info("Going to cell " + id);
					bp.getPerso().moveToCell(id, true, false);
					return;
				default:
					break;
			}
		}
		LOGGER.error("Usage: goto <map/cell> <mapid/cellid> <pseudo> <server>");
	}

}
