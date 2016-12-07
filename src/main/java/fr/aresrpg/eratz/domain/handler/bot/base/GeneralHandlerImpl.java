package fr.aresrpg.eratz.domain.handler.bot.base;

import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.player.Player;

/**
 * 
 * @since
 */
public class GeneralHandlerImpl implements GeneralHandler {

	private Perso perso;

	public GeneralHandlerImpl(Perso perso) {
		this.perso = perso;
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	@Override
	public void onEchangeRequest(Player p) {

	}

	@Override
	public void onDefiRequest(Player p) {
		if (getPerso().getCurrentBehavior().acceptDefi(p)) getPerso().getBaseAbility().acceptDefiRequest(true);
		else getPerso().getBaseAbility().acceptDefiRequest(false);
	}

	@Override
	public void onGroupInvitRequest(String name) {

	}

	@Override
	public void onGuildInvitRequest(String name) {

	}

}
