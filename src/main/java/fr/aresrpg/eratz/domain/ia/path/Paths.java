package fr.aresrpg.eratz.domain.ia.path;

import fr.aresrpg.commons.domain.util.exception.NotImplementedException;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.path.zone.fight.*;
import fr.aresrpg.eratz.domain.ia.path.zone.harvest.*;

/**
 * 
 * @since
 */
public enum Paths {

	BUCHERON_BOMBU_OLI,
	BUCHERON_BOMBU,
	BUCHERON_KALIPTUS,
	BUCHERON_OLIVIOLET,
	BUCHERON_BONTA,
	BUCHERON_FULL,
	PECHE_KOIN_KOIN,
	BUCHERON_AMAKNA,
	FIGHT_CHAMPS_ASTRUB,
	FIGHT_PICHON_ASTRUB,
	FIGHT_COCHON_DE_LAIT;

	public FightZone getFightPath(BotPerso perso) {
		switch (this) {
			case FIGHT_CHAMPS_ASTRUB:
				return new ChampAstrubZone(perso);
			case FIGHT_PICHON_ASTRUB:
				return new PichonZone(perso);
			case FIGHT_COCHON_DE_LAIT:
				return new CochinilloZone(perso);
			default:
				throw new NotImplementedException();
		}
	}

	public HarvestZone getHarvestPath(BotPerso perso) {
		switch (this) {
			case BUCHERON_BOMBU_OLI:
				return new BombuOlivioletZone(perso);
			case BUCHERON_BOMBU:
				return new BombuZone(perso);
			case BUCHERON_OLIVIOLET:
				return new OliZone(perso);
			case BUCHERON_AMAKNA:
				return new AmaknaZone(perso);
			case BUCHERON_BONTA:
				return new BontaZone(perso);
			case BUCHERON_FULL:
				return new FullZone(perso);
			case PECHE_KOIN_KOIN:
				return new KoinKoinZone(perso);
			case BUCHERON_KALIPTUS:
				return new KaliptusZone(perso);
			default:
				throw new NotImplementedException();
		}
	}

}
