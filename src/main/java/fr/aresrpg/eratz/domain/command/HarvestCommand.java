package fr.aresrpg.eratz.domain.command;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.path.Paths;
import fr.aresrpg.tofumanchou.domain.Accounts;
import fr.aresrpg.tofumanchou.domain.command.Command;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;

import java.util.concurrent.CompletableFuture;

/**
 * 
 * @since
 */
public class HarvestCommand implements Command {

	CompletableFuture<?> harvest;
	boolean stop;

	@Override
	public String getCmd() {
		return "harvest";
	}

	void setHarvest(CompletableFuture<?> harvest) {
		this.harvest = harvest;
	}

	@Override
	public void trigger(String[] args) {
		if (args.length == 3) {
			Perso pers = Accounts.getPersoWithPseudo(args[2], Server.valueOf(args[1].toUpperCase()));
			if (pers == null) {
				LOGGER.info("Player not found");
				return;
			}
			BotPerso bdp = BotFather.getPerso(pers);
			if (args[0].equalsIgnoreCase("start")) bdp.startHarvest(Paths.FULL);
			else if (args[0].equalsIgnoreCase("stop")) bdp.stopBehavior();
			else throw new IllegalArgumentException(args[0] + " is invalid");
			return;
		}
		LOGGER.error("Usage: bucheron <start/stop> <server> <perso>");
	}

}
