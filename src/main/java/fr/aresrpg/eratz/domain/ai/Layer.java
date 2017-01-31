package fr.aresrpg.eratz.domain.ai;

/**
 * 
 * @since
 */
public interface Layer<T extends Layer> {

	T up();

	T down();

}
