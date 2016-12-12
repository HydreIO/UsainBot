package fr.aresrpg.eratz.domain.handler.bot.move;

import fr.aresrpg.dofus.protocol.game.movement.MovementPlayer;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.structures.map.Mob;
import fr.aresrpg.eratz.domain.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.dofus.map.BotMap;
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
		for (Ressource res : perso.getCurrentMap().getRessources())
			if (res.equals(r)) res.setSpawned();
	}

	@Override
	public void onRessourceRecolted(int id, Ressource r) {
		for (Ressource res : perso.getCurrentMap().getRessources())
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
		getPerso().setCurrentMap(BotMap.fromDofusMap(m));
	}

	@Override
	public void onQuitMap(DofusMap m) {

	}

	@Override
	public void onMobSpawn(Mob m) {
		getPerso().getCurrentMap().getDofusMap().getMobs().add(m);
	}

	@Override
	public void onPlayerJoinMap(MovementPlayer pl) {
		getPerso().getCurrentMap().getPlayers().add(pl);
	}

	@Override
	public void onPlayerQuitMap(int id) {
		getPerso().getCurrentMap().getPlayers().remove(Player.fromCharacter(p));
	}

	@Override
	public void onFightSpawn(Fight fight) {
		getPerso().getCurrentMap().getFights().add(fight);
	}

	@Override
	public void onFightEnd(Fight fight) {
		getPerso().getCurrentMap().getFights().remove(fight);
	}

}
