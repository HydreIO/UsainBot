package fr.aresrpg.eratz.domain.ia.ability;

/**
 * 
 * @since
 */
public class BaseAbilityState {

	public InvitationState partyInvit;
	public InvitationState defiInvit;
	public String currentInvited;
	public int currentDefied;

	public static enum InvitationState {
		ACCEPTED,
		REFUSED,
		AWAITING
	}
}
