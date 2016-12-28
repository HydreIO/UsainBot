package fr.aresrpg.eratz.domain.ia.ability;

import fr.aresrpg.dofus.structures.Exchange;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbility.BuyResult;

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
	public volatile BuyResult buyResult;
	public volatile boolean itemUsed;
	public volatile boolean exchangeSuccess;
	public volatile Exchange currentInventory;

	public static enum InvitationState {
		ACCEPTED,
		REFUSED,
		AWAITING
	}
}
