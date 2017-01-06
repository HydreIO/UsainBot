package fr.aresrpg.eratz.domain.ia.path;

import fr.aresrpg.eratz.domain.ia.path.fight.PichonFightPath;
import fr.aresrpg.eratz.domain.ia.path.harvest.bucheron.BucheronAmaknaPath;
import fr.aresrpg.eratz.domain.ia.path.harvest.bucheron.BucheronBombuOlivioletPath;

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
