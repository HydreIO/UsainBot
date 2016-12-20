/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.io.handler;

import fr.aresrpg.dofus.protocol.ClientPacketHandler;
import fr.aresrpg.dofus.protocol.DofusConnection;
import fr.aresrpg.eratz.domain.data.player.Perso;

/**
 * 
 * @since
 */
public abstract class BaseClientPacketHandler implements ClientPacketHandler {

	private Perso perso;

	public BaseClientPacketHandler(Perso perso) {
		this.perso = perso;
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	/**
	 * @param perso
	 *            the perso to set
	 */
	public void setPerso(Perso perso) {
		this.perso = perso;
	}

	@Override
	public void register(DofusConnection<?> connection) {
		// TODO

	}

}
