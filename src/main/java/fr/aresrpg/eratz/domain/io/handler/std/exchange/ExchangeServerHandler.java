package fr.aresrpg.eratz.domain.io.handler.std.exchange;

import fr.aresrpg.dofus.protocol.exchange.server.ExchangeCreatePacket.ExchangeData;
import fr.aresrpg.dofus.structures.*;
import fr.aresrpg.dofus.structures.item.Item;

import java.util.Collection;

/**
 * 
 * @since
 */
public interface ExchangeServerHandler {

	void onCreate(Exchange type, ExchangeData datas);

	void onInventoryList(Exchange type, Collection<Item> items, int kamas);

	void onExchangeRequest(int targetId, Exchange exchange, int cell);

	void onExchangeRequestOk(int playerId, int targetId, Exchange exchange);

	void onExchangeReady(int datas);

	void onCraft(CraftResult result);

	void onLocalMove(int itemtype, int amount, int localkama);

	void onDistantMove(Item moved, boolean added, int kamas, int remainingHours);

	void onCoopMove(Item moved, int kamas, boolean added);

	void onStorageMove(Item moved, int kamas, boolean added);

	void onShopMove(Item moved, boolean add);

	void onCraftPublic(boolean craftPublicMode, int itemid, int multiCraftSkill);

	void onSellToNpc(boolean success);

	void onBuyToNpc(boolean success);

	void onCraftLoop(int index);

	void onCraftLoopEnd(CraftLoopEndResult result);

	void onLeave(boolean success);
}
