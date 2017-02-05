package fr.aresrpg.eratz.domain.util;

import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.dofus.protocol.exchange.client.ExchangeMoveItemsPacket.MovedItem;
import fr.aresrpg.dofus.structures.ExchangeMove;
import fr.aresrpg.dofus.structures.Skills;
import fr.aresrpg.dofus.structures.item.ItemCategory;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.data.MapsManager;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.tofumanchou.domain.data.ItemsData;
import fr.aresrpg.tofumanchou.domain.data.ItemsData.LangItem;
import fr.aresrpg.tofumanchou.infra.data.ManchouItem;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 
 * @since
 */
public class UtilFunc {

	public static Function<ManchouItem, MovedItem> deposit(BotPerso perso) {
		return i -> {
			Integer toKeep = perso.getUtilities().getItemsToKeep().get(i.getTypeId());
			int amount = i.getAmount();
			if (toKeep != null) amount -= toKeep.intValue();
			if (amount < 1) return null;
			return new MovedItem(ExchangeMove.ADD, i.getUUID(), amount);
		};
	}

	public static Function<ManchouItem, MovedItem> retrieve(BotPerso perso) {
		return i -> {
			Integer toKeep = perso.getUtilities().getItemsToKeep().get(i.getTypeId());
			int amount = i.getAmount();
			int amountInInv = perso.getUtilities().getQuantityInInventoryOf(i.getTypeId());
			int toMove = 0;
			if (toKeep != null && amountInInv < toKeep.intValue()) toMove = toKeep.intValue() - amountInInv;
			if (toMove < 1) return null;
			if (toMove > amount) toMove = amount;
			return new MovedItem(ExchangeMove.REMOVE, i.getUUID(), toMove);
		};
	}

	public static MovedItem[] retrieveWoodStacks(BotPerso perso) {
		int pod = perso.getPerso().getMaxPods() - perso.getPerso().getPods();
		PriorityQueue<ItemMoved> queue = new PriorityQueue<>(Comparator.<ItemMoved>comparingInt(item -> Skills.fromRecolted(item.item.getTypeId()).getMinLvlToUse()).reversed());
		perso.getPerso().getAccount().getBank().getItemsByCategory(ItemCategory.WOOD)
		.stream() 
		.filter(i -> i.getAmount() >= 100)
		.map(i -> (ManchouItem) i)
		.forEach(i->{
			LangItem langItem = ItemsData.get(i.getTypeId());
			int stacks = i.getAmount() / 100;
			if (stacks < 1) return;
			IntStream.range(0, stacks).forEach(num->queue.add(new ItemMoved(i, new MovedItem(ExchangeMove.REMOVE, i.getUUID(), 100), langItem.getPod() * 100)));
		});
		List<MovedItem> moveds = new ArrayList<>();
		while (!queue.isEmpty()) {
			ItemMoved im = queue.poll();
			if (im.pods > pod) break;
			moveds.add(im.moved);
			pod -= im.pods;
		}
		return moveds.stream().toArray(MovedItem[]::new);
	}

	public static Function<int[], Node[]> mapsToNodes() {
		return ids -> ArrayUtils.shrinkNulls(Arrays.stream(ids).mapToObj(id -> {
			BotMap map = MapsManager.getMap(id);
			if (map == null) return null;
			return new Node(map.getMap().getX(), map.getMap().getY());
		}).toArray(Node[]::new));
	}

	public static Predicate<ManchouItem> needToDeposit(BotPerso perso) {
		return i -> i.getPosition() == -1 && i.getCategory() != ItemCategory.QUESTOBJECT && i.getCategory() != ItemCategory.QUEST;
	}

	public static Map[] emptyMapArray() {
		return new Map[0];
	}

	public static Comparator<BotMap> distanceToPlayer(Supplier<BotPerso> playerPosition) {
		return (o1, o2) -> {
			BotMap map = MapsManager.getMap(playerPosition.get().getPerso().getMap().getMapId());
			if (map == null) return 0;
			return map.distance(o1) - map.distance(o2);
		};
	}

	private static class ItemMoved {
		ManchouItem item;
		MovedItem moved;
		int pods;

		public ItemMoved(ManchouItem item, MovedItem moved, int pods) {
			this.item = item;
			this.moved = moved;
			this.pods = pods;
		}

	}

}
