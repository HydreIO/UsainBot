package fr.aresrpg.eratz.domain.ai;

import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.path.zone.HarvestZone;
import fr.aresrpg.eratz.domain.util.functionnal.FutureHandler;

import java.util.concurrent.CompletableFuture;

/**
 * 
 * @since
 */
public class BehaviorLayer extends Info implements Layer {

	/**
	 * @param perso
	 */
	public BehaviorLayer(BotPerso perso) {
		super(perso);
	}

	public CompletableFuture<Void> harvestZone(HarvestZone zone) {
		return down().searchNextMap(zone)
				.thenComposeAsync(down()::joinMap)
				.thenRunAsync(() -> down().harvestMap(zone))
				.handle(FutureHandler.handleEx())
				.thenRunAsync(() -> harvestZone(zone));
	}

	@Override
	public void shutdown() {
	}

	@Override
	public Layer up() {
		return null;
	}

	@Override
	public ActionLayer down() {
		return getPerso().getLayers().actionLayer;
	}

}
