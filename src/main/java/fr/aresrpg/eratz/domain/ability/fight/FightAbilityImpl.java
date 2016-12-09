package fr.aresrpg.eratz.domain.ability.fight;

import fr.aresrpg.eratz.domain.dofus.player.Spell;
import fr.aresrpg.eratz.domain.player.Perso;

/**
 * 
 * @since
 */
public class FightAbilityImpl implements FightAbility {

	private Perso perso;

	public FightAbilityImpl(Perso perso) {
		this.perso = perso;
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	@Override
	public void launchSpell(Spell spell, int cellid) {
		// TODO

	}

	@Override
	public void move(int... cellIds) {
		// TODO

	}

	@Override
	public void goToCellBeforeStart(int cellid) {
		// TODO

	}

	@Override
	public void endTurn() {
		// TODO

	}

	@Override
	public void beReady(boolean block) {
		// TODO

	}

	@Override
	public void blockSpec(boolean block) {
		// TODO

	}

	@Override
	public void blockCombat(boolean block) {
		// TODO

	}

	@Override
	public void blockToGroup(boolean block) {
		// TODO

	}

	@Override
	public int getSafeCellAwayFromMobs() {
		// TODO
		return 0;
	}

}
