/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.io.handler.proxy;

import fr.aresrpg.dofus.protocol.*;
import fr.aresrpg.dofus.protocol.ProtocolRegistry.Bound;
import fr.aresrpg.dofus.protocol.account.AccountKeyPacket;
import fr.aresrpg.dofus.protocol.account.AccountRegionalVersionPacket;
import fr.aresrpg.dofus.protocol.account.server.*;
import fr.aresrpg.dofus.protocol.aks.Aks0MessagePacket;
import fr.aresrpg.dofus.protocol.basic.server.BasicConfirmPacket;
import fr.aresrpg.dofus.protocol.chat.ChatSubscribeChannelPacket;
import fr.aresrpg.dofus.protocol.chat.server.ChatMessageOkPacket;
import fr.aresrpg.dofus.protocol.dialog.DialogLeavePacket;
import fr.aresrpg.dofus.protocol.dialog.server.*;
import fr.aresrpg.dofus.protocol.exchange.server.*;
import fr.aresrpg.dofus.protocol.fight.server.*;
import fr.aresrpg.dofus.protocol.friend.server.FriendListPacket;
import fr.aresrpg.dofus.protocol.game.actions.GameActions;
import fr.aresrpg.dofus.protocol.game.actions.server.GameHarvestTimeAction;
import fr.aresrpg.dofus.protocol.game.server.*;
import fr.aresrpg.dofus.protocol.guild.server.GuildStatPacket;
import fr.aresrpg.dofus.protocol.hello.server.HelloConnectionPacket;
import fr.aresrpg.dofus.protocol.hello.server.HelloGamePacket;
import fr.aresrpg.dofus.protocol.info.server.*;
import fr.aresrpg.dofus.protocol.item.server.*;
import fr.aresrpg.dofus.protocol.job.server.*;
import fr.aresrpg.dofus.protocol.mount.server.MountXpPacket;
import fr.aresrpg.dofus.protocol.party.PartyRefusePacket;
import fr.aresrpg.dofus.protocol.party.server.*;
import fr.aresrpg.dofus.protocol.specialization.server.SpecializationSetPacket;
import fr.aresrpg.dofus.protocol.spell.server.SpellChangeOptionPacket;
import fr.aresrpg.dofus.protocol.spell.server.SpellListPacket;
import fr.aresrpg.dofus.protocol.subarea.server.SubareaListPacket;
import fr.aresrpg.dofus.protocol.waypoint.ZaapLeavePacket;
import fr.aresrpg.dofus.protocol.waypoint.server.ZaapCreatePacket;
import fr.aresrpg.dofus.protocol.waypoint.server.ZaapUseErrorPacket;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.eratz.domain.TheBotFather;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.state.AccountState;
import fr.aresrpg.eratz.domain.data.player.state.PlayerState;
import fr.aresrpg.eratz.domain.io.handler.BaseServerPacketHandler;
import fr.aresrpg.eratz.domain.io.proxy.Proxy;
import fr.aresrpg.eratz.domain.io.proxy.Proxy.ProxyConnectionType;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;
import fr.aresrpg.eratz.domain.util.config.Variables;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class RemoteHandler extends BaseServerPacketHandler {

	static final ProtocolRegistry[] toSkip = {};
	private Proxy proxy;

	/**
	 * @param proxy
	 */
	public RemoteHandler(Proxy proxy) {
		super(null);
		this.proxy = proxy;
	}

	/**
	 * @return the proxy
	 */
	public Proxy getProxy() {
		return proxy;
	}

	protected void transmit(Packet pkt) {
		try {
			getProxy().getLocalConnection().send(pkt);
		} catch (IOException e) {
			e.printStackTrace();
			TheBotFather.LOGGER.error("Client disconnected");
			getPerso().getAccount().getCurrentPlayed().shutdown();
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
				System.out.println("[RECEIVE direct] " + packet);
				((SocketChannel) getProxy().getLocalConnection().getChannel()).write(ByteBuffer.wrap(packet.getBytes()));
			} catch (IOException e) {
				e.printStackTrace();
				TheBotFather.LOGGER.error("Client disconnected");
				getPerso().getAccount().getCurrentPlayed().shutdown();
			}
			return true;
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public void handle(AccountSelectCharacterOkPacket pkt) {
		getAccount().getPersos().stream()
				.filter(p -> p.getId() == pkt.getCharacter().getId())
				.forEach(getAccount()::setCurrentPlayed);
		setPerso(getAccount().getCurrentPlayed());
		getProxy().getLocalHandler().setPerso(getPerso());
		super.handle(pkt); // on handle apres car on a besoin du perso
		transmit(pkt);
		getPerso().getAccount().notifyMitmOnline();
	}

	@Override
	public void handle(ChatSubscribeChannelPacket chatSubscribeChannelPacket) {
		super.handle(chatSubscribeChannelPacket);
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
		super.handle(pkt);
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
			getProxy().getLocalConnection().send(new AccountServerHostPacket().setIp(Variables.IP_MACHINE).setPort(localPort).setTicketKey(pkt.getTicketKey()));
			getProxy().changeConnection(new DofusConnection<>("Client", srvchannel.accept(), getProxy().getLocalHandler(), Bound.CLIENT),
					ProxyConnectionType.LOCAL);
			getProxy().changeConnection(
					new DofusConnection<>("Server", SocketChannel.open(new InetSocketAddress(ip, pkt.getPort())), getProxy().getRemoteHandler(), Bound.SERVER),
					ProxyConnectionType.REMOTE);
			getAccount().setState(AccountState.CLIENT_IN_GAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(AccountCharactersListPacket pkt) { // tkt on verifie bien que les persos existe pas déja
		super.handle(pkt);
		Arrays.stream(pkt.getCharacters()).filter(Objects::nonNull).forEach(c -> getAccount().getPersos().add(new Perso(c.getId(), c.getPseudo(), getAccount(), null, null, null)));
		transmit(pkt);
	}

	public Perso getPerso() {
		if (getAccount() == null) return null;
		return getAccount().getCurrentPlayed();
	}

	@Override
	public void handle(GameMapDataPacket pkt) {
		super.handle(pkt);
		transmit(pkt);
	}

	@Override
	public void handle(GameServerActionPacket pkt) {
		super.handle(pkt);
		if (pkt.getType() == GameActions.HARVEST_TIME) {
			if (pkt.getEntityId() != getPerso().getId()) {
				getPerso().setState(PlayerState.IDLE);
				getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
			} else {
				GameHarvestTimeAction actionh = (GameHarvestTimeAction) pkt.getAction();
				Executors.SCHEDULED.schedule(() -> getPerso().setState(PlayerState.IDLE), actionh.getTime(), TimeUnit.MILLISECONDS);
			}
		}
		transmit(pkt);
	}

	@Override
	public void handle(AccountKeyPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountRegionalVersionPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ChatMessageOkPacket pkt) {
		super.handle(pkt);
		transmit(pkt);
	}

	@Override
	public void handle(FriendListPacket pkt) {
		super.handle(pkt);
		transmit(pkt);
	}

	@Override
	public void handle(ZaapLeavePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(PartyRefusePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountCommunityPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountHostPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountLoginErrPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountPseudoPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountQuestionPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountQueuePosition pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountRestrictionsPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountServerHostPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountServerListPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountTicketOkPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountTicketPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeCreatePacket pkt) {
		super.handle(pkt);
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeListPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(BasicConfirmPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GuildStatPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(InfoMessagePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(MountXpPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(SpecializationSetPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(SpellChangeOptionPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(SpellListPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(SubareaListPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ZaapCreatePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ZaapUseErrorPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameActionFinishPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameActionStartPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameEffectPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameEndPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameFightChallengePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameJoinPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameMapFramePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameMovementPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GamePositionsPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GamePositionStartPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameServerReadyPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameStartToPlayPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameTurnFinishPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameTurnListPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameTurnMiddlePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameTurnReadyPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameTurnStartPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeRequestOkPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(DialogCreateOkPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(DialogQuestionPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(DialogPausePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeReadyPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ItemAddOkPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ItemAddErrorPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ItemDropErrorPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ItemRemovePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ItemQuantityUpdatePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ItemMovementConfirmPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ItemToolPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ItemWeightPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountStatsPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountNewLevelPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(AccountServerQueuePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(HelloGamePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeCraftPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeLocalMovePacket pkt) {
		super.handle(pkt);
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeDistantMovePacket pkt) {
		super.handle(pkt);
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeCoopMovePacket pkt) {
		super.handle(pkt);
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeStorageMovePacket pkt) {
		super.handle(pkt);
		transmit(pkt);
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void handle(ExchangeShopMovePacket pkt) {
		super.handle(pkt);
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeCraftPublicPacket pkt) {
		super.handle(pkt);
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeSellToNpcResultPacket pkt) {
		super.handle(pkt);
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeBuyToNpcResultPacket pkt) {
		super.handle(pkt);
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeCraftLoopPacket pkt) {
		super.handle(pkt);
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeCraftLoopEndPacket pkt) {
		super.handle(pkt);
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
		transmit(pkt);

	}

	@Override
	public void handle(ExchangeLeaveResultPacket pkt) {
		super.handle(pkt);
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
		transmit(pkt);

	}

	@Override
	public void handle(PartyInviteRequestOkPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(PartyInviteRequestErrorPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(PartyLeaderPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(PartyCreateOkPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(PartyCreateErrorPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(PartyPlayerLeavePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(PartyFollowReceivePacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(PartyMovementPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameTeamPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(JobSkillsPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(JobXpPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(JobLevelPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(GameSpawnPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(FightCountPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(FightListPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(FightDetailsPacket pkt) {
		super.handle(pkt);
		transmit(pkt);

	}

	@Override
	public void handle(InfoCompassPacket pkt) {
		super.handle(pkt);
		transmit(pkt);
	}

	@Override
	public void handle(InfoCoordinatePacket pkt) {
		super.handle(pkt);
		transmit(pkt);
	}

	@Override
	public void handle(Aks0MessagePacket pkt) {
		super.handle(pkt);
		transmit(pkt);
	}

	@Override
	public void handle(DialogLeavePacket pkt) {
		super.handle(pkt);
		transmit(pkt);
	}

}
