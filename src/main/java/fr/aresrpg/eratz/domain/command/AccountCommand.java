package fr.aresrpg.eratz.domain.command;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.dofus.util.DofusMapView;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.gui.MapView;
import fr.aresrpg.eratz.domain.util.BotConfig;
import fr.aresrpg.tofumanchou.domain.Accounts;
import fr.aresrpg.tofumanchou.domain.command.Command;
import fr.aresrpg.tofumanchou.domain.data.Account;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;
import fr.aresrpg.tofumanchou.domain.util.Validators;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.paint.Color;

/**
 * 
 * @since
 */
public class AccountCommand implements Command {

	@Override
	public String getCmd() {
		return "account";
	}

	@Override
	public void trigger(String[] args) {
		if (args.length != 0) {
			switch (args[0]) {
				case "pktclient":
					if (args.length < 3) break;
					Perso perss = Accounts.getPersoWithPseudo(args[1], Server.valueOf(args[2].toUpperCase()));
					if (perss == null) {
						LOGGER.info("Player not found");
						return;
					}
					BotPerso bdsp = BotFather.getPerso(perss);
					StringBuilder sb = new StringBuilder();
					for (int i = 2; i < args.length; i++)
						sb.append(args[i]);
					try {
						((SocketChannel) bdsp.getPerso().getAccount().getProxy().getLocalConnection().getChannel()).write(ByteBuffer.wrap((args[3] + "\0\n").getBytes()));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				case "pkt":
					if (args.length < 3) break;
					Perso pers = Accounts.getPersoWithPseudo(args[1], Server.valueOf(args[2].toUpperCase()));
					if (pers == null) {
						LOGGER.info("Player not found");
						return;
					}
					BotPerso bdp = BotFather.getPerso(pers);
					try {
						((SocketChannel) bdp.getPerso().getAccount().getConnection().getChannel()).write(ByteBuffer.wrap((args[3] + "\0\n").getBytes()));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				case "disconnect":
					if (args.length < 3) break;
					Perso persso = Accounts.getPersoWithPseudo(args[1], Server.valueOf(args[2].toUpperCase()));
					if (persso == null) {
						LOGGER.info("Player not found");
						return;
					}
					LOGGER.info("Disconnecting " + persso.getPseudo());
					BotPerso sbp = BotFather.getPerso(persso);
					sbp.getPerso().disconnect();
					return;
				case "test":
					if (args.length < 3) break;
					Perso persot = Accounts.getPersoWithPseudo(args[1], Server.valueOf(args[2].toUpperCase()));
					if (persot == null) {
						LOGGER.info("Player not found");
						return;
					}
					BotPerso botp = BotFather.getPerso(persot);
					botp.getPerso().moveToRandomCell();
					//Threads.uSleep(2, TimeUnit.SECONDS);
					botp.getPerso().moveToCell(439, true, false);
					return;
				case "view":
					if (args.length < 3) break;
					Perso perso = Accounts.getPersoWithPseudo(args[1], Server.valueOf(args[2].toUpperCase()));
					if (perso == null) {
						LOGGER.info("Player not found");
						return;
					}
					LOGGER.info("Showing view for " + perso);
					BotPerso bp = BotFather.getPerso(perso);
					bp.setView(new DofusMapView());
					bp.getView().setMap(bp.getPerso().getMap().serialize());
					bp.getView().setCurrentPosition(bp.getPerso().getCellId());
					bp.getView().setOnCellClick(i -> {
						List<Node> cellPath = Pathfinding.getCellPath(bp.getPerso().getCellId(), i, bp.getPerso().getMap().getProtocolCells(), perso.getMap().getWidth(), perso.getMap().getHeight(),
								Pathfinding::getNeighbors,
								Validators.avoidingMobs(bp.getPerso().getMap()));
						if (cellPath == null) LOGGER.debug("Path not found !");
						else bp.getView().setPath(cellPath, Color.DARKRED);
					});
					MapView.getInstance().startView(bp.getView(), perso.getMap().getInfos());
					return;
				case "list":
				case "List":
				case "LIST":
					LOGGER.info(Accounts.getInstance().getAccounts().values().stream().map(Account::getAccountName).collect(Collectors.joining(", ")));
					return;
				case "stopspeak":
					BotConfig.AUTO_SPEAK = !BotConfig.AUTO_SPEAK;
					LOGGER.success("Autospeak " + (BotConfig.AUTO_SPEAK ? "Enabled" : "Disabled"));
					return;
				case "connect":
				case "Connect":
				case "CONNECT":
					if (args.length < 3) break;
					Perso persoe = Accounts.getPersoWithPseudo(args[1], Server.valueOf(args[2].toUpperCase()));
					if (persoe == null) {
						LOGGER.info("Player not found");
						return;
					}
					LOGGER.info("Selecting " + persoe);
					persoe.connect();
					return;
				default:
					return;
			}
		}
		LOGGER.error("Usage: account <list | connect <pseudo> <server>>");
	}

}
