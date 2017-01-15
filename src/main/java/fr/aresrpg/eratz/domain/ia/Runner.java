package fr.aresrpg.eratz.domain.ia;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.tofumanchou.domain.data.enums.Bank;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public abstract class Runner extends Info {

	/**
	 * @param perso
	 */
	public Runner(BotPerso perso) {
		super(perso);
	}

	protected CompletableFuture<?> onFullPod() {
		getPerso().getUtilities().useRessourceBags();
		Threads.uSleep(1, TimeUnit.SECONDS);
		getPerso().getUtilities().destroyHeaviestRessource();
		Threads.uSleep(1, TimeUnit.SECONDS);
		return getPerso().getMind().moveToMap(MapsManager.getMap(Bank.BONTA.getMapId())).thenRunAsync(() -> {
			getPerso().getUtilities().openBank();
			Threads.uSleep(1, TimeUnit.SECONDS);
			getPerso().getUtilities().depositBank();
			Threads.uSleep(1, TimeUnit.SECONDS);
		}, Executors.FIXED);
	}
}
