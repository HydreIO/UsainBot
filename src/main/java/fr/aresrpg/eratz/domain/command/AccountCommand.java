package fr.aresrpg.eratz.domain.command;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.gui.MapView;
import fr.aresrpg.tofumanchou.domain.Accounts;
import fr.aresrpg.tofumanchou.domain.command.Command;
import fr.aresrpg.tofumanchou.domain.data.Account;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;

import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public class AccountCommand implements Command {

	@Override
	public String getCmd() {
		return "account";
	}

	@Override
	public void trigger(String[] args) {
		if (args.length != 0) {
			switch (args[0]) {
				case "view":
					if (args.length < 3) break;
					Perso perso = Accounts.getPersoWithPseudo(args[1], Server.valueOf(args[2].toUpperCase()));
					if (perso == null) {
						LOGGER.info("Player not found");
						return;
					}
					LOGGER.info("Showing view for " + perso);
					MapView.getInstance().startView(BotFather.getPerso(perso).getView(), perso.getMap().getInfos());
					return;
				case "list":
				case "List":
				case "LIST":
					LOGGER.info(Accounts.getInstance().getAccounts().values().stream().map(Account::getAccountName).collect(Collectors.joining(", ")));
					return;
				case "connect":
				case "Connect":
				case "CONNECT":
					if (args.length < 3) break;
					Perso persoe = Accounts.getPersoWithPseudo(args[1], Server.valueOf(args[2].toUpperCase()));
					if (persoe == null) {
						LOGGER.info("Player not found");
						return;
					}
					LOGGER.info("Selecting " + persoe);
					persoe.connect();
					return;
				default:
					return;
			}
		}
		LOGGER.error("Usage: account <list | connect <pseudo> <server>>");
	}

}
