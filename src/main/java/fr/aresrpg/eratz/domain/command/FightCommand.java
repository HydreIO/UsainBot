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
public class FightCommand implements Command {

	CompletableFuture<?> fight;
	boolean stop;

	@Override
	public String getCmd() {
		return "fight";
	}

	void setFight(CompletableFuture<?> fight) {
		this.fight = fight;
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
			if (args[0].equalsIgnoreCase("start")) bdp.startFight(Paths.FIGHT_COCHON_DE_LAIT);
			else if (args[0].equalsIgnoreCase("stop")) bdp.stopBehavior();
			else throw new IllegalArgumentException(args[0] + " is invalid");
			return;
		}
		LOGGER.error("Usage: fight <start/stop> <server> <perso>");
	}

}
