package fr.aresrpg.eratz.domain.util.exception;

/**
 * 
 * @since
 */
public class MapJoinError extends RuntimeException {

	public MapJoinError() {
		super("Map join error");
	}

	public static boolean same(Throwable t) {
		return t.getClass() == MapJoinError.class;
	}
}
