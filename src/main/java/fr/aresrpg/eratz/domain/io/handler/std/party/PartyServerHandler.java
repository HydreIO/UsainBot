package fr.aresrpg.eratz.domain.io.handler.std.party;

import fr.aresrpg.dofus.structures.PartyErrorReason;
import fr.aresrpg.dofus.structures.character.PartyMember;

/**
 * 
 * @since
 */
public interface PartyServerHandler {

	void onPlayerAccept();

	void onPlayerRefuse();

	void onInvitePlayerInGroup(String playerSender, String playerTarger);

	void onInviteFail(PartyErrorReason reason);

	void onGroupLeaderUpdate(int leaderId);

	void onJoinGroupOk();

	void onJoinGroupError(PartyErrorReason reason);

	void onPlayerLeaveGroup(String player);

	void onFollow(String followed);

	void onStopFollow();

	void onPartyMemberUpdate(PartyMember member);

}
