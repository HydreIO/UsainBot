package fr.aresrpg.eratz.domain.behavior.fight;

import fr.aresrpg.eratz.domain.behavior.Behavior;
import fr.aresrpg.eratz.domain.dofus.mob.Mob;
import fr.aresrpg.eratz.domain.dofus.player.Spell;
import fr.aresrpg.eratz.domain.dofus.player.Spells;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.player.Player;

import java.util.Set;

/**
 * 
 * @since
 */
public abstract class FightBehavior extends Behavior {

	/**
	 * @param perso
	 */
	public FightBehavior(Perso perso) {
		super(perso);
	}

	public abstract boolean canLaunchSpell(Spell s);

	public abstract boolean canEndTurnHere(int cellid);

	public abstract int getTryToReachCellId(); // dans le cas ou canEndTurnHere = false (par exemple pour le chall hardi ou il faut coller un mob)

	public abstract Mob getTarget();

	public abstract Mob getNearestMob();

	public abstract Spell getSpellNeededForChallenge();

	public abstract Set<Spell> getBuffSpells();

	public abstract Set<Spell> getOffensiveSpells();

	public abstract Set<Player> getAllies();

	public abstract boolean canAttackTarget(Spell spell, Mob target);

	public abstract boolean canUseMagicalSpell();

	public abstract boolean canUseWeapon();

	public abstract boolean canUseTrapAndGlyphes();

	public abstract boolean needToKillAnEnnemiBeforeHisTurn();

	public abstract boolean needToKillAtLeastOneMob();

	public abstract boolean canTakeDamage();

	public abstract boolean needToKillAnAlly(Player p);

	public abstract int getTotalMobKilled();

	public abstract int getMobKilledInTurn();

	public abstract boolean hasAlreadyLaunched(Spells s);

}
