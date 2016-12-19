package fr.aresrpg.eratz.domain.io.handler.impl.bot;

import fr.aresrpg.dofus.protocol.DofusConnection;
import fr.aresrpg.dofus.protocol.ProtocolRegistry;
import fr.aresrpg.dofus.protocol.account.AccountKeyPacket;
import fr.aresrpg.dofus.protocol.account.AccountRegionalVersionPacket;
import fr.aresrpg.dofus.protocol.account.client.*;
import fr.aresrpg.dofus.protocol.account.server.*;
import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction;
import fr.aresrpg.dofus.protocol.game.client.GameCreatePacket;
import fr.aresrpg.dofus.protocol.game.client.GameExtraInformationPacket;
import fr.aresrpg.dofus.protocol.game.server.*;
import fr.aresrpg.dofus.protocol.hello.server.HelloConnectionPacket;
import fr.aresrpg.dofus.protocol.hello.server.HelloGamePacket;
import fr.aresrpg.dofus.protocol.info.server.message.InfoMessagePacket;
import fr.aresrpg.dofus.structures.InfosMessage;
import fr.aresrpg.dofus.structures.PathDirection;
import fr.aresrpg.dofus.structures.character.AvailableCharacter;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.structures.server.*;
import fr.aresrpg.dofus.util.*;
import fr.aresrpg.eratz.domain.data.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.io.handler.BaseServerPacketHandler;
import fr.aresrpg.eratz.domain.io.handler.impl.BaseMapHandler;
import fr.aresrpg.eratz.domain.io.handler.impl.PlayerFightHandler;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class BotHandler extends BaseServerPacketHandler {

	private Perso perso;
	private String ticket;

	public BotHandler(Perso perso) {
		this.perso = perso;
		addFightHandlers(new PlayerFightHandler(perso));
		addMapHandlers(new BaseMapHandler(perso));
	}

	@Override
	public boolean parse(ProtocolRegistry registry, String packet) {
		if (registry == null) return true;
		throw new UnsupportedOperationException();
	}

	public Perso getPerso() {
		return perso;
	}

	public DofusConnection<?> getConnection() {
		return getPerso().getAccount().getRemoteConnection();
	}

	@Override
	public void register(DofusConnection<?> connection) {
	}

	@Override
	public void handle(HelloGamePacket helloGamePacket) {
		try {
			getConnection().send(new AccountTicketPacket().setTicket(ticket));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(HelloConnectionPacket helloConnectionPacket) {
		try {
			getConnection().send(new AccountAuthPacket()
					.setPseudo(perso.getAccount().getUsername())
					.setHashedPassword(Crypt.hash(perso.getAccount().getPass(), helloConnectionPacket.getHashKey()))
					.setVersion("1.29.1"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(AccountLoginErrPacket accountLoginErrPacket) {
		throw new IllegalStateException("Can't login " + accountLoginErrPacket.getErr());
	}

	@Override
	public void handle(AccountHostPacket accountHostPacket) {
		for (DofusServer s : accountHostPacket.getServers())
			if (getPerso().getServer().equals(s)) getPerso().getServer().setState(s.getState());
		if (getPerso().getServer().getState() != ServerState.ONLINE)
			getPerso().disconnect(Server.fromId(getPerso().getServer().getId()).name() + " n'est pas en ligne !", 10 * 60);
	}

	@Override
	public void handle(AccountQuestionPacket accountQuestionPacket) {
		Executors.SCHEDULED.schedule(() -> {
			try {
				getConnection().send(new AccountListServersPacket());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} , 2, TimeUnit.SECONDS);
	}

	@Override
	public void handle(AccountServerListPacket accountServerListPacket) {
		try {
			getConnection().send(new AccountAccessServerPacket().setServerId(getPerso().getServer().getId()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(AccountServerEncryptedHostPacket accountServerEncryptedHostPacket) {
		this.ticket = accountServerEncryptedHostPacket.getTicketKey();
		try {
			getConnection().close();
			getPerso().getAccount().setRemoteConnection(
					new DofusConnection<>(getPerso().getPseudo(), SocketChannel.open(new InetSocketAddress(accountServerEncryptedHostPacket.getIp(), 443)), this, ProtocolRegistry.Bound.SERVER));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(AccountTicketOkPacket accountTicketOkPacket) {
		handle((AccountKeyPacket) accountTicketOkPacket);

		try {
			getConnection().send(new AccountRegionalVersionPacket());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(AccountKeyPacket accountKeyPacket) {
		try {
			getConnection().send(new AccountKeyPacket().setKey(accountKeyPacket.getKey()).setData(accountKeyPacket.getData()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(AccountRegionalVersionPacket accountRegionalVersionPacket) {
		try {
			getConnection().send(new AccountGetGiftsPacket().setLanguage("fr"));
			getConnection().send(new AccountIdentityPacket().setIdentity(Crypt.getRandomNetworkKey()));
			getConnection().send(new AccountGetCharactersPacket());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(AccountCharactersListPacket accountCharactersListPacket) {
		for (AvailableCharacter c : accountCharactersListPacket.getCharacters())
			if (c.getPseudo().equals(perso.getPseudo())) {
				try {
					getPerso().setId(c.getId());
					getConnection().send(new AccountSelectCharacterPacket().setCharacterId(c.getId()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
		throw new IllegalStateException("No perso with name " + perso.getPseudo());
	}

	@Override
	public void handle(InfoMessagePacket infoMessagePacket) {
		if (infoMessagePacket.getMessage() == InfosMessage.CURRENT_ADRESS) {
			try {
				getConnection().send(new GameCreatePacket().setGameType(GameCreatePacket.TYPE_SOLO));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void handle(GameMapDataPacket gameMapDataPacket) {
		try {
			Map<String, Object> d = SwfVariableExtractor.extractVariable(Maps.downloadMap(gameMapDataPacket.getMapId(),
					gameMapDataPacket.getSubid()));
			DofusMap m = Maps.loadMap(d, gameMapDataPacket.getDecryptKey());
			getConnection().send(new GameExtraInformationPacket());
			getPerso().getDebugView().setOnCellClick(a -> Executors.FIXED.execute(() -> getPerso().getNavigation().moveToCell(a)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(GamePositionStartPacket gamePositionStartPacket) {
		Fight fi = getPerso().getFightInfos().getCurrentFight();
		if (fi == null) return;
		getPerso().getFightInfos().setCurrentFightTeam(gamePositionStartPacket.getCurrentTeam());
		fi.setPlaceTeam0(gamePositionStartPacket.getPlacesTeam0());
		fi.setPlaceTeam1(gamePositionStartPacket.getPlacesTeam1());
	}

	@Override
	public void handle(GameServerActionPacket pkt) {
		if (pkt.getEntityId() != getPerso().getId())
			return;
		if (pkt.getAction() instanceof GameMoveAction) {
			GameMoveAction a = (GameMoveAction) pkt.getAction();
			int id = 0;
			for (Map.Entry<Integer, PathDirection> e : a.getPath().entrySet())
				id = e.getKey();
			getPerso().getMapInfos().setCellId(id);
		}
	}

}
