/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.io.handler.std.fight;

import fr.aresrpg.dofus.structures.game.FightDetail;
import fr.aresrpg.dofus.structures.game.FightInfos;

import java.util.List;

/**
 * 
 * @since
 */
public interface FightServerHandler {

	void onFightCount(int count);

	void onFightInfos(FightInfos infos);

	void onFightDetails(int fightId, List<FightDetail> team0, List<FightDetail> team1);
}
