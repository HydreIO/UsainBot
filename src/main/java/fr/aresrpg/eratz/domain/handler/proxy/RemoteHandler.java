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
import fr.aresrpg.dofus.protocol.account.client.AccountSelectCharacterPacket;
import fr.aresrpg.dofus.protocol.account.server.*;
import fr.aresrpg.dofus.protocol.chat.ChatSubscribeChannelPacket;
import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction;
import fr.aresrpg.dofus.protocol.game.client.GameExtraInformationPacket;
import fr.aresrpg.dofus.protocol.game.movement.*;
import fr.aresrpg.dofus.protocol.game.server.*;
import fr.aresrpg.dofus.protocol.hello.server.HelloConnectionPacket;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.dofus.structures.PathDirection;
import fr.aresrpg.dofus.structures.game.GameMovementType;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.util.Maps;
import fr.aresrpg.dofus.util.SwfVariableExtractor;
import fr.aresrpg.eratz.domain.ability.move.NavigationImpl;
import fr.aresrpg.eratz.domain.dofus.Constants;
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
public class RemoteHandler extends TransfertHandler {

	/**
	 * @param account
	 */
	public RemoteHandler(DofusProxy proxy) {
		super(proxy);
	}

	@Override
	protected void transmit(Packet pkt) {
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
		// throw new UnsupportedOperationException();
		return true; // temp
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
	public void handle(HelloConnectionPacket pkt) {
		getProxy().setHc(pkt.getHashKey());
		transmit(pkt);
	}

	@Override
	public void handle(AccountLoginOkPacket pkt) {
		pkt.setAdmin(true);
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
	public void handle(AccountCharactersListPacket pkt) { // tkt on verifie bien que les persos existe pas déja
		Arrays.stream(pkt.getCharacters()).filter(Objects::nonNull).forEach(c -> getAccount().getPersos().add(new Perso(c.getId(), c.getPseudo(), getAccount(), null, null)));
		transmit(pkt);
	}

	@Override
	public void handle(AccountSelectCharacterPacket pkt) {
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
			getPerso().getDebugView().clearPath();
			((NavigationImpl) getPerso().getNavigation()).setMap(m);
			getAccount().getRemoteConnection().send(new GameExtraInformationPacket());
			getPerso().getDebugView().setOnCellClick(a -> Executors.FIXED.execute(() -> {
				Cell cell = m.getCells()[a];
				System.out.println("frame = " + cell.getFrame() + (cell.getFrame() != null ? cell.getFrame().getId() : ""));
				System.out.println("Groundlvl = " + cell.getGroundLevel());
				System.out.println("GroundSlope = " + cell.getGroundSlope());
				System.out.println("GroundNum = " + cell.getLayerGroundNum());
				System.out.println("GroundRot = " + cell.getLayerGroundRot());
				System.out.println("Object1Num = " + cell.getLayerObject1Num());
				System.out.println("Object1Rot = " + cell.getLayerObject1Rot());
				System.out.println("Movement = " + cell.getMovement());
				System.out.println("isLayerGroundFlip = " + cell.isLayerGroundFlip());
				System.out.println("isLayerObject1Flip = " + cell.isLayerObject1Flip());
				System.out.println("isLayerObject2Flip = " + cell.isLayerObject2Flip());
				System.out.println("isLayerObject2Inter = " + cell.isLayerObject2Interactive());
				System.out.println("lineOfSight = " + cell.isLineOfSight());
				System.out.println("LayerObject2Num = " + cell.getLayerObject2Num());
				Executors.FIXED.execute(() -> getPerso().getNavigation().moveToCell(a));
			}));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(GameMovementPacket gameMovementPacket) {
		if (gameMovementPacket.getType() == GameMovementType.REMOVE) {
			gameMovementPacket.getActors().forEach(v -> getPerso().getDebugView().removeActor(((MovementRemoveActor) ((Object) v.getSecond())).getId()));
			return;
		}
		gameMovementPacket.getActors().forEach(e -> {
			switch (e.getFirst()) {
				case DEFAULT:
					MovementCreatePlayer player = (MovementCreatePlayer) (Object) e.getSecond();
					if (player.getId() == getPerso().getId()) ((NavigationImpl) getPerso().getNavigation()).setCurrentPos(player.getCell(), true);
					else getPerso().getDebugView().addPlayer(player.getId(), player.getCell());
					return;
				case CREATE_INVOCATION:
					MovementCreateInvocation invoc = (MovementCreateInvocation) (Object) e.getSecond();
					getPerso().getDebugView().addMob(invoc.getId(), invoc.getCellId());
					return;
				case CREATE_MONSTER:
					MovementCreateMonster mob = (MovementCreateMonster) (Object) e.getSecond();
					getPerso().getDebugView().addMob(mob.getId(), mob.getCellId());
					return;
				case CREATE_MONSTER_GROUP:
					MovementCreateMonsterGroup mobs = (MovementCreateMonsterGroup) (Object) e.getSecond();
					getPerso().getDebugView().addMob(mobs.getId(), mobs.getCellid());
					return;
				case CREATE_NPC:
					// TODO
					return;
				default:
					break;
			}
		});
	}

	@Override
	public void handle(GameServerActionPacket pkt) {
		System.out.println(pkt.getEntityId());
		System.out.println(getPerso().getId());
		if (pkt.getEntityId() != getPerso().getId())
			return;
		if (pkt.getAction() instanceof GameMoveAction) {
			GameMoveAction a = (GameMoveAction) pkt.getAction();
			int id = 0;
			for (Map.Entry<Integer, PathDirection> e : a.getPath().entrySet())
				id = e.getKey();
			((NavigationImpl) getPerso().getNavigation()).setCurrentPos(id);

		}
		transmit(pkt);
	}

}
