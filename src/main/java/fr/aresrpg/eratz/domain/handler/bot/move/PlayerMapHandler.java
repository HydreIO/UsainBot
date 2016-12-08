package fr.aresrpg.eratz.domain.handler.bot.move;

import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.structures.map.Mob;
import fr.aresrpg.eratz.domain.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.dofus.map.Ressource;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.player.Player;

/**
 * 
 * @since
 */
public class PlayerMapHandler implements MapHandler {

	private Perso perso;

	public PlayerMapHandler(Perso perso) {
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

	@Override
	public void onJoinMap(DofusMap m) {
		// TODO

	}

	@Override
	public void onQuitMap(DofusMap m) {
		// TODO

	}

	@Override
	public void onMobSpawn(Mob m) {
		// TODO

	}

	@Override
	public void onPlayerJoinMap(Player p, int cellId) {
		// TODO

	}

	@Override
	public void onPlayerQuitMap(Player p, int cellId) {
		// TODO

	}

	@Override
	public void onFightSpawn(Fight fight, int cellId1, int cellId2) {
		// TODO

	}

	@Override
	public void onFightEnd() {
		// TODO

	}

}
