package fr.aresrpg.eratz.domain.io.handler.bot.map;

import fr.aresrpg.dofus.protocol.game.movement.*;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.structures.map.Frame;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.dofus.map.BotMap;
import fr.aresrpg.eratz.domain.player.Perso;

import java.util.Set;

/**
 * 
 * @since
 */
public class BaseMapHandler implements MapHandler {

	private Perso perso;

	public BaseMapHandler(Perso perso) {
		this.perso = perso;
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	public BotMap getMap() {
		return getPerso().getMapInfos().getMap();
	}

	@Override
	public void onPlayerMove(MovementPlayer pl) {
		if (pl.getId() == getPerso().getId()) {
			getPerso().getMapInfos().setCellId(pl.getCell());
			getPerso().getNavigation().notifyMovementEnd();
		}
		Set<MovementPlayer> pls;
		if (pl.isFight()) pls = getPerso().getFightInfos().getCurrentFight().getTeam(pl.getPlayerInFight().getTeam());
		else pls = getMap().getPlayers();
		if (pls.contains(pl)) pls.remove(pl); // on peut tricher car le equals de movementplayer est redéfini
		pls.add(pl); // comme ça j'enleve et j'add le nouveau avec les bon fields
	}

	@Override
	public void onFightSpawn(Fight fight) {
		getMap().getFights().add(fight);
	}

	@Override
	public void onFightEnd(Fight fight) {
		if(getPerso().getFightInfos())
		// TODO remove fight
	}

	@Override
	public void onEntityLeave(int id) {
		getPerso().getDebugView().removeActor(id);
	}

	@Override
	public void onJoinMap(DofusMap m) {
		getPerso().getMapInfos().setMap(MapsManager.getOrCreate(m));
		getPerso().getDebugView().setPath(null);
		getPerso().getDebugView().setMap(m);
	}

	@Override
	public void onQuitMap(DofusMap m) {

	}

	@Override
	public void onInvocMove(MovementInvocation i) {

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

	@Override
	public void onFrameUpdate(int cellid, Frame frame) {

	}

}
