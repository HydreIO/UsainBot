package fr.aresrpg.eratz.domain.ia.harvest;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.Mind.MindState;
import fr.aresrpg.eratz.domain.ia.connection.Connector;
import fr.aresrpg.eratz.domain.ia.path.Path;
import fr.aresrpg.eratz.domain.ia.path.Paths;
import fr.aresrpg.eratz.domain.util.BotConfig;
import fr.aresrpg.tofumanchou.domain.data.enums.Bank;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class HarvestRunner extends Info {

	private Path path;
	private final Queue<Integer> allMaps = new LinkedList<>();

	/**
	 * @param perso
	 */
	public HarvestRunner(BotPerso perso) {
		super(perso);
	}

	void refill() {
		this.path.fillMaps(allMaps);
	}

	@Override
	public void shutdown() {

	}

	public void startHarvest(Paths path, CompletableFuture<Harvesting> promise) {
		this.path = path.getPath();
		refill();
		runHarvest(new Harvesting(getPerso(), path.getPath().getRessources()), promise);
	}

	public void runHarvest(Harvesting harvesting, CompletableFuture<Harvesting> promise) {
		if (allMaps.isEmpty()) refill();
		if (!harvesting.harvest()) {
			getPerso().getMind().moveToMap(MapsManager.getMap(allMaps.poll())).thenRunAsync(() -> {
				Threads.uSleep(1, TimeUnit.SECONDS);
				runHarvest(harvesting, promise);
			}, Executors.FIXED);
			return;
		}
		getPerso().getMind().publishState(MindState.HARVEST, interrupt -> {
			LOGGER.debug("state " + interrupt);
			switch (interrupt) {
				case FIGHT_JOIN:
					break;
				case MOVED:
					Executors.SCHEDULED.schedule(() -> runHarvest(harvesting, promise), 1, TimeUnit.SECONDS);
					break;
				case RESSOURCE_STEAL:
				case RESSOURCE_HARVESTED:
					Executors.SCHEDULED.schedule(() -> runHarvest(harvesting, promise), 100, TimeUnit.MILLISECONDS);
					break;
				case OVER_POD:
					getPerso().getUtilities().destroyHeaviestRessource();
				case FULL_POD:
					getPerso().getMind().moveToMap(MapsManager.getMap(Bank.BONTA.getMapId()))
							.thenRun(() -> {
								getPerso().getUtilities().openBank();
								Threads.uSleep(1, TimeUnit.SECONDS);
								getPerso().getUtilities().depositBank();
								Threads.uSleep(1, TimeUnit.SECONDS);
								promise.complete(harvesting); // finish
							});
					break;
				case DISCONNECT:
					Connector connector = new Connector(getPerso(), Randoms.nextBetween(BotConfig.RECONNECT_MIN, BotConfig.RECONNECT_MAX), TimeUnit.MILLISECONDS);
					CompletableFuture<Connector> conPromise = new CompletableFuture<>();
					getPerso().getConRunner().runConnection(connector, conPromise);
					conPromise.thenRunAsync(() -> runHarvest(harvesting, promise), Executors.FIXED);
					break;
				default:
					break;
			}
			getPerso().getMind().getStates().remove(MindState.HARVEST);
		});
	}

}
