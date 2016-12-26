package fr.aresrpg.eratz.domain.event;

import fr.aresrpg.commons.domain.event.Event;
import fr.aresrpg.commons.domain.event.EventBus;

/**
 * 
 * @since
 */
public class BotMoveEvent implements Event<BotMoveEvent> {

	private static final EventBus<BotMoveEvent> BUS = new EventBus<>(BotMoveEvent.class);
	private int playerId;
	private int cellId;
	private boolean teleport;

	/**
	 * @param playerId
	 * @param cellId
	 * @param teleport
	 */
	public BotMoveEvent(int playerId, int cellId, boolean teleport) {
		this.playerId = playerId;
		this.cellId = cellId;
		this.teleport = teleport;
	}

	/**
	 * @return the teleport
	 */
	public boolean isTeleport() {
		return teleport;
	}

	/**
	 * @param teleport
	 *            the teleport to set
	 */
	public void setTeleport(boolean teleport) {
		this.teleport = teleport;
	}

	/**
	 * @return the playerId
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * @param playerId
	 *            the playerId to set
	 */
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	/**
	 * @return the cellId
	 */
	public int getCellId() {
		return cellId;
	}

	/**
	 * @param cellId
	 *            the cellId to set
	 */
	public void setCellId(int cellId) {
		this.cellId = cellId;
	}

	@Override
	public EventBus<BotMoveEvent> getBus() {
		return BUS;
	}

	@Override
	public boolean isAsynchronous() {
		return false;
	}

	@Override
	public String toString() {
		return "PlayerMoveEvent [playerId=" + playerId + ", cellId=" + cellId + ", teleport=" + teleport + "]";
	}

}
