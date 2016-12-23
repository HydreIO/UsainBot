package fr.aresrpg.eratz.domain.ia.ability.fight;

import fr.aresrpg.dofus.protocol.fight.client.*;
import fr.aresrpg.dofus.protocol.game.actions.GameActions;
import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction;
import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction.PathFragment;
import fr.aresrpg.dofus.protocol.game.actions.client.GameJoinFightAction;
import fr.aresrpg.dofus.protocol.game.actions.client.GameLaunchSpellAction;
import fr.aresrpg.dofus.protocol.game.client.*;
import fr.aresrpg.eratz.domain.data.dofus.player.Spells;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.util.BotThread;

import java.util.List;

/**
 * 
 * @since
 */
public class FightAbilityImpl implements FightAbility {

	private Perso perso;
	private BotThread botThread;

	public FightAbilityImpl(Perso perso) {
		this.perso = perso;
	}

	@Override
	public void shutdown() {
		botThread = new BotThread();
	}

	/**
	 * @return the botThread
	 */
	@Override
	public BotThread getBotThread() {
		return botThread;
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	@Override
	public void launchSpell(Spells spell, int cellid) {
		GameLaunchSpellAction action = new GameLaunchSpellAction(spell.getId(), cellid);
		getPerso().sendPacketToServer(new GameClientActionPacket(GameActions.LAUNCH_SPELL, action));
		getBotThread().pause();
	}

	@Override
	public void setPosition(int pos) {
		GameSetPlayerPositionPacket pkt = new GameSetPlayerPositionPacket();
		pkt.setCellNum(pos);
		getPerso().sendPacketToServer(pkt);
		getBotThread().pause();
	}

	@Override
	public void move(List<PathFragment> path) {
		GameMoveAction action = new GameMoveAction();
		action.setPath(path);
		getPerso().sendPacketToServer(new GameClientActionPacket(GameActions.MOVE, action));
		getBotThread().pause();
	}

	@Override
	public void endTurn() {
		getPerso().sendPacketToServer(new GameEndTurnPacket());
		getBotThread().pause();
	}

	@Override
	public void beReady(boolean ready) {
		GameClientReadyPacket pkt = new GameClientReadyPacket();
		pkt.setReady(ready);
		getPerso().sendPacketToServer(pkt);
		getBotThread().pause();
	}

	@Override
	public void blockSpec(boolean block) {
		boolean blocked = getPerso().getFightInfos().getCurrentFight().isSpecBlocked();
		if (blocked != block) getPerso().sendPacketToServer(new FightBlockSpectatePacket());
	}

	@Override
	public void blockCombat(boolean block) {
		boolean blocked = getPerso().getFightInfos().getCurrentFight().isBlocked();
		if (blocked != block) getPerso().sendPacketToServer(new FightBlockAllPacket());
	}

	@Override
	public void blockToGroup(boolean block) {
		boolean blocked = getPerso().getFightInfos().getCurrentFight().isGroupBlocked();
		if (blocked != block) getPerso().sendPacketToServer(new FightRestrictGroupPacket());
	}

	@Override
	public void joinFight(int fightId) {
		GameJoinFightAction action = new GameJoinFightAction();
		action.setFightId(fightId);
		getPerso().sendPacketToServer(new GameClientActionPacket(GameActions.JOIN_FIGHT, action));
		getBotThread().pause();
	}

}
