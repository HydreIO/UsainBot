package fr.aresrpg.eratz.domain.util.exception;

/**
 * 
 * @since
 */
public class NotInFightException extends RuntimeException {

	public NotInFightException(String cause) {
		super(cause + " | Not in a fight !");
	}
}
