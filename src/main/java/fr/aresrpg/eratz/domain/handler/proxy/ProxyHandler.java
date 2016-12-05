package fr.aresrpg.eratz.domain.handler.proxy;

import fr.aresrpg.dofus.protocol.DofusConnection;
import fr.aresrpg.dofus.protocol.PacketHandler;
import fr.aresrpg.dofus.protocol.ProtocolRegistry;
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

import java.util.*;

/**
 * 
 * @since
 */
public class ProxyHandler implements PacketHandler {

	private Set<PacketHandler> handlers = new HashSet<>();

	public ProxyHandler(PacketHandler... h) {
		Arrays.stream(h).forEach(handlers::add);
	}

	/**
	 * @return the handlers
	 */
	public Set<PacketHandler> getHandlers() {
		return handlers;
	}

	public void removeHandlers(PacketHandler... handlers) {
		Arrays.stream(handlers).forEach(this::removeHandler);
	}

	public void removeHandlers() {
		this.handlers = new HashSet<>();
	}

	public void addHandler(PacketHandler handler) {
		getHandlers().add(handler);
	}

	public void removeHandler(PacketHandler handler) {
		getHandlers().remove(handler);
	}

	@Override
	public void register(DofusConnection<?> connection) {

	}

	@Override
	public void handle(HelloGamePacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(HelloConnectionPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountAuthPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountLoginErrPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountLoginOkPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountPseudoPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountCommunityPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountHostPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountQuestionPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountListServersPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountServerListPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountAccessServerPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountServerEncryptedHostPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountServerHostPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountTicketPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountTicketOkPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(BasicConfirmPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountKeyPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountRegionalVersionPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountGetGiftsPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountIdentity pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountGetCharactersPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountCharactersListPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountSelectCharacterPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountGetQueuePosition pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountQueuePosition pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(MountXpPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(GameExtraInformationPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(InfoMessagePacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(SpecializationSetPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(InfoMapPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(GameCreatePacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(GameMapDataPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(PlayerMountPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(GameJoinPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(GameEndTurnPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(GameTurnOkPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(FreeMySoulPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(LeaveGamePacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(GameSetPlayerPositionPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(GamePositionStartPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(GameOnReadyPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(GameStartPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(GameEndPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(AccountSelectCharacterOkPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public void handle(ChatSubscribeChannelPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}

	@Override
	public boolean parse(ProtocolRegistry registry, String packet) {
		for(PacketHandler handler : handlers)
			if(handler.parse(registry , packet))
				return true;
		return false;
	}

	@Override
	public void handle(GameActionPacket pkt) {
		handlers.forEach(h -> h.handle(pkt));
	}
}
