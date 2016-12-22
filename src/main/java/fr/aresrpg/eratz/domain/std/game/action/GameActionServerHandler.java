package fr.aresrpg.eratz.domain.std.game.action;

import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction.PathFragment;
import fr.aresrpg.dofus.protocol.game.movement.MovementInvocation;
import fr.aresrpg.dofus.structures.game.JoinError;

import java.util.List;

/**
 * 
 * @since
 */
public interface GameActionServerHandler {

	void onActionStart(int charactereId);

	void onActionError();

	void onActionFinish(int ackId, int characterId);

	void onEntityLifeChange(int entityId, int life);

	void onEntityPaChange(int entityId, int pa);

	void onEntityPmChange(int entityId, int pm);

	void onEntityKilled(int entityId);

	void onEntitySummoned(MovementInvocation invoc);

	void onFightJoinError(JoinError error);

	void onEntityMove(int entityId, List<PathFragment> path);

	void onDuel(int entityId, int targetId);

	void onPlayerRefuseDuel(int entityId, int whoRefusedId);

	void onPlayerAcceptDuel(int entityId, int whoAcceptedId);

}