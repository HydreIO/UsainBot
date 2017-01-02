package fr.aresrpg.eratz.domain.event;

import fr.aresrpg.commons.domain.event.Event;
import fr.aresrpg.commons.domain.event.EventBus;
import fr.aresrpg.eratz.domain.data.player.BotPerso;

/**
 * 
 * @since
 */
public class TemplateEvent implements Event<TemplateEvent> {

	private static final EventBus<TemplateEvent> BUS = new EventBus<>(TemplateEvent.class);
	private BotPerso perso;

	@Override
	public EventBus<TemplateEvent> getBus() {
		return BUS;
	}

	@Override
	public boolean isAsynchronous() {
		return false;
	}

}
