package fr.aresrpg.eratz.domain.handler.bot.move;

import fr.aresrpg.eratz.domain.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.dofus.map.Map;
import fr.aresrpg.eratz.domain.dofus.mob.Mob;
import fr.aresrpg.eratz.domain.dofus.ressource.Ressource;
import fr.aresrpg.eratz.domain.player.Player;

/**
 * 
 * @since
 */
public interface MapHandler {

	void onJoinMap(Map m);

	void onQuitMap(Map m);

	void onMobSpawn(Mob m);

	void onRessourceSpawn(Ressource r);

	void onRessourceRecolted(Player p, Ressource r); // faire crash ce fdp si p != bot lmao

	void onPlayerJoinMap(Player p, int cellId);

	void onPlayerQuitMap(Player p, int cellId);

	void onFightSpawn(Fight fight, int cellId1, int cellId2);

	void onFightEnd();

}
