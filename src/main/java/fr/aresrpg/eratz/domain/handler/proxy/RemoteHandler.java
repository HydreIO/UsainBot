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
import fr.aresrpg.dofus.protocol.ProtocolRegistry.Bound;
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
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.util.Maps;
import fr.aresrpg.dofus.util.SwfVariableExtractor;
import fr.aresrpg.eratz.domain.ability.move.NavigationImpl;
import fr.aresrpg.eratz.domain.dofus.Constants;
import fr.aresrpg.eratz.domain.handler.BaseHandler;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.player.state.AccountState;
import fr.aresrpg.eratz.domain.proxy.DofusProxy;
import fr.aresrpg.eratz.domain.proxy.Proxy.ProxyConnectionType;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * 
 * @since
 */
public class RemoteHandler extends BaseHandler {

	/**
	 * @param account
	 */
	public RemoteHandler(DofusProxy proxy) {
		super(proxy);
	}

	private void transmit(Packet pkt) {
		try {
			System.out.println("[RCV:]<< " + pkt);
			getProxy().getLocalConnection().send(pkt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean parse(ProtocolRegistry registry, String packet) {
		if (registry == null || registry == ProtocolRegistry.GAME_MOVEMENT || registry == ProtocolRegistry.GAME_MAP_FRAME) {
			SocketChannel channel = (SocketChannel) getProxy().getLocalConnection().getChannel();
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
	public void handle(ChatSubscribeChannelPacket chatSubscribeChannelPacket) {
		Chat[] channels = chatSubscribeChannelPacket.getChannels();
		channels = Arrays.copyOf(channels, channels.length + 1);
		channels[channels.length - 1] = Chat.ADMIN;
		chatSubscribeChannelPacket.setChannels(channels);
		transmit(chatSubscribeChannelPacket);
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
		getProxy().setHc(pkt.getHashKey());
		transmit(pkt);
	}

	@Override
	public void handle(AccountAuthPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountLoginErrPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountLoginOkPacket pkt) {
		pkt.setAdmin(true);
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
		if (getAccount().isBotOnline())
			getAccount().getCurrentPlayed().disconnect();
		try {
			String ip = pkt.getIp();
			ServerSocketChannel srvchannel = ServerSocketChannel.open();
			srvchannel.bind(new InetSocketAddress(0));
			int localPort = srvchannel.socket().getLocalPort();
			getProxy().getLocalConnection().send(new AccountServerHostPacket().setIp(Constants.LOCALHOST).setPort(localPort).setTicketKey(pkt.getTicketKey()));
			getProxy().changeConnection(
					new DofusConnection<>("RemoteGame", SocketChannel.open(new InetSocketAddress(ip, 443)), getProxy().getRemoteHandler(), Bound.SERVER),
					ProxyConnectionType.REMOTE);
			getProxy().changeConnection(new DofusConnection<>("LocalGame", srvchannel.accept(), getProxy().getLocalHandler(), Bound.CLIENT),
					ProxyConnectionType.LOCAL);
			getAccount().setState(AccountState.CLIENT_IN_GAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		Arrays.stream(pkt.getCharacters()).filter(Objects::nonNull).forEach(c -> getAccount().getPersos().add(new Perso(c.getId(), c.getPseudo(), getAccount(), null, null)));
		transmit(pkt);
	}

	@Override
	public void handle(AccountSelectCharacterPacket pkt) {
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

	public Perso getPerso() {
		return getAccount().getCurrentPlayed();
	}

	@Override
	public void handle(GameMapDataPacket pkt) {
		transmit(pkt);
		try {
			Map<String, Object> d = SwfVariableExtractor.extractVariable(Maps.downloadMap(pkt.getMapId(),
					pkt.getSubid()));
			DofusMap m = Maps.loadMap(d, pkt.getDecryptKey());
			getPerso().getDebugView().setMap(m);
			((NavigationImpl) getPerso().getNavigation()).setMap(m);
			getAccount().getRemoteConnection().send(new GameExtraInformationPacket());
			getPerso().getDebugView().setOnCellClick(a -> Executors.FIXED.execute(() -> getPerso().getNavigation().moveToCell(a, m.getCells()[a].getMovement() == 2)));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
