package fr.aresrpg.eratz.domain.ai;

import fr.aresrpg.commons.domain.event.EventBus;
import fr.aresrpg.commons.domain.event.Subscriber;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.tofumanchou.domain.event.ActionErrorEvent;

import java.util.concurrent.CompletableFuture;

/**
 * 
 * @since
 */
public class Layers extends Info implements Layer {

	public static final EventBus<ActionErrorEvent> FAILING = EventBus.getBus(ActionErrorEvent.class);
	protected final ActionLayer actionLayer;
	protected final BasicLayer basicLayer;
	protected final BehaviorLayer behaviorLayer;

	/**
	 * @param perso
	 */
	public Layers(BotPerso perso) {
		super(perso);
		this.actionLayer = new ActionLayer(perso);
		this.basicLayer = new BasicLayer(perso);
		this.behaviorLayer = new BehaviorLayer(perso);
	}

	protected static Subscriber<ActionErrorEvent> failing(BotPerso perso, Throwable error, CompletableFuture<?> promise) {
		return FAILING.subscribe(event -> {
			BotPerso p = BotFather.getPerso(event.getClient());
			if (p != perso || promise.isDone()) return;
			promise.obtrudeException(error);
		});
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
