package fr.aresrpg.eratz.domain.handler.bot.map;

import fr.aresrpg.dofus.protocol.game.movement.*;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.eratz.domain.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.dofus.map.BotMap;
import fr.aresrpg.eratz.domain.dofus.map.Ressource;
import fr.aresrpg.eratz.domain.player.Perso;

/**
 * 
 * @since
 */
public class BaseMapHandler implements MapHandler {

	private Perso perso;

	public BaseMapHandler(Perso perso) {
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

	public BotMap getMap() {
		return getPerso().getCurrentMap();
	}


	@Override
	public void onPlayerMove(MovementPlayer pl) {
		if(pl.getId() == getPerso().getId()) {
			
		}
		getPerso().getCurrentMap().getPlayers().add(pl);
	}

	@Override
	public void onFightSpawn(Fight fight) {
		getPerso().getCurrentMap().getFights().add(fight);
	}

	@Override
	public void onFightEnd(Fight fight) {
		getPerso().getCurrentMap().getFights().remove(fight);
	}

	@Override
	public void onEntityLeave(int id) {
		getPerso().getDebugView().removeActor(id);
	}

	@Override
	public void onJoinMap(DofusMap m) {
		// TODO
		
	}

	@Override
	public void onQuitMap(DofusMap m) {
		getPerso().setCurrentMap(BotMap.fromDofusMap(m));
		getPerso().getDebugView().setPath(null);
		getPerso().getDebugView().setMap(m);
	}

	@Override
	public void onInvocMove(MovementInvocation i) {
		// TODO
		
	}

	@Override
	public void onMobMove(MovementMonster m) {
		// TODO
		
	}

	@Override
	public void onMobGroupMove(MovementMonsterGroup mg) {
		// TODO
		
	}

	@Override
	public void onNpcMove(MovementNpc npc) {
		// TODO
		
	}
}
