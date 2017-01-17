package fr.aresrpg.eratz.domain.util.functionnal;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import java.util.function.BiFunction;

/**
 * 
 * @since
 */
public interface FutureHandler<T, U> extends BiFunction<T, Throwable, U> {

	public static <T, U> FutureHandler<T, U> handleEx() {
		return (t, u) -> {
			if (u != null) LOGGER.error(u);
			return (U) t;
		};
	}
}
