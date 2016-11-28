/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.player;

import fr.aresrpg.dofus.protocol.DofusConnection;
import fr.aresrpg.dofus.protocol.ProtocolRegistry.Bound;
import fr.aresrpg.eratz.domain.TheBotFather;
import fr.aresrpg.eratz.domain.handler.bot.BotHandler;
import fr.aresrpg.eratz.domain.player.state.AccountState;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;
import fr.aresrpg.eratz.domain.util.config.Variables;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.time.Instant;

public class Perso extends Player {

	private Account account;

	public Perso(int id, String pseudo, Account account) {
		super(id, pseudo);
		this.account = account;
	}

	public void connect() {
		Account a = getAccount();
		if (a.getLastConnection() + (Variables.SEC_AFTER_CRASH * 1000) > System.currentTimeMillis())
			throw new IllegalAccessError("[ANTI-BAN] Connection refused, please wait at least " + Variables.SEC_AFTER_CRASH + "s before every reconnection.");
		System.out.println("[" + Instant.now().toString() + "] Connecting " + getPseudo());
		if (a.isClientOnline()) throw new IllegalAccessError("The account of " + getPseudo() + " is already online | No need to connect the bot");
		if (a.isBotOnline()) throw new IllegalAccessError("The bot " + a.getCurrentPlayed().getPseudo() + " is already online | you need to deconnect it first");
		a.setState(AccountState.BOT_ONLINE);
		try {
			SocketChannel channel = SocketChannel.open(TheBotFather.SERVER_ADRESS);
			a.setCurrentPlayed(this);
			a.setRemoteConnection(new DofusConnection(getPseudo(), channel, new BotHandler(a), Bound.SERVER));
			Executors.FIXED.execute(a::readRemote);
		} catch (IOException e) {
			a.setState(AccountState.OFFLINE);
			System.out.println("Bot crash."); // test debug
			e.printStackTrace(); // test debug
		}
		a.setLastConnection(System.currentTimeMillis());
	}

	public void disconnect() {
		if (getAccount().isClientOnline()) throw new IllegalAccessError("Unable to disconnect " + getPseudo() + " ! | A client is online");
		System.out.println("Disconnecting " + getPseudo());
		Account a = getAccount();
		try {
			a.getRemoteConnection().close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			a.setState(AccountState.OFFLINE);
			System.out.println(getPseudo() + " disconnected.");
		}

	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

}
