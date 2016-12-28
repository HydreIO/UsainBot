package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import fr.aresrpg.dofus.structures.Spell;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.std.spell.SpellServerHandler;

import java.util.Collection;

/**
 * 
 * @since
 */
public class BotSpellServerHandler extends BotHandlerAbstract implements SpellServerHandler {

	/**
	 * @param perso
	 */
	public BotSpellServerHandler(Perso perso) {
		super(perso);
	}

	@Override
	public void onSpellChangeOption(boolean canUse) {

	}

	@Override
	public void onSpellList(Collection<Spell> spells) {
		
	}

}
