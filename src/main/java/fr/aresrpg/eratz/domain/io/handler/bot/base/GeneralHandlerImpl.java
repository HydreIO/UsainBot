package fr.aresrpg.eratz.domain.io.handler.bot.base;

import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.Player;

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
		getPerso().getBaseAbility().acceptEchangeRequest(getPerso().getCurrentBehavior().acceptEchange(p));
	}

	@Override
	public void onDefiRequest(Player p) {
		getPerso().getBaseAbility().acceptDefiRequest(getPerso().getCurrentBehavior().acceptDefi(p));
	}

	@Override
	public void onGroupInvitRequest(String name) {
		getPerso().getBaseAbility().acceptGroupInvitation(getPerso().getCurrentBehavior().acceptGroup(name));
	}

	@Override
	public void onGuildInvitRequest(String name) {
		getPerso().getBaseAbility().acceptGuildInvitation(getPerso().getCurrentBehavior().acceptGuilde(name));
	}

}
