package fr.aresrpg.eratz.domain.io.handler.std.game;

import fr.aresrpg.dofus.protocol.game.movement.*;
import fr.aresrpg.dofus.protocol.game.server.GameEndPacket;
import fr.aresrpg.dofus.structures.game.*;
import fr.aresrpg.eratz.domain.data.dofus.map.BotMap;
import fr.aresrpg.eratz.domain.data.dofus.ressource.Interractable;

/**
 * 
 * @since
 */
public interface GameServerHandler {

	void onEffect(Effect eff, int... entities);

	void onFightEnd(GameEndPacket pkt);

	void onFightChallenge(FightChallenge chall);

	void onFightJoin(int state, FightType fightType, boolean isSpectator, int startTimer, boolean cancelButton, boolean isDuel);

	void onMap(BotMap map);

	void onInterractRespawn(Interractable type, int cellid);

	void onEntityLeave(int id);

	void onPlayerMove(MovementPlayer player);

	void onInvocMove(MovementInvocation invoc);

	void onMobMove(MovementMonster mob);

	void onNpcMove(MovementNpc npc);

	void onMobGroupMove(MovementMonsterGroup mobs);

	void onGameReady(boolean ready, String playerid);
}
