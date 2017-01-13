package fr.aresrpg.eratz.domain.ia.harvest;

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

	private final Path path;
	private final Queue<Integer> allMaps = new LinkedList<>();

	/**
	 * @param perso
	 */
	public HarvestRunner(BotPerso perso, Paths path) {
		super(perso);
		this.path = path.getPath();
		refill();
	}

	void refill() {
		this.path.fillMaps(allMaps);
	}

	@Override
	public void shutdown() {

	}

	public void runHarvest(Harvesting harvesting, CompletableFuture<Harvesting> promise) {
		getPerso().getMind().publishState(MindState.HARVEST, interrupt -> {
			switch (interrupt) {
				case FIGHT_JOIN:
					break;
				case MOVED:
				case RESSOURCE_STEAL:
				case RESSOURCE_HARVESTED:
					Executors.FIXED.execute(() -> runHarvest(harvesting, promise));
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
		if (allMaps.isEmpty()) refill();
		if (!harvesting.harvest()) getPerso().getMind().moveToMap(MapsManager.getMap(allMaps.poll()));
	}

}
