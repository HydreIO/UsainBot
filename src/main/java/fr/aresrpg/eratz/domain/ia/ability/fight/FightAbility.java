package fr.aresrpg.eratz.domain.ia.ability.fight;

import fr.aresrpg.eratz.domain.data.player.object.Spell;

/**
 * 
 * @since
 */
public interface FightAbility {

	void launchSpell(Spell spell, int cellid);

	void move(int... cellIds); // array pour choisir exactement le chemin a suivre

	void goToCellBeforeStart(int cellid);

	void endTurn();

	void beReady(boolean block);

	void blockSpec(boolean block);

	void blockCombat(boolean block);

	void blockToGroup(boolean block);

	int getSafeCellAwayFromMobs();

}
