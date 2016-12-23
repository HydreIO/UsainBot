package fr.aresrpg.eratz.domain.ia.ability.fight;

import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction.PathFragment;
import fr.aresrpg.eratz.domain.data.dofus.player.Spells;
import fr.aresrpg.eratz.domain.util.BotThread;
import fr.aresrpg.eratz.domain.util.Closeable;

import java.util.List;

/**
 * 
 * @since
 */
public interface FightAbility extends Closeable {

	void launchSpell(Spells spell, int cellid);

	void setPosition(int pos);

	void move(List<PathFragment> path); // array pour choisir exactement le chemin a suivre

	void endTurn();

	void beReady(boolean ready);

	void blockSpec(boolean block);

	void blockCombat(boolean block);

	void blockToGroup(boolean block);

	void joinFight(int fightId);

	BotThread getBotThread();

}
