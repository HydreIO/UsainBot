package fr.aresrpg.eratz.domain.data;

import fr.aresrpg.eratz.domain.ia.behavior.harvest.type.Path;
import fr.aresrpg.eratz.domain.ia.behavior.harvest.type.bucheron.BucheronAmaknaPath;

/**
 * 
 * @since
 */
public enum Paths {

	BUCHERON_AMAKNA(new BucheronAmaknaPath()),;

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
