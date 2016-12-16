package fr.aresrpg.eratz.domain.io.handler.std.game.action;

/**
 * 
 * @since 
 */
public interface GameActionServerHandler {
	
	void onActionStart(int charactereId);
	
	void onActionFinish(int ackId, int characterId);

}
