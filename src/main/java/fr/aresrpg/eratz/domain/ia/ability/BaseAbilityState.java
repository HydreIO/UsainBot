package fr.aresrpg.eratz.domain.ia.ability;

/**
 * 
 * @since
 */
public class BaseAbilityState {

	public volatile InvitationState partyInvit;
	public volatile InvitationState defiInvit;
	public volatile int currentToExchange;
	public volatile String currentToInvite;
	public volatile int currentToDefie;
	public volatile int currentToCrash;

	public static enum InvitationState {
		ACCEPTED,
		REFUSED,
		AWAITING
	}
}
