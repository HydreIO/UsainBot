package fr.aresrpg.eratz.domain.util.exception;

/**
 * 
 * @since
 */
public class MobOnPathException extends RuntimeException {

	public MobOnPathException() {
		super("Unable to find a path | Some monsters are blocking the way");
	}

}
