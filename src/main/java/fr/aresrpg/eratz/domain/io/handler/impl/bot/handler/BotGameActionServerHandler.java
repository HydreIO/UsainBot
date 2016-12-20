package fr.aresrpg.eratz.domain.io.handler.impl.bot.handler;

import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction.PathFragment;
import fr.aresrpg.dofus.protocol.game.movement.MovementInvocation;
import fr.aresrpg.dofus.structures.game.FightJoinError;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.io.handler.std.game.action.GameActionServerHandler;

import java.util.List;

/**
 * 
 * @since
 */
public class BotGameActionServerHandler extends BotHandlerAbstract implements GameActionServerHandler {

	/**
	 * @param perso
	 */
	public BotGameActionServerHandler(Perso perso) {
		super(perso);
	}

	@Override
	public void onActionStart(int charactereId) {
		// TODO

	}

	@Override
	public void onActionFinish(int ackId, int characterId) {
		// TODO

	}

	@Override
	public void onEntityLifeChange(int entityId, int life) {
		// TODO

	}

	@Override
	public void onEntityPaChange(int entityId, int pa) {
		// TODO

	}

	@Override
	public void onEntityPmChange(int entityId, int pm) {
		// TODO

	}

	@Override
	public void onEntityKilled(int entityId) {
		// TODO

	}

	@Override
	public void onEntitySummoned(MovementInvocation invoc) {
		// TODO

	}

	@Override
	public void onFightJoinError(FightJoinError error) {
		// TODO

	}

	@Override
	public void onEntityMove(int entityId, List<PathFragment> path) {
		int id = path.get(path.size() - 1).getCellId();
		if (entityId == getPerso().getId()) {
			getPerso().getMapInfos().setCellId(id);
			getPerso().getNavigation().notifyMovementEnd();
		} else {
			getPerso().getMapInfos().getMap().entityMove(entityId, id);
			//	getPerso().getDebugView().addMob(entityId, id);
		}
	}

}
