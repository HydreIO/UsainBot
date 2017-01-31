package fr.aresrpg.eratz.domain.util.exception;

/**
 * 
 * @since
 */
public class SpellLaunchActionError extends RuntimeException {

	public SpellLaunchActionError() {
		super("Spell launch action error");
	}

	public static boolean same(Throwable t) {
		return t.getClass() == HarvestActionError.class;
	}
}
