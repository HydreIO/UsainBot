package fr.aresrpg.eratz.domain.io.handler.impl.bot.handler;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.dofus.structures.PartyErrorReason;
import fr.aresrpg.dofus.structures.character.PartyMember;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbilityState.InvitationState;
import fr.aresrpg.eratz.domain.ia.behavior.antibot.DuelCrashBehavior;
import fr.aresrpg.eratz.domain.io.handler.std.party.PartyServerHandler;

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
	public void onPlayerAccept() {
		LOGGER.success(getPerso().getAbilities().getBaseAbility().getStates().currentInvited + " has joined the group '" + getPerso().getGroup().getLabel() + "' !");
		getPerso().getAbilities().getBaseAbility().getStates().partyInvit = InvitationState.ACCEPTED;
		getPerso().getAbilities().getBaseAbility().getStates().currentInvited = null;
		if (getPerso().getCurrentBehavior() != null && getPerso().getCurrentBehavior() instanceof DuelCrashBehavior) {
			DuelCrashBehavior ba = (DuelCrashBehavior) getPerso().getCurrentBehavior();
			ba.stop();
		}
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onPlayerRefuse() {
		LOGGER.info(getPerso().getAbilities().getBaseAbility().getStates().currentInvited + " refused to join the group '" + getPerso().getGroup().getLabel() + "' !");
		getPerso().getAbilities().getBaseAbility().getStates().partyInvit = InvitationState.REFUSED;
		getPerso().getAbilities().getBaseAbility().getStates().currentInvited = null;
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onInvitePlayerInGroup(String playerSender, String playerTarger) {

	}

	@Override
	public void onInviteFail(PartyErrorReason reason) {
		switch (reason) {
			case ALREADY_IN_GROUP:
				LOGGER.info(getPerso().getAbilities().getBaseAbility().getStates().currentInvited + " is already in a group !");
				break;
			case FULL:
				LOGGER.info("The group '" + getPerso().getGroup().getLabel() + "' is full !");
				break;
			case UNKNOW_PLAYER:
				if (getPerso().getCurrentBehavior() != null && getPerso().getCurrentBehavior() instanceof DuelCrashBehavior) {
					DuelCrashBehavior ba = (DuelCrashBehavior) getPerso().getCurrentBehavior();
					ba.stop();
					LOGGER.success(getPerso().getAbilities().getBaseAbility().getStates().currentInvited + " crashed !");
				}
				break;
			default:
				break;
		}
		getPerso().getAbilities().getBaseAbility().getStates().partyInvit = InvitationState.REFUSED;
		getPerso().getAbilities().getBaseAbility().getStates().currentInvited = null;
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onGroupLeaderUpdate(int leaderId) {
		// TODO

	}

	@Override
	public void onJoinGroupOk() {
		// TODO

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
		// TODO

	}

	@Override
	public void onStopFollow() {
		// TODO

	}

	@Override
	public void onPartyMemberUpdate(PartyMember member) {
		// TODO

	}

}
