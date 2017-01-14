package fr.aresrpg.eratz.domain.ia.harvest;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.connection.Connector;
import fr.aresrpg.eratz.domain.ia.path.Paths;
import fr.aresrpg.eratz.domain.ia.path.zone.HarvestZone;
import fr.aresrpg.eratz.domain.util.BotConfig;
import fr.aresrpg.tofumanchou.domain.data.enums.Bank;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class HarvestRunner extends Info {

	private HarvestZone zone;

	/**
	 * @param perso
	 */
	public HarvestRunner(BotPerso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {

	}

	public void startHarvest(Paths path, CompletableFuture<Harvesting> promise) {
		this.zone = path.getHarvestPath(getPerso());
		runHarvest(new Harvesting(getPerso(), zone), promise);
	}

	public void runHarvest(Harvesting harvesting, CompletableFuture<Harvesting> promise) {
		LOGGER.debug("run harvest");
		getPerso().getMind().publishState(interrupt -> {
			LOGGER.debug("state " + interrupt);
			switch (interrupt) {
				case FIGHT_JOIN:
					break;
				case MOVED:
					Executors.SCHEDULED.schedule(() -> runHarvest(harvesting, promise), 1, TimeUnit.SECONDS);
					break;
				case RESSOURCE_STEAL:
				case RESSOURCE_HARVESTED:
					getPerso().getUtilities().setCurrentHarvest(-1);
					Executors.SCHEDULED.schedule(() -> runHarvest(harvesting, promise), 100, TimeUnit.MILLISECONDS);
					break;
				case OVER_POD:
				case FULL_POD:
					getPerso().getUtilities().useRessourceBags();
					Threads.uSleep(1, TimeUnit.SECONDS);
					getPerso().getUtilities().destroyHeaviestRessource();
					Threads.uSleep(1, TimeUnit.SECONDS);
					Executors.FIXED.execute(() -> getPerso().getMind().moveToMap(MapsManager.getMap(Bank.BONTA.getMapId()))
							.thenRunAsync(() -> {
								getPerso().getUtilities().openBank();
								Threads.uSleep(1, TimeUnit.SECONDS);
								getPerso().getUtilities().depositBank();
								Threads.uSleep(1, TimeUnit.SECONDS);
								runHarvest(harvesting, promise);
							}, Executors.FIXED));
					break;
				case DISCONNECT:
					Connector connector = new Connector(getPerso(), Randoms.nextBetween(BotConfig.RECONNECT_MIN, BotConfig.RECONNECT_MAX), TimeUnit.MILLISECONDS);
					CompletableFuture<Connector> conPromise = new CompletableFuture<>();
					Executors.FIXED.execute(() -> getPerso().getConRunner().runConnection(connector, conPromise));
					conPromise.thenRunAsync(() -> runHarvest(harvesting, promise), Executors.FIXED);
					break;
				default:
					return; // avoid reset if non handled
			}
			getPerso().getMind().resetState();
		});
		if (!harvesting.harvest()) {
			LOGGER.debug("map empty -> no ressource");
			Executors.FIXED.execute(() -> getPerso().getMind().moveToMap(MapsManager.getMap(zone.getNextMap())).thenRunAsync(() -> {
				Threads.uSleep(1, TimeUnit.SECONDS);
				runHarvest(harvesting, promise);
			}, Executors.FIXED));
		}
	}

}
