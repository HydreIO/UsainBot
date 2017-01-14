package fr.aresrpg.eratz.domain.ia.navigation;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.connection.Connector;
import fr.aresrpg.eratz.domain.util.BotConfig;
import fr.aresrpg.tofumanchou.domain.data.enums.Bank;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class NavigationRunner extends Info {

	public NavigationRunner(BotPerso perso) {
		super(perso);
	}

	@Override
	public void shutdown() {
	}

	public void runNavigation(Navigator navigator, CompletableFuture<Navigator> promise) {
		LOGGER.success("run navigation");
		getPerso().getMind().publishState(interrupt -> {
			LOGGER.debug("state move " + interrupt);
			switch (interrupt) {
				case DISCONNECT:
					Connector connector = new Connector(getPerso(), Randoms.nextBetween(BotConfig.RECONNECT_MIN, BotConfig.RECONNECT_MAX), TimeUnit.MILLISECONDS);
					CompletableFuture<Connector> conPromise = new CompletableFuture<>();
					Executors.FIXED.execute(() -> getPerso().getConRunner().runConnection(connector, conPromise));
					conPromise.thenRunAsync(() -> runNavigation(navigator, promise), Executors.FIXED);
					break;
				case FIGHT_JOIN: // TODO
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
								promise.complete(navigator); // finish
							}, Executors.FIXED));
					break;
				case MOVED:
					navigator.notifyMoved();
					if (navigator.isFinished()) {
						navigator.resetPersoPath();
						promise.complete(navigator);
					} else Executors.FIXED.execute(() -> runNavigation(navigator, promise));
				default:
					return; // avoid reset if non handled
			}
			getPerso().getMind().resetState();
		});
		if (!getPerso().getUtilities().isOnPath()) navigator.compilePath();
		navigator.runToNext();
	}

}
