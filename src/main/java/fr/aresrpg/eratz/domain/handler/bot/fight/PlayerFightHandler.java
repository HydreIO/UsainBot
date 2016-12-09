package fr.aresrpg.eratz.domain.handler.bot.fight;

import fr.aresrpg.eratz.domain.dofus.fight.*;
import fr.aresrpg.eratz.domain.dofus.player.Spells;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.player.Player;

import java.util.Optional;

/**
 * 
 * @since
 */
public class PlayerFightHandler implements FightHandler {

	private Perso perso;

	public PlayerFightHandler(Perso perso) {
		this.perso = perso;
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	@Override
	public void onTurnBegin() {
		
	}

	@Override
	public void onTurnEnd() {
		// TODO

	}

	@Override
	public void onPlayerJoinFight(Player p) {
		// TODO

	}

	@Override
	public void onPlayerJoinSpectate(Player p) {
		// TODO

	}

	@Override
	public void onFightEvent(FightEvent event) {
		// TODO

	}

	@Override
	public void onPlayerKicked(Player p) {
		// TODO

	}

	@Override
	public void onFightEnd(FightEndReason reason) {
		// TODO

	}

	@Override
	public void onHit(Spells spell, int dgt, Target... target) {
		// TODO

	}

	@Override
	public void onMiss(MissReason reason, Optional<Target> target) {
		// TODO

	}

	@Override
	public void onPlayerMove(int newCellId, int lastCellId, Player player) {
		// TODO

	}

	@Override
	public void onEntityBecameInvisible(Target entity) {
		// TODO

	}

	@Override
	public void onEntityBecameImmune(Target entity) {
		// TODO

	}

}
