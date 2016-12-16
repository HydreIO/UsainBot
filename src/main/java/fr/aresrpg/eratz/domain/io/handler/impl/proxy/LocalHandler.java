/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.io.handler.impl.proxy;

import fr.aresrpg.dofus.protocol.*;
import fr.aresrpg.dofus.protocol.account.client.AccountAuthPacket;
import fr.aresrpg.dofus.protocol.account.client.AccountSelectCharacterPacket;
import fr.aresrpg.dofus.protocol.game.actions.GameAction;
import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction;
import fr.aresrpg.dofus.protocol.game.client.GameClientActionPacket;
import fr.aresrpg.dofus.structures.PathDirection;
import fr.aresrpg.eratz.domain.data.AccountsManager;
import fr.aresrpg.eratz.domain.data.player.Account;
import fr.aresrpg.eratz.domain.ia.ability.move.NavigationImpl;
import fr.aresrpg.eratz.domain.io.handler.BaseClientPacketHandler;
import fr.aresrpg.eratz.domain.io.proxy.DofusProxy;
import fr.aresrpg.eratz.domain.io.proxy.Proxy;
import fr.aresrpg.eratz.domain.util.encryption.CryptHelper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;

/**
 * 
 * @since
 */
public class LocalHandler extends BaseClientPacketHandler {

	private boolean state_machine = false;
	private Account account;
	private Proxy proxy;

	public LocalHandler(Proxy proxy) {
		this.proxy = proxy;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	protected void transmit(Packet pkt) {
		try {
			System.out.println("[SEND:]>> " + pkt);
			getProxy().getRemoteConnection().send(pkt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the proxy
	 */
	public Proxy getProxy() {
		return proxy;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @param state_machine
	 *            the state_machine to set
	 */
	public void setStateMachine(boolean state_machine) {
		this.state_machine = state_machine;
	}

	private boolean contains(ProtocolRegistry registry) {
		for (ProtocolRegistry r : RemoteHandler.toSkip)
			if (r == registry) return true;
		return false;
	}

	@Override
	public boolean parse(ProtocolRegistry registry, String packet) {
		if (registry == null || contains(registry)) {
			try {
				((SocketChannel) getProxy().getLocalConnection().getChannel()).write(ByteBuffer.wrap((packet + "\n\0").getBytes()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public void register(DofusConnection<?> connection) {
	}

	@Override
	public void handle(AccountAuthPacket pkt) {
		setAccount(AccountsManager.getInstance().getOrRegister(pkt.getPseudo(), CryptHelper.decryptpass(pkt.getHashedPassword().substring(2), getProxy().getHc())));
		((DofusProxy) getProxy()).initAccount(getAccount());
		transmit(pkt);
		state_machine = true;
	}

	@Override
	public void handle(AccountSelectCharacterPacket pkt) {
		getAccount().getPersos().stream()
				.filter(p -> p.getId() == pkt.getCharacterId())
				.forEach(getAccount()::setCurrentPlayed);
		transmit(pkt);
	}

	@Override
	public void handle(GameClientActionPacket gameActionPacket) {
		GameAction action = gameActionPacket.getAction();
		if (action instanceof GameMoveAction) {
			GameMoveAction a = (GameMoveAction) action;
			int id = 0;
			for (Map.Entry<Integer, PathDirection> e : a.getPath().entrySet())
				id = e.getKey();
			((NavigationImpl) getAccount().getCurrentPlayed().getNavigation()).setCurrentPos(id);
		}
		transmit(gameActionPacket);
	}

}
