package fr.aresrpg.eratz.domain.command;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.path.Paths;
import fr.aresrpg.eratz.domain.ia.path.zone.HarvestZone;
import fr.aresrpg.tofumanchou.domain.Accounts;
import fr.aresrpg.tofumanchou.domain.command.Command;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

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
		if (args.length == 2) {
			Perso pers = Accounts.getPersoWithPseudo(args[1], Server.valueOf(args[0].toUpperCase()));
			if (pers == null) {
				LOGGER.info("Player not found");
				return;
			}
			BotPerso bdp = BotFather.getPerso(pers);
			Executors.FIXED.execute(() -> {
				HarvestZone zone = Paths.AMAKNA.getHarvestPath(bdp);
				for (;;)
					bdp.getMind().harvest(zone.getRessources()).thenApply(h -> MapsManager.getMap(zone.getNextMap())).thenAccept(bdp.getMind()::moveToMap);
			});
		}
		LOGGER.error("Usage: bucheron <server> <perso>");
	}

}
