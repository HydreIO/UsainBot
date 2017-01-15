package fr.aresrpg.eratz.domain.command;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class BucheronCommand implements Command {

	CompletableFuture<?> harvest;

	@Override
	public String getCmd() {
		return "bucheron";
	}

	void setHarvest(CompletableFuture<?> harvest) {
		this.harvest = harvest;
	}

	@Override
	public void trigger(String[] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase("stop")) {
			if (harvest != null) {
				harvest.cancel(true);
				LOGGER.debug("Harvesting stoped !");
			}
			return;
		}
		if (args.length == 2) {
			Perso pers = Accounts.getPersoWithPseudo(args[1], Server.valueOf(args[0].toUpperCase()));
			if (pers == null) {
				LOGGER.info("Player not found");
				return;
			}
			BotPerso bdp = BotFather.getPerso(pers);
			HarvestZone zone = Paths.BONTA.getHarvestPath(bdp);
			Executors.FIXED.execute(() -> {
				try {
					setHarvest(harvest(bdp, zone));
				} catch (Exception e) {
					LOGGER.error(e, "not handled");
				}
			});
			return;
		}
		LOGGER.error("Usage: bucheron <server> <perso>");
	}

	private CompletableFuture<?> harvest(BotPerso perso, HarvestZone zone) {
		zone.sort();
		Threads.uSleep(1, TimeUnit.SECONDS);
		return perso.getMind().harvest(zone.getRessources())
				.thenApply(h -> MapsManager.getMap(zone.getNextMap()))
				.thenCompose(perso.getMind()::moveToMap)
				.handle((v, e) -> {
					if (e != null) LOGGER.severe(e);
					return v;
				}).thenCompose(c -> harvest(perso, zone));
	}

}
