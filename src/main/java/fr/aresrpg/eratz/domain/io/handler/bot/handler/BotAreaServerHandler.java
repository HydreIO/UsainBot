package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import fr.aresrpg.dofus.structures.map.Subarea;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.std.area.SubareaServerHandler;

/**
 * 
 * @since 
 */
public class BotAreaServerHandler extends BotHandlerAbstract implements SubareaServerHandler{

	/**
	 * @param perso
	 */
	public BotAreaServerHandler(Perso perso) {
		super(perso);
	}

	@Override
	public void onSubareaList(Subarea... subs) {
	}

}
