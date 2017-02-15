package fr.aresrpg.eratz.domain.data;

import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.dofus.structures.item.ItemCategory;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.tofumanchou.domain.data.ItemsData;

import java.util.*;

/**
 * 
 * @since
 */
public class HdvManager {

	private static final HdvManager INSTANCE = new HdvManager();
	private Map<Integer, StackPrice> hdvPrices = new HashMap<>();
	private Map<Integer, StackPrice> botPrices = new HashMap<>();
	private Map<BotPerso, Set<SellingItem>> items = new HashMap<>();

	private HdvManager() {
	}

	public static HdvManager getInstance() {
		return INSTANCE;
	}

	public StackPrice getMinSellingHdvPrice(int item) {
		return hdvPrices.getOrDefault(item, new StackPrice());
	}

	public StackPrice getMinSellingBotPrice(int item) {
		return botPrices.getOrDefault(item, new StackPrice());
	}

	public boolean isHdvCheaper(int stack, int item) {
		return getMinSellingHdvPrice(item).isCheaper(stack, getMinSellingBotPrice(item));
	}

	public void updateHdvPrice(int item, StackPrice price) {
		hdvPrices.put(null, price);
	}

	public Set<SellingItem> getSellingItems(BotPerso perso) {
		Set<SellingItem> set = items.get(perso);
		if (set == null) items.put(perso, set = new HashSet<SellingItem>() {
			@Override
			public boolean add(SellingItem e) {
				getMinSellingBotPrice(e.getType()).updatePrice(e.getAmount(), e.getPrice());
				return super.add(e);
			}
		});
		return set;

	}

	public void notifySelledItems(BotPerso perso, SellingItem... items) {
		notifySelledItems(perso, Arrays.asList(items));
	}

	public void notifySelledItems(BotPerso perso, Collection<SellingItem> items) {
		if (items.size() == 0) return;
		Set<SellingItem> sellingItems = getSellingItems(perso);
		sellingItems.clear();
		sellingItems.addAll(items);
	}

	public static class StackPrice {
		private long price1 = -1, price10 = -1, price100 = -1, midlePrice = -1;

		public StackPrice() {

		}

		public StackPrice(long price1, long price10, long price100, long midlePrice) {
			this.price1 = price1;
			this.price10 = price10;
			this.price100 = price100;
			this.midlePrice = midlePrice;
		}

		public boolean isCheaper(int stack, StackPrice price) {
			switch (stack) {
				case 1:
					return price1 < price.price1;
				case 10:
					return price10 < price.price10;
				case 100:
					return price100 < price.price100;
				default:
					throw new IllegalArgumentException("Invalid stack (" + stack + ')');
			}
		}

		public void updatePrice(int stack, long price) {
			switch (stack) {
				case 1:
					if (price1 == -1 || price < price1) price1 = price;
					break;
				case 10:
					if (price10 == -1 || price < price10) price10 = price;
					break;
				case 100:
					if (price100 == -1 || price < price100) price100 = price;
					break;
				default:
					throw new IllegalArgumentException("Invalid stack (" + stack + ')');
			}
		}

		public long getMidlePrice() {
			return midlePrice;
		}

		public void updateMidlePrice(long midlePrice) {
			this.midlePrice = midlePrice;
		}

		public long getPrice1() {
			return price1;
		}

		public long getPrice10() {
			return price10;
		}

		public long getPrice100() {
			return price100;
		}

		@Override
		public String toString() {
			return "StackPrice [price1=" + price1 + ", price10=" + price10 + ", price100=" + price100 + "]";
		}

	}

	public static class SellingItem {
		private int type;
		private ItemCategory category;
		private int amount;
		private long price;

		public SellingItem(int type, ItemCategory category, int amount, long price) {
			this.type = type;
			this.category = category;
			this.amount = amount;
			this.price = price;
		}

		public static SellingItem fromItem(Item item) {
			int type = item.getItemTypeId();
			ItemCategory cate = ItemsData.get(type).getCategory();
			int amount = item.getQuantity();
			long price = item.getPrice();
			return new SellingItem(type, cate, amount, price);
		}

		public int getType() {
			return type;
		}

		public ItemCategory getCategory() {
			return category;
		}

		public int getAmount() {
			return amount;
		}

		public long getPrice() {
			return price;
		}

		@Override
		public String toString() {
			return "SellingItem [type=" + type + ", category=" + category + ", amount=" + amount + ", price=" + price + "]";
		}

	}
}
