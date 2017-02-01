package fr.aresrpg.eratz.domain.event;

import fr.aresrpg.commons.domain.event.Event;
import fr.aresrpg.commons.domain.event.EventBus;
import fr.aresrpg.eratz.domain.data.player.BotPerso;

/**
 * 
 * @since
 */
public class LayerCancellingEvent implements Event<LayerCancellingEvent> {

	private static final EventBus<LayerCancellingEvent> BUS = new EventBus<>(LayerCancellingEvent.class);
	private BotPerso perso;

	public LayerCancellingEvent(BotPerso perso) {
		this.perso = perso;
	}

	/**
	 * @return the perso
	 */
	public BotPerso getPerso() {
		return perso;
	}

	@Override
	public EventBus<LayerCancellingEvent> getBus() {
		return BUS;
	}

	@Override
	public boolean isAsynchronous() {
		return false;
	}

}
