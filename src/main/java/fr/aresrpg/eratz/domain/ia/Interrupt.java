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

	DISCONNECT,

	RESSOURCE_STEAL,
	RESSOURCE_HARVESTED,
	NO_RESSOURCES,

	DEATH,
	GRAVE, // succomber

	FULL_POD,

	OUT_OF_PATH,
	MOVED,

	BANNED,
	LOGIN_ERROR,
	SAVE,
	CLOSED,

}
