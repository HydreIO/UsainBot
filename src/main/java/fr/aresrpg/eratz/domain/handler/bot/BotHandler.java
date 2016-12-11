package fr.aresrpg.eratz.domain.handler.bot;

import fr.aresrpg.dofus.protocol.*;
import fr.aresrpg.dofus.protocol.account.AccountKeyPacket;
import fr.aresrpg.dofus.protocol.account.AccountRegionalVersionPacket;
import fr.aresrpg.dofus.protocol.account.client.*;
import fr.aresrpg.dofus.protocol.account.server.*;
import fr.aresrpg.dofus.protocol.basic.server.BasicConfirmPacket;
import fr.aresrpg.dofus.protocol.chat.ChatSubscribeChannelPacket;
import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction;
import fr.aresrpg.dofus.protocol.game.client.*;
import fr.aresrpg.dofus.protocol.game.movement.MovementCreatePlayer;
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
import fr.aresrpg.dofus.structures.PathDirection;
import fr.aresrpg.dofus.structures.character.AvailableCharacter;
import fr.aresrpg.dofus.structures.game.GameMovementAction;
import fr.aresrpg.dofus.structures.game.GameMovementType;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.structures.server.ServerState;
import fr.aresrpg.dofus.util.*;
import fr.aresrpg.eratz.domain.ability.move.NavigationImpl;
import fr.aresrpg.eratz.domain.handler.bot.craft.CraftHandler;
import fr.aresrpg.eratz.domain.handler.bot.fight.FightHandler;
import fr.aresrpg.eratz.domain.handler.bot.move.MapHandler;
import fr.aresrpg.eratz.domain.handler.bot.move.PlayerMapHandler;
import fr.aresrpg.eratz.domain.handler.proxy.ProxyHandler;
import fr.aresrpg.eratz.domain.player.Perso;
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
public class BotHandler implements PacketHandler {

	private Perso perso;
	private FightHandler fightHandler;
	private CraftHandler craftHandler;
	private MapHandler mapHandler;
	private String ticket;

	public BotHandler(Perso perso) {
		this.perso = perso;
		this.mapHandler = new PlayerMapHandler(perso);
	}

	@Override
	public boolean parse(ProtocolRegistry registry, String packet) {
		// if (registry == null) return true;
		// throw new UnsupportedOperationException();
		return true; // temp
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
	public void handle(AccountAuthPacket accountAuthPacket) {
	}

	@Override
	public void handle(AccountLoginErrPacket accountLoginErrPacket) {
		throw new IllegalStateException("Can't login " + accountLoginErrPacket.getErr());
	}

	@Override
	public void handle(AccountLoginOkPacket accountLoginOkPacket) {
	}

	@Override
	public void handle(AccountPseudoPacket accountPseudoPacket) {
	}

	@Override
	public void handle(AccountCommunityPacket accountCommunityPacket) {
	}

	@Override
	public void handle(AccountHostPacket accountHostPacket) {
		if (accountHostPacket.getServers()[0].getState() != ServerState.ONLINE)
			throw new IllegalStateException("Server not online");
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
	public void handle(AccountListServersPacket accountListServersPacket) {
	}

	@Override
	public void handle(AccountServerListPacket accountServerListPacket) {
		try {
			getConnection().send(new AccountAccessServerPacket().setServerId(601));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(AccountAccessServerPacket accountAccessServerPacket) {
	}

	@Override
	public void handle(AccountServerEncryptedHostPacket accountServerEncryptedHostPacket) {
		this.ticket = accountServerEncryptedHostPacket.getTicketKey();
		try {
			getConnection().close();
			getPerso().getAccount().setRemoteConnection(
					new DofusConnection<>(getPerso().getPseudo(), SocketChannel.open(new InetSocketAddress(accountServerEncryptedHostPacket.getIp(), 443)), new ProxyHandler(this),
							ProtocolRegistry.Bound.SERVER));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(AccountServerHostPacket accountServerHostPacket) {
	}

	@Override
	public void handle(AccountTicketPacket accountTicketPacket) {
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
	public void handle(BasicConfirmPacket basicConfirmPacket) {
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
			getConnection().send(new AccountIdentity().setIdentity(Crypt.getRandomNetworkKey()));
			getConnection().send(new AccountGetCharactersPacket());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(AccountGetGiftsPacket accountGetGiftsPacket) {
	}

	@Override
	public void handle(AccountIdentity accountIdentity) {
	}

	@Override
	public void handle(AccountGetCharactersPacket accountGetCharactersPacket) {
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
	public void handle(AccountSelectCharacterPacket accountSelectCharacterPacket) {
	}

	@Override
	public void handle(AccountGetQueuePosition accountGetQueuePosition) {
	}

	@Override
	public void handle(AccountQueuePosition accountQueuePosition) {
	}

	@Override
	public void handle(MountXpPacket mountXpPacket) {
	}

	@Override
	public void handle(GameExtraInformationPacket gameExtraInformationPacket) {
	}

	@Override
	public void handle(InfoMessagePacket infoMessagePacket) {
		if (infoMessagePacket.getMessageId() == 153) {
			try {
				getConnection().send(new GameCreatePacket().setGameType(1));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
		try {
			Map<String, Object> d = SwfVariableExtractor.extractVariable(Maps.downloadMap(gameMapDataPacket.getMapId(),
					gameMapDataPacket.getSubid()));
			DofusMap m = Maps.loadMap(d, gameMapDataPacket.getDecryptKey());
			getPerso().getDebugView().setMap(m);
			mapHandler.onJoinMap(m);
			((NavigationImpl) getPerso().getNavigation()).setMap(m);
			getConnection().send(new GameExtraInformationPacket());
			getPerso().getDebugView().setOnCellClick(a -> Executors.FIXED.execute(() -> getPerso().getNavigation().moveToCell(a)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handle(GameMovementPacket gameMovementPacket) {
		if (gameMovementPacket.getType() == GameMovementType.REMOVE) return;
		gameMovementPacket.getActors().entrySet().stream().filter(en -> en.getKey() == GameMovementAction.DEFAULT).forEach(e -> {
			MovementCreatePlayer player = (MovementCreatePlayer) e.getValue();
			if (player.getId() == getPerso().getId()) ((NavigationImpl) getPerso().getNavigation()).setCurrentPos(player.getCell(), true);
		});
	}

	@Override
	public void handle(PlayerMountPacket playerMountPacket) {
		// TODO

	}

	@Override
	public void handle(GameJoinPacket gameJoinPacket) {
		// TODO

	}

	@Override
	public void handle(GameEndTurnPacket gameEndTurnPacket) {
		// TODO

	}

	@Override
	public void handle(GameTurnOkPacket gameTurnOkPacket) {
		// TODO

	}

	@Override
	public void handle(FreeMySoulPacket freeMySoulPacket) {
		// TODO

	}

	@Override
	public void handle(LeaveGamePacket leaveGamePacket) {
		// TODO

	}

	@Override
	public void handle(GameSetPlayerPositionPacket gameSetPlayerPositionPacket) {
		// TODO

	}

	@Override
	public void handle(GamePositionStartPacket gamePositionStartPacket) {
		// TODO

	}

	@Override
	public void handle(GameOnReadyPacket gameOnReadyPacket) {
		// TODO

	}

	@Override
	public void handle(GameStartPacket gameStartPacket) {
		// TODO

	}

	@Override
	public void handle(GameEndPacket gameEndPacket) {
		// TODO

	}

	@Override
	public void handle(AccountSelectCharacterOkPacket accountSelectCharacterOkPacket) {
		// TODO
	}

	@Override
	public void handle(ChatSubscribeChannelPacket chatSubscribeChannelPacket) {
		// TODO
	}

	@Override
	public void handle(GameMapFramePacket gameMapFramePacket) {
		System.out.println(gameMapFramePacket);
	}

	@Override
	public void handle(GameActionACKPacket gameActionACKPacket) {

	}

	@Override
	public void handle(GameClientActionPacket gameClientActionPacket) {

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
	}

	@Override
	public void handle(GuildStatPacket guildStatPacket) {
		// TODO

	}

	@Override
	public void handle(SpellChangeOptionPacket spellChangeOptionPacket) {
		// TODO

	}

	@Override
	public void handle(SpellListPacket spellListPacket) {
		// TODO

	}

	@Override
	public void handle(SubareaListPacket subareaListPacket) {
		// TODO

	}

	@Override
	public void handle(AccountRestrictionsPacket accountRestrictionsPacket) {
		// TODO

	}

	@Override
	public void handle(GamePositionsPacket gamePositionPacket) {
		// TODO

	}

	@Override
	public void handle(GameClientReadyPacket gameReadyPacket) {
		// TODO

	}

	@Override
	public void handle(GameServerReadyPacket gameServerReadyPacket) {
		// TODO

	}

	@Override
	public void handle(GameStartToPlayPacket gameStartToPlayPacket) {
		// TODO

	}

	@Override
	public void handle(GameTurnListPacket gameTurnListPacket) {
		// TODO

	}

	@Override
	public void handle(GameTurnEndPacket gameTurnEndPacket) {
		// TODO

	}

	@Override
	public void handle(GameTurnMiddlePacket gameTurnMiddlePacket) {
		// TODO

	}

	@Override
	public void handle(GameTurnStartPacket gameTurnStartPacket) {
		// TODO

	}

	@Override
	public void handle(GameTurnFinishPacket gameTurnFinishPacket) {
		// TODO

	}

	@Override
	public void handle(GameTurnReadyPacket gameTurnReadyPacket) {
		// TODO

	}

	@Override
	public void handle(GameActionFinishPacket gameActionFinishPacket) {
		// TODO

	}

	@Override
	public void handle(GameEffectPacket gameEffectPacket) {
		// TODO

	}

}
