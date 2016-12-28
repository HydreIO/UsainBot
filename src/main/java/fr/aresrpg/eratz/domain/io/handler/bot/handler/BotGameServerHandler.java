package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import fr.aresrpg.dofus.protocol.game.client.*;
import fr.aresrpg.dofus.protocol.game.movement.*;
import fr.aresrpg.dofus.protocol.game.server.GameEndPacket;
import fr.aresrpg.dofus.protocol.game.server.GameTeamPacket.TeamEntity;
import fr.aresrpg.dofus.structures.game.*;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.eratz.domain.data.dofus.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.std.game.GameServerHandler;

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
		GameCreatePacket pktc = new GameCreatePacket();
		pktc.setGameType(GameType.SOLO);
		getPerso().sendPacketToServer(pktc);
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
	}

	@Override
	public void onMap(BotMap map) {
		getPerso().sendPacketToServer(new GameExtraInformationPacket());
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
	}

	@Override
	public void onMobMove(MovementMonster mob) {

	}

	@Override
	public void onNpcMove(MovementNpc npc) {
	}

	@Override
	public void onMobGroupMove(MovementMonsterGroup mobs) {
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
		getPerso().sendPacketToServer(new GameTurnOkPacket());
	}

	@Override
	public void onEntityTurnStart(int entityId, int time) {
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
