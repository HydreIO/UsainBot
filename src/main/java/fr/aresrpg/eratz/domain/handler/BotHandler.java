package fr.aresrpg.eratz.domain.handler;

import fr.aresrpg.dofus.protocol.DofusConnection;
import fr.aresrpg.dofus.protocol.account.AccountKeyPacket;
import fr.aresrpg.dofus.protocol.account.AccountRegionalVersionPacket;
import fr.aresrpg.dofus.protocol.account.client.*;
import fr.aresrpg.dofus.protocol.account.server.*;
import fr.aresrpg.dofus.protocol.basic.server.BasicConfirmPacket;
import fr.aresrpg.dofus.protocol.game.client.GameCreatePacket;
import fr.aresrpg.dofus.protocol.game.client.GameExtraInformationPacket;
import fr.aresrpg.dofus.protocol.game.server.GameMapDataPacket;
import fr.aresrpg.dofus.protocol.hello.client.HelloGamePacket;
import fr.aresrpg.dofus.protocol.hello.server.HelloConnectionPacket;
import fr.aresrpg.dofus.protocol.info.client.InfoMapPacket;
import fr.aresrpg.dofus.protocol.info.server.message.InfoMessagePacket;
import fr.aresrpg.dofus.protocol.mount.server.MountXpPacket;
import fr.aresrpg.dofus.protocol.specialization.server.SpecializationSetPacket;
import fr.aresrpg.eratz.domain.behavior.fight.FightHandler;
import fr.aresrpg.eratz.domain.player.Account;

/**
 * 
 * @since
 */
public class BotHandler extends BaseHandler {
	
	private FightHandler fightHandler;
	
	/**
	 * @param account
	 */
	public BotHandler(Account account) {
		super(account);
	}

	@Override
	public void register(DofusConnection<?> connection) {
		// TODO

	}

	@Override
	public void handle(HelloGamePacket helloGamePacket) {
		// TODO

	}

	@Override
	public void handle(HelloConnectionPacket helloConnectionPacket) {
		// TODO

	}

	@Override
	public void handle(AccountAuthPacket accountAuthPacket) {
		// TODO

	}

	@Override
	public void handle(AccountLoginErrPacket accountLoginErrPacket) {
		// TODO

	}

	@Override
	public void handle(AccountLoginOkPacket accountLoginOkPacket) {
		// TODO

	}

	@Override
	public void handle(AccountPseudoPacket accountPseudoPacket) {
		// TODO

	}

	@Override
	public void handle(AccountCommunityPacket accountCommunityPacket) {
		// TODO

	}

	@Override
	public void handle(AccountHostPacket accountHostPacket) {
		// TODO

	}

	@Override
	public void handle(AccountQuestionPacket accountQuestionPacket) {
		// TODO

	}

	@Override
	public void handle(AccountListServersPacket accountListServersPacket) {
		// TODO

	}

	@Override
	public void handle(AccountServerListPacket accountServerListPacket) {
		// TODO

	}

	@Override
	public void handle(AccountAccessServerPacket accountAccessServerPacket) {
		// TODO

	}

	@Override
	public void handle(AccountServerEncryptedHostPacket accountServerEncryptedHostPacket) {
		// TODO

	}

	@Override
	public void handle(AccountServerHostPacket accountServerHostPacket) {
		// TODO

	}

	@Override
	public void handle(AccountTicketPacket accountTicketPacket) {
		// TODO

	}

	@Override
	public void handle(AccountTicketOkPacket accountTicketOkPacket) {
		// TODO

	}

	@Override
	public void handle(BasicConfirmPacket basicConfirmPacket) {
		// TODO

	}

	@Override
	public void handle(AccountKeyPacket accountKeyPacket) {
		// TODO

	}

	@Override
	public void handle(AccountRegionalVersionPacket accountRegionalVersionPacket) {
		// TODO

	}

	@Override
	public void handle(AccountGetGiftsPacket accountGetGiftsPacket) {
		// TODO

	}

	@Override
	public void handle(AccountIdentity accountIdentity) {
		// TODO

	}

	@Override
	public void handle(AccountGetCharactersPacket accountGetCharactersPacket) {
		// TODO

	}

	@Override
	public void handle(AccountCharactersListPacket accountCharactersListPacket) {
		// TODO

	}

	@Override
	public void handle(AccountSelectCharacterPacket accountSelectCharacterPacket) {
		// TODO

	}

	@Override
	public void handle(AccountGetQueuePosition accountGetQueuePosition) {
		// TODO

	}

	@Override
	public void handle(AccountQueuePosition accountQueuePosition) {
		// TODO

	}

	@Override
	public void handle(MountXpPacket mountXpPacket) {
		// TODO

	}

	@Override
	public void handle(GameExtraInformationPacket gameExtraInformationPacket) {
		// TODO

	}

	@Override
	public void handle(InfoMessagePacket infoMessagePacket) {
		// TODO

	}

	@Override
	public void handle(SpecializationSetPacket specializationSetPacket) {
		// TODO

	}

	@Override
	public void handle(InfoMapPacket infoMapPacket) {
		// TODO

	}

	@Override
	public void handle(GameCreatePacket gameCreatePacket) {
		// TODO

	}

	@Override
	public void handle(GameMapDataPacket gameMapDataPacket) {
		// TODO

	}

}