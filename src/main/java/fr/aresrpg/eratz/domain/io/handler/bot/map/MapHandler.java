package fr.aresrpg.eratz.domain.io.handler.bot.map;

import fr.aresrpg.dofus.protocol.game.movement.*;
import fr.aresrpg.dofus.protocol.game.server.GameEndPacket;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.structures.map.Frame;

/**
 * 
 * @since
 */
public interface MapHandler {

	void onJoinMap(DofusMap m);

	void onQuitMap(DofusMap m);

	void onFrameUpdate(int cellid, Frame frame);

	void onPlayerMove(MovementPlayer p);

	void onInvocMove(MovementInvocation i);

	void onMobMove(MovementMonster m);

	void onMobGroupMove(MovementMonsterGroup mg);

	void onNpcMove(MovementNpc npc);

	void onEntityLeave(int id);

	void onFightEnd(GameEndPacket packet);

}
