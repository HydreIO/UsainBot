package fr.aresrpg.eratz.domain.handler.proxy;

import fr.aresrpg.dofus.protocol.Packet;
import fr.aresrpg.dofus.protocol.account.AccountKeyPacket;
import fr.aresrpg.dofus.protocol.account.AccountRegionalVersionPacket;
import fr.aresrpg.dofus.protocol.account.client.*;
import fr.aresrpg.dofus.protocol.account.server.*;
import fr.aresrpg.dofus.protocol.basic.server.BasicConfirmPacket;
import fr.aresrpg.dofus.protocol.chat.ChatSubscribeChannelPacket;
import fr.aresrpg.dofus.protocol.game.client.*;
import fr.aresrpg.dofus.protocol.game.server.*;
import fr.aresrpg.dofus.protocol.guild.server.GuildStatPacket;
import fr.aresrpg.dofus.protocol.hello.client.HelloGamePacket;
import fr.aresrpg.dofus.protocol.hello.server.HelloConnectionPacket;
import fr.aresrpg.dofus.protocol.info.client.InfoMapPacket;
import fr.aresrpg.dofus.protocol.info.server.message.InfoMessagePacket;
import fr.aresrpg.dofus.protocol.mount.client.PlayerMountPacket;
import fr.aresrpg.dofus.protocol.mount.server.MountXpPacket;
import fr.aresrpg.dofus.protocol.specialization.server.SpecializationSetPacket;
import fr.aresrpg.dofus.protocol.spell.server.SpellChangeOptionPacket;
import fr.aresrpg.dofus.protocol.spell.server.SpellListPacket;
import fr.aresrpg.dofus.protocol.subarea.server.SubareaListPacket;
import fr.aresrpg.eratz.domain.handler.BaseHandler;
import fr.aresrpg.eratz.domain.proxy.Proxy;

public abstract class TransfertHandler extends BaseHandler {
	public TransfertHandler(Proxy proxy) {
		super(proxy);
	}

	protected abstract void transmit(Packet pkt);

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
		transmit(pkt);
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
	public void handle(AccountPseudoPacket pkt) {
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
	public void handle(PlayerMountPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameJoinPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameEndTurnPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameTurnOkPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(FreeMySoulPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(LeaveGamePacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameSetPlayerPositionPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GamePositionStartPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameOnReadyPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameStartPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameEndPacket pkt) {
		transmit(pkt);
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
	public void handle(GameMovementPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameMapFramePacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameActionACKPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameClientActionPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameServerActionPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(AccountRestrictionsPacket accountRestrictionsPacket) {
		transmit(accountRestrictionsPacket);
	}

	@Override
	public void handle(GameActionFinishPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameClientReadyPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameEffectPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GamePositionsPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameServerReadyPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameStartToPlayPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameTurnEndPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameTurnFinishPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameTurnListPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameTurnMiddlePacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameTurnReadyPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GameTurnStartPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(GuildStatPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(SpellChangeOptionPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(SpellListPacket pkt) {
		transmit(pkt);
	}

	@Override
	public void handle(SubareaListPacket pkt) {
		transmit(pkt);
	}
}