/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.handler.proxy;

import fr.aresrpg.dofus.protocol.*;
import fr.aresrpg.dofus.protocol.account.client.AccountAuthPacket;
import fr.aresrpg.dofus.protocol.account.client.AccountSelectCharacterPacket;
import fr.aresrpg.dofus.protocol.game.actions.GameAction;
import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction;
import fr.aresrpg.dofus.protocol.game.client.GameClientActionPacket;
import fr.aresrpg.dofus.structures.PathDirection;
import fr.aresrpg.eratz.domain.ability.move.NavigationImpl;
import fr.aresrpg.eratz.domain.player.AccountsManager;
import fr.aresrpg.eratz.domain.proxy.DofusProxy;
import fr.aresrpg.eratz.domain.util.encryption.CryptHelper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;

/**
 * 
 * @since
 */
public class LocalHandler extends TransfertHandler {

	private boolean state_machine = false;

	public LocalHandler(DofusProxy proxy) {
		super(proxy);
	}

	/**
	 * @param state_machine
	 *            the state_machine to set
	 */
	public void setStateMachine(boolean state_machine) {
		this.state_machine = state_machine;
	}

	@Override
	protected void transmit(Packet pkt) {
		try {
			System.out.println("[SEND:]>> " + pkt);
			getProxy().getRemoteConnection().send(pkt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean parse(ProtocolRegistry registry, String packet) {
		if (state_machine && registry == null) {
			SocketChannel channel = (SocketChannel) getProxy().getRemoteConnection().getChannel();
			try {
				packet += "\0";
				channel.write(ByteBuffer.wrap(packet.getBytes()));
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
