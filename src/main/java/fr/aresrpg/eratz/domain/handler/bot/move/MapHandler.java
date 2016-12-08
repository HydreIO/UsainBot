package fr.aresrpg.eratz.domain.handler.bot.move;

import fr.aresrpg.dofus.structures.character.Character;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.structures.map.Mob;
import fr.aresrpg.eratz.domain.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.dofus.map.Ressource;

/**
 * 
 * @since
 */
public interface MapHandler {

	void onJoinMap(DofusMap m);

	void onQuitMap(DofusMap m);

	void onMobSpawn(Mob m);

	void onRessourceSpawn(Ressource r);

	void onRessourceRecolted(Character p, Ressource r); // faire crash ce fdp si p != bot lmao

	void onPlayerJoinMap(Character p, int cellId);

	void onPlayerQuitMap(Character p, int cellId);

	void onFightSpawn(Fight fight, int cellId1, int cellId2);

	void onFightEnd();

}
