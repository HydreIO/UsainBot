package fr.aresrpg.eratz.domain.std.party;

import fr.aresrpg.dofus.structures.ExchangeMove;
import fr.aresrpg.dofus.structures.PartyErrorReason;
import fr.aresrpg.dofus.structures.character.PartyMember;

/**
 * 
 * @since
 */
public interface PartyServerHandler {

	void onPlayerRefuse();

	void onInvitePlayerInGroup(String playerSender, String playerTarger);

	void onInviteFail(PartyErrorReason reason);

	void onGroupLeaderUpdate(int leaderId);

	void onJoinGroupOk();

	void onJoinGroupError(PartyErrorReason reason);

	void onPlayerLeaveGroup(String player);

	void onFollow(String followed);

	void onStopFollow();

	void onPartyMemberUpdate(ExchangeMove move, PartyMember member);

}
