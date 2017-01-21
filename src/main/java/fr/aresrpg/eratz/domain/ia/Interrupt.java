package fr.aresrpg.eratz.domain.ia;

/**
 * 
 * @since
 */
public enum Interrupt {

	FIGHT_JOIN,
	FIGHT_START,
	TURN_START,
	STATS,

	RESSOURCE_STEAL,
	RESSOURCE_HARVESTED,
	RESSOURCE_SPAWN,

	GRAVE, // succomber

	FULL_POD,
	MOVED,

	LOGIN_ERROR,
	SAVE,
	CLOSED,
	DISCONNECT,

	ACTION_STOP,
	GUILD,
	EXCHANGE,
	DEFI,
	GROUP,

	;
}
