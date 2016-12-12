package fr.aresrpg.eratz.domain.handler.bot.move;

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

	void onPlayerJoinMap(MovementPlayer p);

	void onInvocSpawn(MovementInvocation i);

	void onMobSpawn(MovementMonster m);

	void onMobGroupSpawn(MovementMonsterGroup mg);

	void onActorLeaveMap(int id);

	void onFightSpawn(Fight fight);

	void onFightEnd(Fight fight);

}
