package fr.aresrpg.eratz.domain.io.handler.std.info;

import fr.aresrpg.dofus.structures.InfosMessage;

/**
 * 
 * @since 
 */
public interface InfoServerHandler {
	
	void onInfos(InfosMessage msg,String data);

}
