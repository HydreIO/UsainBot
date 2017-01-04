package fr.aresrpg.eratz.domain.command;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.dofus.structures.server.Server;
import fr.aresrpg.eratz.domain.BotFather;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.data.player.state.BotState;
import fr.aresrpg.eratz.domain.listener.HarvestListener;
import fr.aresrpg.eratz.domain.listener.MapViewListener;
import fr.aresrpg.tofumanchou.domain.Accounts;
import fr.aresrpg.tofumanchou.domain.Manchou;
import fr.aresrpg.tofumanchou.domain.command.Command;
import fr.aresrpg.tofumanchou.domain.data.entity.player.Perso;

import java.util.Arrays;

/**
 * 
 * @since
 */
public class BucheronCommand implements Command {

	@Override
	public String getCmd() {
		return "bucheron";
	}

	@Override
	public void trigger(String[] args) {
		System.out.println(Arrays.toString(args));
		if (args.length != 2) {
			LOGGER.error("bucheron <server> <playerName>");
			return;
		}
		Server srv = Server.fromName(args[0]);
		if (srv == null) {
			LOGGER.error("The server '" + args[0] + "' is invalid");
			return;
		}
		Perso p = Accounts.getPersoWithPseudo(args[1], srv);
		if (p == null) {
			LOGGER.error("The perso '" + args[1] + "' is not found");
			return;
		}
		LOGGER.success("Starting bucheron for " + p.getPseudo());
		BotPerso perso = BotFather.getPerso(p);
		BotState st = perso.getBotState();
		st.ressources.add(Interractable.CHATAIGNIER);
		st.ressources.add(Interractable.MERISIER);
		st.ressources.add(Interractable.CHENE);
		st.ressources.add(Interractable.NOYER);
		st.ressources.add(Interractable.CHARME);
		st.ressources.add(Interractable.IF);
		st.ressources.add(Interractable.ERABLE);
		st.ressources.add(Interractable.ORME);
		st.ressources.add(Interractable.EBENE);
		st.addPath(1, 2);
		st.addPath(1, 4);
		st.addPath(4, 4);
		st.addPath(4, 5);
		st.addPath(10, 5);
		st.addPath(10, 9);
		st.addPath(11, 9);
		st.addPath(12, 9);
		st.addPath(4, 7);
		st.addPath(5, 8);
		st.addPath(4, 9);
		st.addPath(0, 8);
		st.addPath(1, 8);
		st.addPath(2, 8);
		st.addPath(3, 8);
		st.addPath(0, 9);
		st.addPath(1, 9);
		st.addPath(2, 9);
		st.addPath(3, 9);
		st.addPath(4, 11);
		st.addPath(5, 11);
		st.addPath(9, 12);
		st.addPath(13, 13);
		st.addPath(13, 14);
		st.addPath(13, 15);
		st.addPath(1, 13);
		st.addPath(14, 19);
		st.addPath(14, 20);
		st.addPath(14, 21);
		st.addPath(13, 20);
		st.addPath(13, 21);
		st.addPath(10, 20);
		st.addPath(9, 21);
		st.addPath(6, 14);
		st.addPath(2, 15);
		st.addPath(5, 15);
		st.addPath(6, 15);
		st.addPath(1, 16);
		st.addPath(2, 16);
		st.addPath(3, 16);
		st.addPath(4, 16);
		st.addPath(5, 16);
		st.addPath(6, 16);
		st.addPath(5, 17);
		st.addPath(6, 17);
		st.addPath(7, 17);
		st.addPath(5, 18);
		st.addPath(6, 18);
		st.addPath(7, 18);
		st.addPath(7, 19);
		st.addPath(7, 20);
		st.addPath(3, 20);
		st.addPath(3, 21);
		st.addPath(4, 21);
		st.addPath(1, 22);
		st.addPath(3, 22);
		st.addPath(4, 22);
		st.addPath(3, 23);
		st.addPath(4, 29);
		st.addPath(5, 29);
		st.addPath(6, 29);
		st.addPath(7, 29);
		st.addPath(8, 29);
		st.addPath(9, 29);
		st.addPath(10, 29);
		st.addPath(0, 30);
		st.addPath(1, 30);
		st.addPath(2, 30);
		st.addPath(3, 30);
		st.addPath(4, 30);
		st.addPath(5, 30);
		st.addPath(6, 30);
		st.addPath(7, 30);
		st.addPath(8, 30);
		st.addPath(9, 30);
		st.addPath(2, 31);
		st.addPath(3, 31);
		st.addPath(4, 31);
		st.addPath(5, 31);
		st.addPath(6, 31);
		st.addPath(7, 31);
		st.addPath(8, 31);
		st.addPath(4, 32);
		st.addPath(1, 32);
		st.addPath(0, 32);
		st.addPath(-1, 32);
		st.addPath(-1, 33);
		st.addPath(0, 33);
		HarvestListener.register();
		try {
			if (!HarvestListener.getInstance().harvestRessource(perso, perso.getPerso().getMap(), perso.getBotState())) HarvestListener.getInstance().goToNextMap(perso, perso.getPerso().getMap(), st);
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

}
