package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import fr.aresrpg.dofus.structures.Waypoint;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.std.zaap.ZaapServerHandler;

/**
 * 
 * @since
 */
public class BotZaapServerHandler extends BotHandlerAbstract implements ZaapServerHandler {

	/**
	 * @param perso
	 */
	public BotZaapServerHandler(Perso perso) {
		super(perso);
	}

	@Override
	public void onLeaveZaap() {

	}

	@Override
	public void onDiscover(int respawnpoint, Waypoint... zaaps) {
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onZaapError() {
		// TODO

	}

}
