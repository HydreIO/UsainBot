package fr.aresrpg.eratz.domain.util.exception;

/**
 * 
 * @since
 */
public class NotOnPathException extends RuntimeException {

	public NotOnPathException() {
		super("You are not on the path");
	}

	public static boolean same(Throwable t) {
		return t.getClass() == NotOnPathException.class;
	}
}
