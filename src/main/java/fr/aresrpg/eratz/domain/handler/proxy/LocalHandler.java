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
import fr.aresrpg.dofus.protocol.account.AccountKeyPacket;
import fr.aresrpg.dofus.protocol.account.AccountRegionalVersionPacket;
import fr.aresrpg.dofus.protocol.account.client.*;
import fr.aresrpg.dofus.protocol.account.server.*;
import fr.aresrpg.dofus.protocol.basic.server.BasicConfirmPacket;
import fr.aresrpg.dofus.protocol.chat.ChatSubscribeChannelPacket;
import fr.aresrpg.dofus.protocol.game.client.*;
import fr.aresrpg.dofus.protocol.game.server.*;
import fr.aresrpg.dofus.protocol.hello.client.HelloGamePacket;
import fr.aresrpg.dofus.protocol.hello.server.HelloConnectionPacket;
import fr.aresrpg.dofus.protocol.info.client.InfoMapPacket;
import fr.aresrpg.dofus.protocol.info.server.message.InfoMessagePacket;
import fr.aresrpg.dofus.protocol.mount.client.PlayerMountPacket;
import fr.aresrpg.dofus.protocol.mount.server.MountXpPacket;
import fr.aresrpg.dofus.protocol.specialization.server.SpecializationSetPacket;
import fr.aresrpg.eratz.domain.handler.BaseHandler;
import fr.aresrpg.eratz.domain.player.AccountsManager;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.proxy.DofusProxy;
import fr.aresrpg.eratz.domain.util.encryption.CryptHelper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 
 * @since
 */
public class LocalHandler extends BaseHandler {

	private boolean state_machine = false;

	/**
	 * @param account
	 */
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

	private void transmit(Packet pkt) {
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
	public void handle(AccountSelectCharacterOkPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(ChatSubscribeChannelPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void register(DofusConnection<?> connection) {
	}

	@Override
	public void handle(HelloGamePacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(HelloConnectionPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountAuthPacket pkt) {
		setAccount(AccountsManager.getInstance().getOrRegister(pkt.getPseudo(), CryptHelper.decryptpass(pkt.getHashedPassword().substring(2), getProxy().getHc())));
		((DofusProxy) getProxy()).initAccount(getAccount());
		transmit(pkt);
		state_machine = true;
	}

	@Override
	public void handle(AccountLoginErrPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountLoginOkPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountCommunityPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountHostPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountQuestionPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountPseudoPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountListServersPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountServerListPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountAccessServerPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountServerEncryptedHostPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountServerHostPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountTicketPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountTicketOkPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(BasicConfirmPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountKeyPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountRegionalVersionPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountGetGiftsPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountIdentity pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountGetCharactersPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountCharactersListPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountSelectCharacterPacket pkt) {
		for (Perso p : getAccount().getPersos()) {
			if (p.getId() == pkt.getCharacterId()) {
				getAccount().setCurrentPlayed(p);
				break;
			}
		}
		transmit(pkt);
	}

	@Override
	public void handle(AccountGetQueuePosition pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountQueuePosition pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(MountXpPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameExtraInformationPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(InfoMessagePacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(SpecializationSetPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(InfoMapPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameCreatePacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameMapDataPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(PlayerMountPacket playerMountPacket) {
		transmit(playerMountPacket);
	}

	@Override
	public void handle(GameJoinPacket gameJoinPacket) {
		transmit(gameJoinPacket);
	}

	@Override
	public void handle(GameEndTurnPacket gameEndTurnPacket) {
		transmit(gameEndTurnPacket);
	}

	@Override
	public void handle(GameTurnOkPacket gameTurnOkPacket) {
		transmit(gameTurnOkPacket);
	}

	@Override
	public void handle(FreeMySoulPacket freeMySoulPacket) {
		transmit(freeMySoulPacket);
	}

	@Override
	public void handle(LeaveGamePacket leaveGamePacket) {
		transmit(leaveGamePacket);
	}

	@Override
	public void handle(GameSetPlayerPositionPacket gameSetPlayerPositionPacket) {
		transmit(gameSetPlayerPositionPacket);
	}

	@Override
	public void handle(GamePositionStartPacket gamePositionStartPacket) {
		transmit(gamePositionStartPacket);
	}

	@Override
	public void handle(GameOnReadyPacket gameOnReadyPacket) {
		transmit(gameOnReadyPacket);
	}

	@Override
	public void handle(GameStartPacket gameStartPacket) {
		transmit(gameStartPacket);
	}

	@Override
	public void handle(GameEndPacket gameEndPacket) {
		transmit(gameEndPacket);
	}

	@Override
	public void handle(GameActionPacket gameActionPacket) {
		transmit(gameActionPacket);
	}

	@Override
	public void handle(GameMovementPacket gameMovementPacket) {
		transmit(gameMovementPacket);
	}

	@Override
	public void handle(GameMapFramePacket gameMapFramePacket) {
		transmit(gameMapFramePacket);
	}

	@Override
	public void handle(GameActionACKPacket gameActionACKPacket) {
		transmit(gameActionACKPacket);
	}

}
