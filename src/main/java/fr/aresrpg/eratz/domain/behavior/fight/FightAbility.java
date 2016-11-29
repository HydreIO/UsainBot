package fr.aresrpg.eratz.domain.behavior.fight;

import fr.aresrpg.eratz.domain.dofus.player.Spell;

/**
 * 
 * @since
 */
public interface FightAbility {

	void launchSpell(Spell spell, int cellid);

	void move(int... cellIds); // array pour choisir exactement le chemin a suivre

}
