package fr.aresrpg.eratz.domain.event;

import fr.aresrpg.commons.domain.event.Event;
import fr.aresrpg.commons.domain.event.EventBus;
import fr.aresrpg.eratz.domain.data.player.BotPerso;

/**
 * 
 * @since
 */
public class PersoEndHarvestEvent implements Event<PersoEndHarvestEvent> {

	private static final EventBus<PersoEndHarvestEvent> BUS = new EventBus<>(PersoEndHarvestEvent.class);
	private BotPerso perso;
	private int cell;

	/**
	 * @param perso
	 * @param cell
	 */
	public PersoEndHarvestEvent(BotPerso perso, int cell) {
		this.perso = perso;
		this.cell = cell;
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
	 * @return the cell
	 */
	public int getCell() {
		return cell;
	}

	/**
	 * @param cell
	 *            the cell to set
	 */
	public void setCell(int cell) {
		this.cell = cell;
	}

	@Override
	public EventBus<PersoEndHarvestEvent> getBus() {
		return BUS;
	}

	@Override
	public boolean isAsynchronous() {
		return false;
	}

}
