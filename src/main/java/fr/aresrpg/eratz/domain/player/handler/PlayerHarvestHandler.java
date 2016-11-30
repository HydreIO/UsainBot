package fr.aresrpg.eratz.domain.player.handler;

import fr.aresrpg.eratz.domain.behavior.harvest.HarvestHandler;
import fr.aresrpg.eratz.domain.dofus.ressource.Ressource;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.player.Player;

/**
 * 
 * @since
 */
public class PlayerHarvestHandler implements HarvestHandler {

	private Perso perso;

	public PlayerHarvestHandler(Perso perso) {
		this.perso = perso;
	}

	@Override
	public void onRessourceSpawn(Ressource r) {
		if (perso != null) for (Ressource res : perso.getCurrentMap().getRessources())
			if (res.equals(r)) res.setSpawned();
	}

	@Override
	public void onRessourceRecolted(Player p, Ressource r) {
		if (perso != null) for (Ressource res : perso.getCurrentMap().getRessources())
			if (res.equals(r)) res.setSpawned(false);
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

}
