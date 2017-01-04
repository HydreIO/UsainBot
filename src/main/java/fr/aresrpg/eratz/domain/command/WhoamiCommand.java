package fr.aresrpg.eratz.domain.command;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.util.Predicates;
import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.tofumanchou.domain.Accounts;
import fr.aresrpg.tofumanchou.domain.command.Command;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;
import fr.aresrpg.tofumanchou.infra.data.ManchouPerso;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public class WhoamiCommand implements Command {

	@Override
	public String getCmd() {
		return "whoami";
	}

	@Override
	public void trigger(String[] args) {
		System.out.println(Arrays.toString(args));
		if (args.length != 2) {
			LOGGER.error("whoami <server> <playerName>");
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
		LOGGER.debug("Ndc = " + p.getAccount().getAccountName());
		LOGGER.debug("Id = " + p.getUUID());
		LOGGER.debug("Pseudo = " + p.getPseudo());
		LOGGER.debug("Lvl = " + p.getLevel());
		LOGGER.debug("Energie = " + p.getEnergy());
		LOGGER.debug("Pods libre = " + (p.getMaxPods() - p.getPods()));
		LOGGER.debug("Vie = " + p.getLife());
		LOGGER.debug("Xp restant = " + (p.getXpMax() - p.getXp()));
		LOGGER.debug("pos = " + p.getMap().getCoordsInfos());
		LOGGER.debug("cell = " + p.getMap().getCells()[p.getCellId()]);
		LOGGER.debug("Fight = " + !p.getMap().isEnded());
		LOGGER.debug("Inv = " + ((ManchouPerso) p).getInventory().showContent());
		LOGGER.debug("Bank = " + ((ManchouPerso) p).getAccount().getBank().showContent());
		LOGGER.debug("Outdoor = " + p.getMap().isOutdoor());
		LOGGER.debug("Map Width = " + p.getMap().getWidth());
		LOGGER.debug("Map Height = " + p.getMap().getHeight());
		LOGGER.debug("Jobs = " + p.getJobs());
		LOGGER.debug("Job = " + p.getJob());
		((ManchouPerso) p).getTeleporters(Predicates.alwaysFalse());
		LOGGER.debug("All tp = " + Arrays.stream(((ManchouPerso) p).getAllTeleporters())
				.map(i -> String.valueOf(i.getId() + ",mov=" + i.getMovement() + ", 1n=" + i.getLayerObject1Num() + ", 2n=" + i.getLayerObject2Num())).collect(Collectors.joining(" | ")));
	}

}
