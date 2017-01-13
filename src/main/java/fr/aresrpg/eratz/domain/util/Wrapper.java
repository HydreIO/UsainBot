package fr.aresrpg.eratz.domain.util;

/**
 * 
 * @since
 */
public class Wrapper<T> {

	T wrapped;

	public Wrapper<T> wrap(T value) {
		this.wrapped = value;
		return this;
	}

	public T get() {
		return this.wrapped;
	}

}
