package fr.aresrpg.eratz.domain.behavior;

/**
 * 
 * @since
 */
public interface Behavior {

	int getPriority();

	boolean isActive();

	void setActive(boolean active);

}
