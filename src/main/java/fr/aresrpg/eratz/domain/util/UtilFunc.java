package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.dofus.protocol.exchange.client.ExchangeMoveItemsPacket.MovedItem;
import fr.aresrpg.dofus.structures.ExchangeMove;
import fr.aresrpg.dofus.structures.item.ItemCategory;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.tofumanchou.infra.data.ManchouItem;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 
 * @since
 */
public class UtilFunc {

	public static Function<ManchouItem, MovedItem> deposit(BotPerso perso) {
		return i -> {
			Integer toKeep = perso.getItemsToKeep().get(i.getTypeId());
			int amount = i.getAmount();
			if (toKeep != null) amount -= toKeep.intValue();
			if (amount < 1) return null;
			return new MovedItem(ExchangeMove.ADD, i.getUUID(), amount);
		};
	}

	public static Function<ManchouItem, MovedItem> retrieve(BotPerso perso) {
		return i -> {
			Integer toKeep = perso.getItemsToKeep().get(i.getTypeId());
			int amount = i.getAmount();
			int amountInInv = perso.getQuantityInInventoryOf(i.getTypeId());
			int toMove = 0;
			if (toKeep != null && amountInInv < toKeep.intValue()) toMove = toKeep.intValue() - amountInInv;
			if (toMove < 1) return null;
			if (toMove > amount) toMove = amount;
			return new MovedItem(ExchangeMove.REMOVE, i.getUUID(), amount);
		};
	}

	public static Predicate<ManchouItem> needToDeposit(BotPerso perso) {
		return i -> i.getPosition() == -1 && i.getCategory() != ItemCategory.QUESTOBJECT && i.getCategory() != ItemCategory.QUEST;
	}

	public static Map[] emptyMapArray() {
		return new Map[0];
	}

}