package fr.aresrpg.eratz.domain.std.spell;

import fr.aresrpg.dofus.structures.Spell;

import java.util.Collection;

/**
 * 
 * @since
 */
public interface SpellServerHandler {

	void onSpellChangeOption(boolean canUse);

	void onSpellList(Collection<Spell> spells);

}