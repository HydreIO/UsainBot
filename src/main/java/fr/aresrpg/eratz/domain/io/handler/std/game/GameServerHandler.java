package fr.aresrpg.eratz.domain.io.handler.std.game;

import fr.aresrpg.dofus.protocol.game.movement.*;
import fr.aresrpg.dofus.protocol.game.server.GameEndPacket;
import fr.aresrpg.dofus.protocol.game.server.GameTeamPacket.TeamEntity;
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

	void onTeamAssign(int team);

	void onMap(BotMap map);

	void onInterractRespawn(Interractable type, int cellid);

	void onEntityLeave(int id);

	void onPlayerMove(MovementPlayer player);

	void onInvocMove(MovementInvocation invoc);

	void onMobMove(MovementMonster mob);

	void onNpcMove(MovementNpc npc);

	void onMobGroupMove(MovementMonsterGroup mobs);

	void onActionError();

	void onEntityFightPositionChange(int entityId, int position);

	void onPlayerReadyToFight(int entityId, boolean ready);

	void onFightStart();

	void onEntityTurnEnd(int entityId);

	void onFightTurnInfos(int... turns);

	void onFighterInfos(FightEntity... entities);

	void onEntityTurnReady(int entityId);

	void onEntityTurnStart(int entityId, int time);

	void onFightTeams(int firstId, TeamEntity... entities);

	void onFightSpawn(FightSpawn fight);

	void onFightRemoved(FightSpawn fight);

}
