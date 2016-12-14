package fr.aresrpg.eratz.domain.io.handler.bot.map;

import fr.aresrpg.dofus.protocol.game.movement.*;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.structures.map.Frame;
import fr.aresrpg.eratz.domain.data.dofus.fight.Fight;

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

	void onFightSpawn(Fight fight);

	void onFightEnd(Fight fight);

}
