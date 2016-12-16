/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.io.handler.std.fight;

import fr.aresrpg.eratz.domain.data.dofus.fight.*;
import fr.aresrpg.eratz.domain.data.dofus.player.Spells;
import fr.aresrpg.eratz.domain.data.player.Player;

import java.util.Optional;

/**
 * 
 * @since
 */
public interface FightHandler {

	void onTurnBegin();

	void onTurnEnd();

	void onPlayerJoinFight(Player p);

	void onPlayerJoinSpectate(Player p);

	void onFightEvent(FightEvent event);

	void onPlayerKicked(Player p);

	void onFightEnd(FightEndReason reason);

	/**
	 * when a target is hit
	 * 
	 * @param spell
	 *            the spell used or null if its a mob spell (oui car c'est mort j'écrit pas les 51548552 sorts de dofus)
	 * @param dgt
	 *            the damage or -1 if its not an attack (specify damage even if the spell is a buff which cause damage)
	 * @param target
	 *            all targets
	 */
	void onHit(Spells spell, int dgt, Target... target);

	void onMiss(MissReason reason, Optional<Target> target);

	void onPlayerMove(int newCellId, int lastCellId, Player player);

	void onEntityBecameInvisible(Target entity);

	void onEntityBecameImmune(Target entity);

}
