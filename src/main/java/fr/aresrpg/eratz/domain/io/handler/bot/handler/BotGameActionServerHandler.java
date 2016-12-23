package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction.PathFragment;
import fr.aresrpg.dofus.protocol.game.movement.MovementInvocation;
import fr.aresrpg.dofus.structures.game.JoinError;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbilityState.InvitationState;
import fr.aresrpg.eratz.domain.std.game.action.GameActionServerHandler;

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
	public void onFightJoinError(JoinError error) {
		switch (error) {
			case OCCUPED:
				LOGGER.warning("Can't fight | You are occuped !");
				break;
			case TARGET_OCCUPED:
				LOGGER.warning("Can't fight | The target '" + getPerso().getAbilities().getBaseAbility().getStates().currentToDefie + "' is occuped !");
				break;
			default:
				LOGGER.warning("Can't fight | " + error);
				break;
		}
		getPerso().getAbilities().getBaseAbility().getStates().defiInvit = InvitationState.REFUSED;
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
		getPerso().getAbilities().getFightAbility().getBotThread().unpause();
	}

	@Override
	public void onEntityMove(int entityId, List<PathFragment> path) {
		int id = path.get(path.size() - 1).getCellId();
		if (entityId == getPerso().getId())
			getPerso().getAbilities().getFightAbility().getBotThread().unpause();
		else {
			getPerso().getMapInfos().getMap().entityMove(entityId, id);
			//	getPerso().getDebugView().addMob(entityId, id);
		}
	}

	@Override
	public void onDuel(int entityId, int targetId) {
		if (entityId != getPerso().getId()) return;
		getPerso().getAbilities().getBaseAbility().getStates().partyInvit = InvitationState.AWAITING;
		getPerso().getAbilities().getBaseAbility().getStates().currentToDefie = targetId;
	}

	@Override
	public void onPlayerRefuseDuel(int entityId, int whoRefusedId) {
		LOGGER.severe(getPerso().getMapInfos().getMap().getNameOf(whoRefusedId) + " refused the duel !");
		getPerso().getAbilities().getBaseAbility().getStates().defiInvit = InvitationState.REFUSED;
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onPlayerAcceptDuel(int entityId, int whoAcceptedId) {
		LOGGER.success(getPerso().getMapInfos().getMap().getNameOf(whoAcceptedId) + " accepted the duel !");
		getPerso().getAbilities().getBaseAbility().getStates().defiInvit = InvitationState.ACCEPTED;
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onActionError() {
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onSpellLaunched(int spellId, int cell, int spellLvl) {
		getPerso().getAbilities().getFightAbility().getBotThread().unpause();
	}

}
