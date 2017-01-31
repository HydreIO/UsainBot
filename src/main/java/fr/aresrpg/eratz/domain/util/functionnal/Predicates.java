package fr.aresrpg.eratz.domain.util.functionnal;

import java.util.function.Predicate;

/**
 * 
 * @since
 */
public class Predicates {

	public static Predicate<?> all(Predicate... predicates) {
		return o -> {
			for (Predicate p : predicates)
				if (!p.test(o)) return false;
			return true;
		};
	}

}
