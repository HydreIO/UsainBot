package fr.aresrpg.eratz.domain.util.exception;

import fr.aresrpg.eratz.domain.util.functionnal.PathContext;

/**
 * 
 * @since
 */
public class PathNotFoundException extends RuntimeException {

	public PathNotFoundException(PathContext context) {
		super("Unable to find a path | " + context.print());
	}

}
