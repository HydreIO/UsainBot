package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.protocol.*;
import fr.aresrpg.dofus.protocol.account.AccountKeyPacket;
import fr.aresrpg.dofus.protocol.account.AccountRegionalVersionPacket;
import fr.aresrpg.dofus.protocol.account.client.*;
import fr.aresrpg.dofus.protocol.account.server.AccountLoginErrPacket.Error;
import fr.aresrpg.dofus.protocol.account.server.AccountTicketPacket;
import fr.aresrpg.dofus.structures.Community;
import fr.aresrpg.dofus.structures.character.AvailableCharacter;
import fr.aresrpg.dofus.structures.character.Character;
import fr.aresrpg.dofus.structures.server.DofusServer;
import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.dofus.util.Crypt;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.std.aproach.AccountServerHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class BotAccountServerHandler extends BotHandlerAbstract implements AccountServerHandler {

	/**
	 * @param perso
	 */
	public BotAccountServerHandler(Perso perso) {
		super(perso);
	}

	@Override
	public void onHelloConnection(String key) {
		getPerso().sendPacketToServer(new AccountAuthPacket()
				.setPseudo(getPerso().getAccount().getUsername())
				.setHashedPassword(Crypt.hash(getPerso().getAccount().getPass(), key))
				.setVersion("1.29.1"));
	}

	@Override
	public void onHelloServer(String ticket) {
		getPerso().sendPacketToServer(new AccountTicketPacket().setTicket(ticket));
	}

	@Override
	public void onRegion() {
		getPerso().sendPacketToServer(
				new AccountGetGiftsPacket().setLanguage("fr"),
				new AccountIdentityPacket().setIdentity(Crypt.getRandomNetworkKey()),
				new AccountGetCharactersPacket());
	}

	@Override
	public void onCharacterList(long subscription, AvailableCharacter... characters) {
		if (!ArrayUtils.contains(getPerso().getId(), Arrays.stream(characters).mapToInt(AvailableCharacter::getId).toArray())) {
			LOGGER.error("The perso '" + getPerso().getPseudo() + "' doesn't exist !");
			getPerso().disconnect("This perso doesn't exist !");
			return;
		}
		getPerso().sendPacketToServer(new AccountSelectCharacterPacket().setCharacterId(getPerso().getId()));
	}

	@Override
	public void onCommunity(Community commu) {
		// TODO

	}

	@Override
	public void onServers(DofusServer... servers) {
		switch (getPerso().getDofusServer().getState()) {
			case OFFLINE:
				LOGGER.warning(Server.fromId(getPerso().getServer().getId()).name() + " is not online ! Reconnecting in ~2 hours");
				getPerso().disconnect(Server.fromId(getPerso().getServer().getId()).name() + " is not online !");
				getPerso().connectIn(Randoms.nextBetween(100, 140), TimeUnit.MINUTES);
				return;
			case SAVING:
				LOGGER.warning(Server.fromId(getPerso().getServer().getId()).name() + " is in save ! Reconnecting in ~10min");
				getPerso().disconnect(Server.fromId(getPerso().getServer().getId()).name() + " is in save !");
				getPerso().connectIn(Randoms.nextBetween(8, 12), TimeUnit.MINUTES);
				return;
			default:
				return;
		}
	}

	@Override
	public void onLoginError(Error err, int minutes, String version) {
		final String format = "Unable to login ! | " + err;
		final String retry = "| Reconnecting in";
		final TimeUnit unit = TimeUnit.MILLISECONDS;
		long ms;
		switch (err) {
			case ALREADY_LOGGED:
			case ALREADY_LOGGED_GAME_SERVER:
			case CONNECTION_NOT_FINISHED:
			case DISCONNECTED:
				LOGGER.warning(format + retry + " ~20s");
				ms = unit.convert(Randoms.nextBetween(15, 28), TimeUnit.SECONDS);
				break;
			case SERVER_FULL:
				LOGGER.warning(format + retry + " ~10min");
				ms = unit.convert(Randoms.nextBetween(8, 15), TimeUnit.MINUTES);
				break;
			case KICKED:
			case BANNED:
				LOGGER.warning("The bot was banned " + minutes + "min :( ! " + retry + " " + (minutes + 1) + "min");
				ms = unit.convert(Randoms.nextBetween(minutes, minutes + 1), TimeUnit.MINUTES);
				break;
			default:
				LOGGER.error(format);
				return;
		}
		getPerso().shutdown();
		getPerso().connectIn(ms, unit);
	}

	@Override
	public void onLogged(boolean isAdmin) {
		// TODO

	}

	@Override
	public void onPseudo(String pseudo) {
		// TODO

	}

	@Override
	public void onSecretQuestion(String question) {
		getPerso().sendPacketToServer(new AccountListServersPacket(), 2, TimeUnit.SECONDS);
	}

	@Override
	public void onQueueRealmPosition(int position, int totalSub, int totalNoSub, boolean sub, int positionInQueue) {
		// TODO

	}

	@Override
	public void onCharacterSelect(Character p) {
		getPerso().getAccount().notifyBotOnline(); // pour autoriser les actions qui onts besoin que le bot soit bien en jeux
	}

	@Override
	public void onReceiveServerHost(String ip, int port, PacketHandler handler) {
		try {
			getPerso().getAccount().getRemoteConnection().closeConnection();
			getPerso().getAccount().setRemoteConnection(new DofusConnection<>(getPerso().getPseudo(), SocketChannel.open(new InetSocketAddress(ip, port)), handler, ProtocolRegistry.Bound.SERVER));
			getPerso().getAccount().readRemote();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onReceiveServerPersoCount(long subtime, Map<Integer, Integer> srvIdAndCount) {
		getPerso().sendPacketToServer(new AccountAccessServerPacket().setServerId(getPerso().getServer().getId()));
	}

	@Override
	public void onStatsUpdate() {
		// TODO

	}

	@Override
	public void onNewLvl(int lvl) {
		// TODO

	}

	@Override
	public void onServerQueue(int currentPos) {
	}

	@Override
	public void onTicketOk(int key, String data) {
		onKey(key, data);
		getPerso().sendPacketToServer(new AccountRegionalVersionPacket());
	}

	@Override
	public void onKey(int key, String data) {
		getPerso().sendPacketToServer(new AccountKeyPacket().setKey(key).setData(data));
	}

	@Override
	public void onTicket(String ticket) {
		// TODO

	}

}
