package fr.aresrpg.eratz.domain.ia.path;

import fr.aresrpg.commons.domain.util.exception.NotImplementedException;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.path.zone.*;

/**
 * 
 * @since
 */
public enum Paths {

	BUCHERON_BOMBU_OLI,
	BOMBU,
	OLIVIOLET,
	AMAKNA;

	public HarvestZone getHarvestPath(BotPerso perso) {
		switch (this) {
			case BUCHERON_BOMBU_OLI:
				return new BombuOlivioletZone(perso);
			case BOMBU:
				return new BombuZone(perso);
			case OLIVIOLET:
				return new OliZone(perso);
			case AMAKNA:
				return new AmaknaZone(perso);
			default:
				throw new NotImplementedException();
		}
	}

}
