package fr.aresrpg.eratz.domain.handler.bot.move;

import fr.aresrpg.dofus.structures.character.Character;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.structures.map.Mob;
import fr.aresrpg.eratz.domain.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.dofus.map.Ressource;
import fr.aresrpg.eratz.domain.player.Perso;

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
	public void onRessourceRecolted(Character p, Ressource r) {
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
		// if (perso != null) getPerso().setCurrentMap();
	}

	@Override
	public void onQuitMap(DofusMap m) {

	}

	@Override
	public void onMobSpawn(Mob m) {
		getPerso().getCurrentMap().getDofusMap().getMobs().add(m);
	}

	@Override
	public void onPlayerJoinMap(Character p, int cellId) {
		getPerso().getCurrentMap().getDofusMap().getPlayers().add(p);
	}

	@Override
	public void onPlayerQuitMap(Character p, int cellId) {
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
