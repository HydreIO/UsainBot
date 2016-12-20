package fr.aresrpg.eratz.domain.io.handler.std.game.action;

import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction.PathFragment;
import fr.aresrpg.dofus.protocol.game.movement.MovementInvocation;
import fr.aresrpg.dofus.structures.game.FightJoinError;

import java.util.List;

/**
 * 
 * @since
 */
public interface GameActionServerHandler {

	void onActionStart(int charactereId);

	void onActionFinish(int ackId, int characterId);

	void onEntityLifeChange(int entityId, int life);

	void onEntityPaChange(int entityId, int pa);

	void onEntityPmChange(int entityId, int pm);

	void onEntityKilled(int entityId);

	void onEntitySummoned(MovementInvocation invoc);

	void onFightJoinError(FightJoinError error);

	void onEntityMove(int entityId, List<PathFragment> path);

}
