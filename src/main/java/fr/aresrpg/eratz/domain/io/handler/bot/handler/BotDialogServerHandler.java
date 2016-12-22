package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.std.dialog.DialogServerHandler;

/**
 * 
 * @since
 */
public class BotDialogServerHandler extends BotHandlerAbstract implements DialogServerHandler {

	/**
	 * @param perso
	 */
	public BotDialogServerHandler(Perso perso) {
		super(perso);
	}

	@Override
	public void onDialogCreate(int npcId) {

	}

	@Override
	public void onQuestion(int question, int[] params, int[] responses) {
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onDialogPause() {
		// TODO

	}

	@Override
	public void onDialogLeave() {
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

}
