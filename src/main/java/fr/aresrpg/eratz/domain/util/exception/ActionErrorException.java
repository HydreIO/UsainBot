package fr.aresrpg.eratz.domain.util.exception;

/**
 * 
 * @since
 */
public class ActionErrorException extends RuntimeException {

	public ActionErrorException(String cause) {
		super("Action Error | " + cause);
	}
}
