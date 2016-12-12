package fr.aresrpg.eratz.domain.handler.bot;

import fr.aresrpg.dofus.protocol.*;
import fr.aresrpg.dofus.protocol.account.AccountKeyPacket;
import fr.aresrpg.dofus.protocol.account.AccountRegionalVersionPacket;
import fr.aresrpg.dofus.protocol.account.client.*;
import fr.aresrpg.dofus.protocol.account.server.*;
import fr.aresrpg.dofus.protocol.basic.server.BasicConfirmPacket;
import fr.aresrpg.dofus.protocol.chat.ChatSubscribeChannelPacket;
import fr.aresrpg.dofus.protocol.chat.client.ChatUseSmileyPacket;
import fr.aresrpg.dofus.protocol.emote.client.EmoteUsePacket;
import fr.aresrpg.dofus.protocol.game.actions.GameMoveAction;
import fr.aresrpg.dofus.protocol.game.client.*;
import fr.aresrpg.dofus.protocol.game.movement.*;
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
import fr.aresrpg.dofus.protocol.waypoint.WaypointLeavePacket;
import fr.aresrpg.dofus.protocol.waypoint.client.WaypointUsePacket;
import fr.aresrpg.dofus.protocol.waypoint.server.WaypointCreatePacket;
import fr.aresrpg.dofus.protocol.waypoint.server.WaypointUseErrorPacket;
import fr.aresrpg.dofus.structures.PathDirection;
import fr.aresrpg.dofus.structures.character.AvailableCharacter;
import fr.aresrpg.dofus.structures.game.GameMovementType;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.structures.map.DofusMap;
import fr.aresrpg.dofus.structures.server.*;
import fr.aresrpg.dofus.util.*;
import fr.aresrpg.eratz.domain.ability.move.NavigationImpl;
import fr.aresrpg.eratz.domain.dofus.fight.Fight;
import fr.aresrpg.eratz.domain.handler.bot.craft.CraftHandler;
import fr.aresrpg.eratz.domain.handler.bot.fight.FightHandler;
import fr.aresrpg.eratz.domain.handler.bot.fight.PlayerFightHandler;
import fr.aresrpg.eratz.domain.handler.bot.move.MapHandler;
import fr.aresrpg.eratz.domain.handler.bot.move.PlayerMapHandler;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

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
public class BotHandler implements PacketHandler {

	private Perso perso;
	private FightHandler fightHandler;
	private CraftHandler craftHandler;
	private MapHandler mapHandler;
	private String ticket;

	public BotHandler(Perso perso) {
		this.perso = perso;
		this.mapHandler = new PlayerMapHandler(perso);
		this.fightHandler = new PlayerFightHandler(perso);
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
	public void handle(AccountListServersPacket accountListServersPacket) {
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
	public void handle(AccountAccessServerPacket accountAccessServerPacket) {
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
				getConnection().send(new GameCreatePacket().setGameType(GameCreatePacket.TYPE_SOLO));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void handle(SpecializationSetPacket specializationSetPacket) {
	}

	@Override
	public void handle(InfoMapPacket infoMapPacket) {
	}

	@Override
	public void handle(GameCreatePacket gameCreatePacket) {
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
		if (gameMovementPacket.getType() == GameMovementType.REMOVE) {
			gameMovementPacket.getActors().forEach(v -> {
				MovementRemoveActor actor = (MovementRemoveActor) (Object) v.getSecond();
				getPerso().getDebugView().removeActor(actor.getId());
				this.mapHandler.onActorLeaveMap(actor.getId());
			});

			return;
		}
		gameMovementPacket.getActors().forEach(e -> {
			switch (e.getFirst()) {
				case DEFAULT:
					MovementPlayer player = (MovementPlayer) (Object) e.getSecond();
					if (player.getId() == getPerso().getId()) ((NavigationImpl) getPerso().getNavigation()).setCurrentPos(player.getCell(), true);
					else getPerso().getDebugView().addPlayer(player.getId(), player.getCell());
					this.mapHandler.onPlayerJoinMap(player);
					return;
				case CREATE_INVOCATION:
					MovementInvocation invoc = (MovementInvocation) (Object) e.getSecond();
					getPerso().getDebugView().addMob(invoc.getId(), invoc.getCellId());
					this.mapHandler.onInvocSpawn(invoc);
					return;
				case CREATE_MONSTER:
					MovementMonster mob = (MovementMonster) (Object) e.getSecond();
					getPerso().getDebugView().addMob(mob.getId(), mob.getCellId());
					this.mapHandler.onMobSpawn(mob);
					return;
				case CREATE_MONSTER_GROUP:
					MovementMonsterGroup mobs = (MovementMonsterGroup) (Object) e.getSecond();
					getPerso().getDebugView().addMob(mobs.getId(), mobs.getCellid());
					this.mapHandler.onMobGroupSpawn(mobs);
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
		Fight fi = getPerso().getCurrentFight();
		if (fi == null) return;
		getPerso().getFightOptions().setCurrentFightTeam(gamePositionStartPacket.getCurrentTeam());
		fi.setPlaceTeam0(Arrays.stream(gamePositionStartPacket.getPlacesTeam0()).mapToObj(id -> getPerso().getCurrentMap().getDofusMap().getCells()[id]).toArray(Cell[]::new));
		fi.setPlaceTeam1(Arrays.stream(gamePositionStartPacket.getPlacesTeam1()).mapToObj(id -> getPerso().getCurrentMap().getDofusMap().getCells()[id]).toArray(Cell[]::new));
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

	@Override
	public void handle(WaypointLeavePacket waypointLeavePacket) {
		// TODO

	}

	@Override
	public void handle(WaypointUseErrorPacket waypointUseErrorPacket) {
		// TODO

	}

	@Override
	public void handle(WaypointCreatePacket waypointCreatePacket) {
		// TODO

	}

	@Override
	public void handle(ChatUseSmileyPacket chatUseSmileyPacket) {
		// TODO

	}

	@Override
	public void handle(WaypointUsePacket waypointUsePacket) {
		// TODO

	}

	@Override
	public void handle(EmoteUsePacket emoteUsePacket) {
		// TODO

	}

}
