package fr.aresrpg.eratz.domain.ia;

/**
 * 
 * @since
 */
public enum Interrupt {

	FIGHT_JOIN,
	FIGHT_START,
	TURN_START,
	FIGHT_END,

	RESSOURCE_STEAL,
	RESSOURCE_HARVESTED,

	DEATH,
	GRAVE, // succomber

	FULL_POD,
	OVER_POD,

	MOVED,

	LOGIN_ERROR,
	SAVE,
	CLOSED,
	DISCONNECT,
	CONNECTED,
	TIMEOUT,

	ACTION_STOP,

}
