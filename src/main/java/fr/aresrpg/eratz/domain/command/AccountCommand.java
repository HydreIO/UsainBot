package fr.aresrpg.eratz.domain.command;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.dofus.protocol.game.server.GameCellUpdatePacket;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.dofus.util.DofusMapView;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.gui.MapView;
import fr.aresrpg.tofumanchou.domain.Accounts;
import fr.aresrpg.tofumanchou.domain.command.Command;
import fr.aresrpg.tofumanchou.domain.data.Account;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;

import java.awt.Point;
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
				case "test":
					if (args.length < 3) break;
					Perso perss = Accounts.getPersoWithPseudo(args[1], Server.valueOf(args[2].toUpperCase()));
					if (perss == null) {
						LOGGER.info("Player not found");
						return;
					}
					BotPerso bdpd = BotFather.getPerso(perss);
					GameCellUpdatePacket cellpkt = new GameCellUpdatePacket();
					Cell cell = bdpd.getPerso().getMap().getProtocolCells()[351];
					Cell clone = cell.clone();
					clone.setLayerObject2Num(798);
					cellpkt.addCell(clone, cell);
					bdpd.sendPacketToClient(cellpkt);
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
						List<Point> cellPath = Pathfinding.getCellPath(bp.getPerso().getCellId(), i, bp.getPerso().getMap().getProtocolCells(), perso.getMap().getWidth(), perso.getMap().getHeight(),
								Pathfinding::getNeighbors,
								bp.getPerso()::canGoOnCellAvoidingMobs);
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
