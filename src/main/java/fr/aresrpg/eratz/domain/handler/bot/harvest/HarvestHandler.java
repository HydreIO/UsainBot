package fr.aresrpg.eratz.domain.handler.bot.harvest;

import fr.aresrpg.eratz.domain.dofus.ressource.Ressource;
import fr.aresrpg.eratz.domain.player.Player;

/**
 * 
 * @since
 */
public interface HarvestHandler {

	void onRessourceSpawn(Ressource r);

	void onRessourceRecolted(Player p, Ressource r); // faire crash ce fdp si p != bot

}
