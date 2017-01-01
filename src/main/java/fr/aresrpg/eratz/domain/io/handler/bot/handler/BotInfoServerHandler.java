package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.dofus.protocol.game.client.GameCreatePacket;
import fr.aresrpg.dofus.protocol.info.server.InfoCoordinatePacket.MovingPlayer;
import fr.aresrpg.dofus.protocol.item.client.ItemDestroyPacket;
import fr.aresrpg.dofus.structures.InfosMessage;
import fr.aresrpg.dofus.structures.InfosMsgType;
import fr.aresrpg.dofus.structures.game.GameType;
import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.eratz.domain.TheBotFather;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.std.info.InfoServerHandler;
import fr.aresrpg.tofumanchou.domain.data.InfosData;
import fr.aresrpg.tofumanchou.domain.data.ItemsData;
import fr.aresrpg.tofumanchou.domain.data.ItemsData.LangItem;
import fr.aresrpg.tofumanchou.domain.util.concurrent.Executors;

import java.awt.Point;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class BotInfoServerHandler extends BotHandlerAbstract implements InfoServerHandler {

	/**
	 * @param perso
	 */
	public BotInfoServerHandler(Perso perso) {
		super(perso);
	}

	@Override
	public void onInfos(InfosMsgType type, int id, String msg) {
		LOGGER.success(InfosData.getMessage(type, id));
		InfosMessage infom = InfosMessage.fromId(type, id);
		if (infom == InfosMessage.CURRENT_ADRESS) getPerso().sendPacketToServer(new GameCreatePacket().setGameType(GameType.SOLO));
		if (type == InfosMsgType.ERROR && id == 12 && getPerso().canDestroyItems()) {
			Item it = getPerso().getInventory().getHeaviestItem();
			if (it == null) throw new NullPointerException("La ressource la plus lourde est introuvable !");
			LangItem lit = ItemsData.get(it.getItemTypeId());
			int maxp = getPerso().getStatsInfos().getMaxPods();
			int pod = getPerso().getStatsInfos().getPods();
			int over = pod + 1 - maxp;
			int poditem = lit.getPod();
			int fullw = poditem * it.getQuantity();
			TheBotFather.LOGGER.debug("Pod en trop = " + over);
			TheBotFather.LOGGER.debug("Poids total = " + fullw);
			if (fullw <= over) {
				TheBotFather.LOGGER.debug("Destruction de x" + it.getQuantity() + " " + lit.getName());
				getPerso().sendPacketToServer(new ItemDestroyPacket(it.getUid(), it.getQuantity()));
			} else {
				int todestroy = over / poditem + (over % poditem == 0 ? 0 : 1);
				TheBotFather.LOGGER.debug("Destruction de x" + it.getQuantity() + " " + lit.getName());
				getPerso().sendPacketToServer(new ItemDestroyPacket(it.getUid(), todestroy));
			}
		}
		if (infom == InfosMessage.PLAYER_IN_SPEC)
			Executors.SCHEDULED.schedule(() -> {
				getPerso().getAbilities().getFightAbility().blockSpec(true);
				Threads.uSleep(5, TimeUnit.SECONDS);
				getPerso().getAbilities().getFightAbility().blockSpec(false);
			} , 1, TimeUnit.SECONDS);
	}

	@Override
	public void onCompass(int x, int y) {
		getPerso().getBotInfos().setFollowedCoords(new Point(x, y));
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onFollowedPlayerMove(MovingPlayer player) {
	}

}
