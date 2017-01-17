package fr.aresrpg.eratz.domain.ia.fight;

import fr.aresrpg.tofumanchou.domain.data.enums.Classe;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * 
 * @since
 */
public enum Fights {

	CRA_BL(null),
	CRA_90(null),
	ENU_BL(null),
	ENU_CUPI(null),
	ENU_100(null),
	ECA_BL(null);

	private FightBehavior behavior;

	private Fights(FightBehavior behavior) {
		this.behavior = behavior;
	}

	public static FightBehavior getBehavior(Classe classe, int lvl) {
		switch (classe) {
			case CRA:
				if (lvl >= 90) return CRA_90.behavior;
				else return CRA_BL.behavior;
			case ENUTROF:
				if (lvl < 54) return ENU_BL.behavior;
				else if (lvl < 100) return ENU_CUPI.behavior;
				else return ENU_100.behavior;
			case ECAFLIP:
				return ECA_BL.behavior;
			default:
				throw new NotImplementedException();
		}
	}

}
