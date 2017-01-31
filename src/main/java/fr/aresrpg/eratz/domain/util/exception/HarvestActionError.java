package fr.aresrpg.eratz.domain.util.exception;

/**
 * 
 * @since
 */
public class HarvestActionError extends RuntimeException {

	public HarvestActionError() {
		super("Harvest action error");
	}

	public static boolean same(Throwable t) {
		return t.getClass() == HarvestActionError.class;
	}
}
