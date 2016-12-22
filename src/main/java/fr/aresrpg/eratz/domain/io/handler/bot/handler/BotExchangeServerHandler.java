package fr.aresrpg.eratz.domain.io.handler.bot.handler;

import fr.aresrpg.dofus.protocol.exchange.server.ExchangeCreatePacket.ExchangeData;
import fr.aresrpg.dofus.structures.*;
import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbility.BuyResult;
import fr.aresrpg.eratz.domain.std.exchange.ExchangeServerHandler;

import java.util.Collection;

/**
 * 
 * @since
 */
public class BotExchangeServerHandler extends BotHandlerAbstract implements ExchangeServerHandler {

	/**
	 * @param perso
	 */
	public BotExchangeServerHandler(Perso perso) {
		super(perso);
	}

	@Override
	public void onCreate(Exchange type, ExchangeData datas) {
		switch (type) {
			case NPC_SHOP:
				getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
				break;

			default:
				break;
		}
	}

	@Override
	public void onInventoryList(Exchange type, Collection<Item> items, int kamas) {
		// TODO

	}

	@Override
	public void onExchangeRequest(int targetId, Exchange exchange, int cell) {
		// TODO

	}

	@Override
	public void onExchangeRequestOk(int playerId, int targetId, Exchange exchange) {
		// TODO

	}

	@Override
	public void onExchangeReady(int datas) {
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onCraft(CraftResult result) {
		// TODO

	}

	@Override
	public void onLocalMove(int itemtype, int amount, int localkama) {
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();

	}

	@Override
	public void onDistantMove(Item moved, boolean added, int kamas, int remainingHours) {
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
		getPerso().getAbilities().getBaseAbility().confirmExchange();

	}

	@Override
	public void onCoopMove(Item moved, int kamas, boolean added) {
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();

	}

	@Override
	public void onStorageMove(Item moved, int kamas, boolean added) {
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onShopMove(Item moved, boolean add) {
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();

	}

	@Override
	public void onCraftPublic(boolean craftPublicMode, int itemid, int multiCraftSkill) {
		// TODO

	}

	@Override
	public void onSellToNpc(boolean success) {
		// TODO

	}

	@Override
	public void onBuyToNpc(boolean success) {
		getPerso().getAbilities().getBaseAbility().getStates().buyResult = success ? BuyResult.SUCCESS : BuyResult.NO_KAMA;
		getPerso().getAbilities().getBaseAbility().getBotThread().unpause();
	}

	@Override
	public void onCraftLoop(int index) {
		// TODO

	}

	@Override
	public void onCraftLoopEnd(CraftLoopEndResult result) {
		// TODO

	}

	@Override
	public void onLeave(boolean success) {
		BaseAbility ab = getPerso().getAbilities().getBaseAbility();
		ab.getStates().exchangeSuccess = success;
		ab.getBotThread().unpause();
	}

}
