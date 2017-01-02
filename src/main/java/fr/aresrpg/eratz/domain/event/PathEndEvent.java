package fr.aresrpg.eratz.domain.event;

import fr.aresrpg.commons.domain.event.Event;
import fr.aresrpg.commons.domain.event.EventBus;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.state.BotState;

/**
 * 
 * @since
 */
public class PathEndEvent implements Event<PathEndEvent> {

	private static final EventBus<PathEndEvent> BUS = new EventBus<>(PathEndEvent.class);
	private BotPerso perso;
	private BotState state;

	/**
	 * @param perso
	 * @param state
	 */
	public PathEndEvent(BotPerso perso, BotState state) {
		this.perso = perso;
		this.state = state;
	}

	/**
	 * @return the perso
	 */
	public BotPerso getPerso() {
		return perso;
	}

	/**
	 * @param perso
	 *            the perso to set
	 */
	public void setPerso(BotPerso perso) {
		this.perso = perso;
	}

	/**
	 * @return the state
	 */
	public BotState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(BotState state) {
		this.state = state;
	}

	@Override
	public EventBus<PathEndEvent> getBus() {
		return BUS;
	}

	@Override
	public boolean isAsynchronous() {
		return false;
	}

}
