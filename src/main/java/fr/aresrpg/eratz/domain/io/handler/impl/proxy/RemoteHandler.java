/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.io.handler.impl.proxy;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.dofus.protocol.*;
import fr.aresrpg.dofus.protocol.ProtocolRegistry.Bound;
import fr.aresrpg.dofus.protocol.account.server.*;
import fr.aresrpg.dofus.protocol.chat.ChatSubscribeChannelPacket;
import fr.aresrpg.dofus.protocol.game.server.GameMapDataPacket;
import fr.aresrpg.dofus.protocol.game.server.GameServerActionPacket;
import fr.aresrpg.dofus.protocol.hello.server.HelloConnectionPacket;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.eratz.domain.data.player.Account;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.state.AccountState;
import fr.aresrpg.eratz.domain.io.handler.BaseServerPacketHandler;
import fr.aresrpg.eratz.domain.io.proxy.Proxy;
import fr.aresrpg.eratz.domain.io.proxy.Proxy.ProxyConnectionType;
import fr.aresrpg.eratz.domain.util.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * 
 * @since
 */
public class RemoteHandler extends BaseServerPacketHandler {

	static final ProtocolRegistry[] toSkip = { ProtocolRegistry.GAME_MOVEMENT };
	private Account account;
	private Proxy proxy;

	/**
	 * @param perso
	 */
	public RemoteHandler(Proxy proxy, Perso perso) {
		super(perso);
		this.proxy = proxy;
		this.account = perso.getAccount();
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
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

	protected void transmit(Packet pkt) {
		try {
			LOGGER.info("[RCV:]<< " + pkt);
			getProxy().getLocalConnection().send(pkt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean contains(ProtocolRegistry registry) {
		for (ProtocolRegistry r : toSkip)
			if (r == registry) return true;
		return false;
	}

	@Override
	public boolean parse(ProtocolRegistry registry, String packet) {
		if (registry == null || contains(registry)) {
			try {
				((SocketChannel) getProxy().getLocalConnection().getChannel()).write(ByteBuffer.wrap((packet + "\0").getBytes()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public void handle(AccountSelectCharacterOkPacket pkt) {
		super.handle(pkt);
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
	public void handle(HelloConnectionPacket pkt) {
		super.handle(pkt);
		getProxy().setHc(pkt.getHashKey());
		transmit(pkt);
	}

	@Override
	public void handle(AccountLoginOkPacket pkt) {
		super.handle(pkt);
		pkt.setAdmin(true);
		transmit(pkt);
	}

	@Override
	public void handle(AccountServerEncryptedHostPacket pkt) {
		super.handle(pkt);
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
	public void handle(AccountCharactersListPacket pkt) { // tkt on verifie bien que les persos existe pas déja
		super.handle(pkt);
		//Arrays.stream(pkt.getCharacters()).filter(Objects::nonNull).forEach(c -> getAccount().getPersos().add(new Perso(c.getId(), c.getPseudo(), getAccount(), null, null)));
		transmit(pkt);
	}

	public Perso getPerso() {
		return getAccount().getCurrentPlayed();
	}

	@Override
	public void handle(GameMapDataPacket pkt) {
		super.handle(pkt);
		transmit(pkt);
		/*
		 * getPerso().getDebugView().setOnCellClick(a -> Executors.FIXED.execute(() -> {
		 * Cell cell = m.getCells()[a];
		 * LOGGER.info("frame = " + cell.getFrame());
		 * LOGGER.info("Groundlvl = " + cell.getGroundLevel());
		 * LOGGER.info("GroundSlope = " + cell.getGroundSlope());
		 * LOGGER.info("GroundNum = " + cell.getLayerGroundNum());
		 * LOGGER.info("GroundRot = " + cell.getLayerGroundRot());
		 * LOGGER.info("Object1Num = " + cell.getLayerObject1Num());
		 * LOGGER.info("Object1Rot = " + cell.getLayerObject1Rot());
		 * LOGGER.info("Movement = " + cell.getMovement());
		 * LOGGER.info("isLayerGroundFlip = " + cell.isLayerGroundFlip());
		 * LOGGER.info("isLayerObject1Flip = " + cell.isLayerObject1Flip());
		 * LOGGER.info("isLayerObject2Flip = " + cell.isLayerObject2Flip());
		 * LOGGER.info("isLayerObject2Inter = " + cell.isLayerObject2Interactive());
		 * LOGGER.info("lineOfSight = " + cell.isLineOfSight());
		 * LOGGER.info("LayerObject2Num = " + cell.getLayerObject2Num());
		 * Executors.FIXED.execute(() -> getPerso().getNavigation().moveToCell(a));
		 * }));
		 */
	}

	@Override
	public void handle(GameServerActionPacket pkt) {
		super.handle(pkt);
		LOGGER.info("" + pkt.getEntityId());
		LOGGER.info("" + getPerso().getId());
		transmit(pkt);
	}

}
