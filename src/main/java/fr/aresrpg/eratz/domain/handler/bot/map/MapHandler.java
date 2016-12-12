package fr.aresrpg.eratz.domain.handler.bot.map;

import fr.aresrpg.dofus.protocol.game.movement.*;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.eratz.domain.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.dofus.map.Ressource;

/**
 * 
 * @since
 */
public interface MapHandler {

	void onJoinMap(DofusMap m);

	void onQuitMap(DofusMap m);

	void onRessourceSpawn(Ressource r);

	void onRessourceRecolted(int id, Ressource r); // faire crash ce fdp si p != bot lmao

	void onPlayerMove(MovementPlayer p);

	void onInvocMove(MovementInvocation i);

	void onMobMove(MovementMonster m);

	void onMobGroupMove(MovementMonsterGroup mg);

	void onNpcMove(MovementNpc npc);

	void onEntityLeave(int id);

	void onFightSpawn(Fight fight);

	void onFightEnd(Fight fight);

}
