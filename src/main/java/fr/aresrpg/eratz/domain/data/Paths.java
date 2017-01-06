package fr.aresrpg.eratz.domain.data;

import fr.aresrpg.eratz.domain.ia.behavior.Path;
import fr.aresrpg.eratz.domain.ia.behavior.fight.PichonFightPath;
import fr.aresrpg.eratz.domain.ia.behavior.harvest.bucheron.BucheronAmaknaPath;
import fr.aresrpg.eratz.domain.ia.behavior.harvest.bucheron.BucheronBombuOlivioletPath;

/**
 * 
 * @since
 */
public enum Paths {

	BUCHERON_AMAKNA(new BucheronAmaknaPath()),
	BUCHERON_BOMBU_OLI(new BucheronBombuOlivioletPath()),
	PICHON(new PichonFightPath()),;

	private Path path;

	/**
	 * @param path
	 */
	private Paths(Path path) {
		this.path = path;
	}

	/**
	 * @return the path
	 */
	public Path getPath() {
		return path;
	}

}
