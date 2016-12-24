/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.ia.ability.move;

import fr.aresrpg.eratz.domain.data.dofus.map.Zaap;
import fr.aresrpg.eratz.domain.data.dofus.map.Zaapi;

/**
 * 
 * @since
 */
public interface Navigation {

	Navigation moveUp();

	default Navigation moveUp(int nbr) {
		for (int i = 0; i < nbr; i++)
			moveUp();
		return this;
	}

	Navigation moveDown();

	default Navigation moveDown(int nbr) {
		for (int i = 0; i < nbr; i++)
			moveDown();
		return this;
	}

	Navigation moveLeft();

	default Navigation moveLeft(int nbr) {
		for (int i = 0; i < nbr; i++)
			moveLeft();
		return this;
	}

	Navigation moveRight();

	default Navigation moveRight(int nbr) {
		for (int i = 0; i < nbr; i++)
			moveRight();
		return this;
	}

	Navigation moveToCell(int cellid, boolean teleport);

	default Navigation moveToCell(int cellid) {
		return moveToCell(cellid, false);
	}

	Navigation takeZaap(Zaap destination);

	Navigation takeZaapi(Zaapi destination);

	void joinCoords(int x, int y);

	void notifyMovementEnd();

}
