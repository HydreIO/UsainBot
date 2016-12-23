package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import fr.aresrpg.dofus.protocol.game.client.GameExtraInformationPacket;
import fr.aresrpg.dofus.protocol.game.movement.*;
import fr.aresrpg.dofus.protocol.game.server.GameEndPacket;
import fr.aresrpg.dofus.protocol.game.server.GameTeamPacket.TeamEntity;
import fr.aresrpg.dofus.structures.game.*;
import fr.aresrpg.eratz.domain.data.dofus.map.BotMap;
import fr.aresrpg.eratz.domain.data.dofus.ressource.Interractable;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.std.game.GameServerHandler;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

/**
 * 
 * @since
 */
public class BotGameServerHandler extends BotHandlerAbstract implements GameServerHandler {

	/**
	 * @param perso
	 */
	public BotGameServerHandler(Perso perso) {
		super(perso);
	}

	@Override
	public void onEffect(Effect eff, int... entities) {
		// TODO

	}

	@Override
	public void onFightEnd(GameEndPacket pkt) {
		// TODO

	}

	@Override
	public void onFightChallenge(FightChallenge chall) {
		// TODO

	}

	@Override
	public void onFightJoin(GameType state, FightType fightType, boolean isSpectator, int startTimer, boolean cancelButton, boolean isDuel) {
		getPerso().getAbilities().getFightAbility().getBotThread().unpause();
	}

	@Override
	public void onTeamAssign(int team) {
		getPerso().getFightInfos().setCurrentFightTeam(team);
	}

	@Override
	public void onMap(BotMap map) {
		getPerso().sendPacketToServer(new GameExtraInformationPacket());
		getPerso().getDebugView().setOnCellClick(a -> Executors.FIXED.execute(() -> {
			System.out.println(map.getDofusMap().getCell(a));
			getPerso().getNavigation().moveToCell(a);
		}));
		getPerso().getDebugView().setPath(null);
		getPerso().getDebugView().setMap(map.getDofusMap());
		getPerso().getAccount().notifyBotOnline(); // pour autoriser les actions qui onts besoin que le bot soit bien en jeux
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onInterractRespawn(Interractable type, int cellid) {
		// TODO
	}

	@Override
	public void onEntityLeave(int id) {
	}

	@Override
	public void onPlayerMove(MovementPlayer player) {
		// AntiBot.getInstance().notifyMove(getPerso());
		if (player.getId() != getPerso().getId()) getPerso().getDebugView().addPlayer(player.getId(), player.getCell());
	}

	@Override
	public void onInvocMove(MovementInvocation invoc) {

	}

	@Override
	public void onMobMove(MovementMonster mob) {
		getPerso().getDebugView().addMob(mob.getId(), mob.getCellId());

	}

	@Override
	public void onNpcMove(MovementNpc npc) {
		getPerso().getDebugView().addNpc(npc.getId(), npc.getCellid());
	}

	@Override
	public void onMobGroupMove(MovementMonsterGroup mobs) {
		getPerso().getDebugView().addMob(mobs.getId(), mobs.getCellid());
	}

	@Override
	public void onEntityFightPositionChange(int entityId, int position) {
		if (entityId == getPerso().getId()) getPerso().getAbilities().getFightAbility().getBotThread().unpause();
	}

	@Override
	public void onPlayerReadyToFight(int entityId, boolean ready) {
		if (entityId == getPerso().getId()) getPerso().getAbilities().getFightAbility().getBotThread().unpause();
	}

	@Override
	public void onFightStart() {
		// TODO

	}

	@Override
	public void onEntityTurnEnd(int entityId) {
		if (entityId == getPerso().getId()) getPerso().getAbilities().getFightAbility().getBotThread().unpause();
	}

	@Override
	public void onFightTurnInfos(int... turns) {
		// TODO

	}

	@Override
	public void onFighterInfos(FightEntity... entities) {
		// TODO

	}

	@Override
	public void onEntityTurnReady(int entityId) {
		// TODO

	}

	@Override
	public void onEntityTurnStart(int entityId, int time) {
		// TODO

	}

	@Override
	public void onFightTeams(int firstId, TeamEntity... entities) {
		// TODO

	}

	@Override
	public void onFightSpawn(FightSpawn fight) {
		// TODO

	}

	@Override
	public void onFightRemoved(FightSpawn fight) {
		// TODO

	}

}
