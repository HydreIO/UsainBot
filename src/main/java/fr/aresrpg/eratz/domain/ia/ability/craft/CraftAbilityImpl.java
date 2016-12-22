/*******************************************************************************
 * BotFather (C) - Dofus 1.29 bot
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.ia.ability.craft;

import fr.aresrpg.dofus.protocol.exchange.client.*;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.util.BotThread;

/**
 * 
 * @since
 */
public class CraftAbilityImpl implements CraftAbility {

	private Perso perso;
	private BotThread botThread;

	public CraftAbilityImpl(Perso perso) {
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
	public void startCraft(int quantity) {
		startCraft();
		if (quantity == 1) return;
		getPerso().sendPacketToServer(new ExchangeRepeatCraftPacket(quantity - 1));
		getBotThread().pause();
	}

	@Override
	public void cancelCraft() {
		getPerso().sendPacketToServer(new ExchangeRepeatCraftPacket(true));
	}

	@Override
	public void startCraft() {
		getPerso().sendPacketToServer(new ExchangeSendReadyPacket());
	}

	@Override
	public void replaceCraft() {
		getPerso().sendPacketToServer(new ExchangeReplayCraftPacket());
		getBotThread().pause();
	}

}
