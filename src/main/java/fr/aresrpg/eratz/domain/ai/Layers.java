package fr.aresrpg.eratz.domain.ai;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.event.EventBus;
import fr.aresrpg.commons.domain.event.Subscriber;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.event.LayerCancellingEvent;
import fr.aresrpg.eratz.domain.ia.path.Paths;
import fr.aresrpg.tofumanchou.domain.event.ActionErrorEvent;

import java.util.concurrent.CompletableFuture;

/**
 * 
 * @since
 */
public class Layers extends Info implements Layer {

	public static final EventBus<ActionErrorEvent> FAILING = EventBus.getBus(ActionErrorEvent.class);
	public static final EventBus<LayerCancellingEvent> CANCELLING = EventBus.getBus(LayerCancellingEvent.class);
	protected final ActionLayer actionLayer;
	protected final BasicLayer basicLayer;
	protected final BehaviorLayer behaviorLayer;

	protected CompletableFuture actions;

	/**
	 * @param perso
	 */
	public Layers(BotPerso perso) {
		super(perso);
		this.actionLayer = new ActionLayer(perso);
		this.basicLayer = new BasicLayer(perso);
		this.behaviorLayer = new BehaviorLayer(perso);
	}

	public void stopActions() {
		if (actions != null) {
			LOGGER.debug("Actions cancelled !");
			actions.cancel(true);
		}
		new LayerCancellingEvent(getPerso()).send();
	}

	public void harvest(Paths path) {
		setActions(down().harvestZone(path.getHarvestPath(getPerso())));
	}

	protected static Subscriber<LayerCancellingEvent> cancelling(BotPerso perso, Throwable error, CompletableFuture<?> promise) {
		return CANCELLING.subscribe(event -> {
			LOGGER.warning("CANCELLING !");
			if (event.getPerso() != perso || promise.isDone()) return;
			LOGGER.warning("OBTRUDE");
			promise.obtrudeException(error);
		});
	}

	protected static Subscriber<ActionErrorEvent> failing(BotPerso perso, Throwable error, CompletableFuture<?> promise) {
		return FAILING.subscribe(event -> {
			BotPerso p = BotFather.getPerso(event.getClient());
			LOGGER.warning("FAILING !");
			if (p != perso || promise.isDone()) return;
			LOGGER.warning("OBTRUDE");
			promise.obtrudeException(error);
		});
	}

	/**
	 * @param actions
	 *            the actions to set
	 */
	public void setActions(CompletableFuture actions) {
		this.actions = actions;
	}

	/**
	 * @return the actions
	 */
	public CompletableFuture getActions() {
		return actions;
	}

	@Override
	public void shutdown() {

	}

	@Override
	public Layer up() {
		throw new IllegalAccessError("There is no");
	}

	@Override
	public BehaviorLayer down() {
		return behaviorLayer;
	}

}
