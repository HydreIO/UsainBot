package fr.aresrpg.eratz.domain.std.zaap;

import fr.aresrpg.dofus.structures.Waypoint;

/**
 * 
 * @since 
 */
public interface ZaapServerHandler {
	
	void onLeaveZaap();
	
	void onDiscover(int respawnpoint,Waypoint... zaaps);
	
	void onZaapError();

}
