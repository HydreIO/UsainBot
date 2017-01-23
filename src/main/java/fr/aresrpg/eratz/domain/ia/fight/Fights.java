package fr.aresrpg.eratz.domain.ia.fight;

import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.fight.behavior.CraLowBehavior;
import fr.aresrpg.eratz.domain.ia.fight.behavior.FightBehavior;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * 
 * @since
 */
public class Fights {

	public static FightBehavior getBehavior(BotPerso perso, int lvl) {
		switch (perso.getPerso().getClasse()) {
			case CRA:
				if (lvl >= 90) return null;
				else return new CraLowBehavior(perso);
			case ENUTROF:
				if (lvl < 54) return null;
				else if (lvl < 100) return null;
				else return null;
			case ECAFLIP:
				return null;
			default:
				throw new NotImplementedException();
		}
	}

}
