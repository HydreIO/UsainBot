package fr.aresrpg.eratz.domain.behavior.fight;

import fr.aresrpg.eratz.domain.behavior.Behavior;
import fr.aresrpg.eratz.domain.dofus.mob.Mob;
import fr.aresrpg.eratz.domain.dofus.player.Spell;
import fr.aresrpg.eratz.domain.player.Player;

import java.util.Set;

/**
 * 
 * @since
 */
public interface FightBehavior extends Behavior {

	boolean canLaunchSpell(Spell s);
	
	boolean canEndTurnHere(int cellid);
	
	int getTryToReachCellId(); // dans le cas ou canEndTurnHere = false (par exemple pour le chall hardi ou il faut coller un  mob)
	
	Mob getTarget();
	
	Spell getSpellNeededForChallenge();
	
	Set<Spell> getBuffSpells();
	
	Set<Spell> getOffensiveSpells();
	
	boolean canAttackTarget(Spell spell,Mob target);
	
	boolean canUseMagicalSpell();
	
	boolean canUseWeapon();
	
	boolean canUseTrapAndGlyphes();
	
	boolean needToKillAnEnnemiBeforeHisTurn();
	
	boolean needToKillAtLeastOneMob();
	
	boolean canTakeDamage();
	
	boolean needToKillAnAlly(Player p);
	
}
