package fr.aresrpg.eratz.domain.ia;

/**
 * 
 * @since
 */
public enum Interrupt {

	FIGHT_JOIN(3),
	FIGHT_START(3),
	TURN_START(3),

	RESSOURCE_STEAL(4),
	RESSOURCE_HARVESTED(4),
	RESSOURCE_SPAWN(4),

	GRAVE(2), // succomber

	FULL_POD(3),
	MOVED(4),

	LOGIN_ERROR(4),
	SAVE(4),
	CLOSED(4),
	DISCONNECT(1),

	ACTION_STOP(1),
	;
	private int priority;

	private Interrupt(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

}
