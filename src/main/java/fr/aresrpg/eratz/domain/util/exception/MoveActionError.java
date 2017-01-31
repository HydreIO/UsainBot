package fr.aresrpg.eratz.domain.util.exception;

/**
 * 
 * @since
 */
public class MoveActionError extends RuntimeException {

	public MoveActionError() {
		super("Move action error");
	}

	public static boolean same(Throwable t) {
		return t.getClass() == HarvestActionError.class;
	}
}
