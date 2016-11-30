/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.behavior.move;

import fr.aresrpg.eratz.domain.behavior.Behavior;
import fr.aresrpg.eratz.domain.dofus.map.Zaap;
import fr.aresrpg.eratz.domain.dofus.map.Zaapi;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public interface Navigation extends Behavior {

	Navigation moveUp();

	Navigation moveUp(int nbr);

	Navigation moveDown();

	Navigation moveDown(int nbr);

	Navigation moveLeft();

	Navigation moveLeft(int nbr);

	Navigation moveRight();

	Navigation moveRight(int nbr);

	Navigation moveToCell(int cellid);

	Navigation takeZaap(Zaap destination);

	Navigation takeZaapi(Zaapi destination);

	@Override
	default Navigation botWait(int time, TimeUnit unit) {
		return Behavior.super.botWait(time, unit);
	}

	@Override
	default Navigation waitSec(int sec) {
		return Behavior.super.waitSec(sec);
	}

}
