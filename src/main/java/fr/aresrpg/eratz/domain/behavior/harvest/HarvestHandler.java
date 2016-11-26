package fr.aresrpg.eratz.domain.behavior.harvest;

import fr.aresrpg.eratz.domain.dofus.ressource.Ressources;
import fr.aresrpg.eratz.domain.player.Player;

/**
 * 
 * @since
 */
public interface HarvestHandler {

	void onRessourceSpawn(Ressources type);

	void onRessourceRecolted(Player p); // faire crash ce fdp si p != bot

}
