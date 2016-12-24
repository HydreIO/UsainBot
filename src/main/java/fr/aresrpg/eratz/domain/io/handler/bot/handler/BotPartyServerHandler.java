package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.dofus.structures.ExchangeMove;
import fr.aresrpg.dofus.structures.PartyErrorReason;
import fr.aresrpg.dofus.structures.character.PartyMember;
import fr.aresrpg.eratz.domain.antibot.behavior.GroupCrashBehavior;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbilityState.InvitationState;
import fr.aresrpg.eratz.domain.std.party.PartyServerHandler;

/**
 * 
 * @since
 */
public class BotPartyServerHandler extends BotHandlerAbstract implements PartyServerHandler {

	/**
	 * @param perso
	 */
	public BotPartyServerHandler(Perso perso) {
		super(perso);
	}

	@Override
	public void onPlayerRefuse() {
		LOGGER.info(getPerso().getAbilities().getBaseAbility().getStates().currentToInvite + " refused to join the group '" + getPerso().getGroup().getLabel() + "' !");
		getPerso().getAbilities().getBaseAbility().getStates().partyInvit = InvitationState.REFUSED;
		getPerso().getAbilities().getBaseAbility().getStates().currentToInvite = null;
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onInvitePlayerInGroup(String playerSender, String playerTarger) {

	}

	@Override
	public void onInviteFail(PartyErrorReason reason) {
		switch (reason) {
			case ALREADY_IN_GROUP:
				LOGGER.info(getPerso().getAbilities().getBaseAbility().getStates().currentToInvite + " is already in a group !");
				break;
			case FULL:
				LOGGER.info("The group '" + getPerso().getGroup().getLabel() + "' is full !");
				break;
			case UNKNOW_PLAYER:
				LOGGER.info(getPerso().getAbilities().getBaseAbility().getStates().currentToInvite + " is not online !");
				break;
			default:
				break;
		}
		getPerso().getAbilities().getBaseAbility().getStates().partyInvit = InvitationState.REFUSED;
		getPerso().getAbilities().getBaseAbility().getStates().currentToInvite = null;
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onGroupLeaderUpdate(int leaderId) {
	}

	@Override
	public void onJoinGroupOk() {
	}

	@Override
	public void onJoinGroupError(PartyErrorReason reason) {
		// TODO

	}

	@Override
	public void onPlayerLeaveGroup(String player) {
		// TODO

	}

	@Override
	public void onFollow(String followed) {
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onStopFollow() {
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onPartyMemberUpdate(ExchangeMove move, PartyMember member) {
		switch (move) {
			case ADD:
				getPerso().getAbilities().getBaseAbility().getStates().partyInvit = InvitationState.ACCEPTED;
				getPerso().getAbilities().getBaseAbility().getStates().currentToInvite = null;
				if (getPerso().getCurrentBehavior() != null && getPerso().getCurrentBehavior() instanceof GroupCrashBehavior) {
					GroupCrashBehavior ba = (GroupCrashBehavior) getPerso().getCurrentBehavior();
					ba.stop();
				}
				break;

			default:
				break;
		}
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

}
