package fr.aresrpg.eratz.domain.ia.fight.behavior;

import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.info.Info;
import fr.aresrpg.eratz.domain.ia.fight.FightBehavior;
import fr.aresrpg.tofumanchou.domain.data.enums.Spells;
import fr.aresrpg.tofumanchou.infra.data.ManchouSpell;

/**
 * 
 * @since
 */
public class CraLowBehavior extends Info implements FightBehavior {

	private final ManchouSpell tir_eloigne;
	private final ManchouSpell fleche_magique;
	private final ManchouSpell tir_puissant;
	private final ManchouSpell fleche_recul;
	private final ManchouSpell oeuil_taupe;

	/**
	 * @param perso
	 */
	public CraLowBehavior(BotPerso perso) {
		super(perso);
		tir_eloigne = (ManchouSpell) perso.getPerso().getSpells().get(Spells.TIR_ELOIGNEE);
		fleche_magique = (ManchouSpell) perso.getPerso().getSpells().get(Spells.FLECHE_MAGIQUE);
		tir_puissant = (ManchouSpell) perso.getPerso().getSpells().get(Spells.TIR_PUISSANT);
		fleche_recul = (ManchouSpell) perso.getPerso().getSpells().get(Spells.FLECHE_DE_RECUL);
		oeuil_taupe = (ManchouSpell) perso.getPerso().getSpells().get(Spells.OEIL_DE_TAUPE);
	}

	@Override
	public void shutdown() {

	}

	@Override
	public void playTurn() {

	}

	private void decrementRelance() {
		tir_eloigne.decrementRelance();
		tir_puissant.decrementRelance();
		oeuil_taupe.decrementRelance();
	}

}
