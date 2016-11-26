package fr.aresrpg.eratz.domain.behavior.move;

import fr.aresrpg.eratz.domain.dofus.map.Map;

/**
 * 
 * @since
 */
public interface MapHandler {

	void onJoinMap(Map m);

	void onQuitMap(Map m);

}
