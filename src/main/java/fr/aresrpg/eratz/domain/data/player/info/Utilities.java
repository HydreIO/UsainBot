package fr.aresrpg.eratz.domain.data.player.info;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.log.AnsiColors.AnsiColor;
import fr.aresrpg.dofus.protocol.exchange.client.ExchangeMoveItemsPacket.MovedItem;
import fr.aresrpg.dofus.protocol.item.client.ItemDestroyPacket;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.util.UtilFunc;
import fr.aresrpg.tofumanchou.domain.data.enums.*;
import fr.aresrpg.tofumanchou.domain.data.item.Item;
import fr.aresrpg.tofumanchou.infra.data.ManchouItem;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public class Utilities extends Info {

	private int kamasToKeep = 10_000;
	private Map<Integer, Integer> itemsToKeep = new HashMap<>();
	private Set<Zaap> zaaps = new HashSet<>();
	private int nextMapId; // used in navigator to inform of the next map

	public Utilities(BotPerso perso) {
		super(perso);
		itemsToKeep.put(DofusItems.POTION_DE_CITE_BONTA, 20);
		itemsToKeep.put(DofusItems.POTION_DE_RAPPEL, 20);
	}

	@Override
	public void shutdown() {

	}

	public boolean hasZaap(Zaap zaap) {
		return zaaps.contains(zaap);
	}

	public int getKamasToKeep() {
		return kamasToKeep;
	}

	public boolean isOnPath() {
		return getPerso().getPerso().getMap().getMapId() == getNextMapId();
	}

	public int getNextMapId() {
		return nextMapId;
	}

	public void setNextMapId(int nextMapId) {
		this.nextMapId = nextMapId;
	}

	public Map<Integer, Integer> getItemsToKeep() {
		return itemsToKeep;
	}

	public int getPodsPercent() {
		int curr = getPerso().getPerso().getPods();
		int max = getPerso().getPerso().getMaxPods();
		return 100 * curr / max;
	}

	public void destroyHeaviestRessource() {
		Item it = getPerso().getPerso().getInventory().getHeaviestItem();
		if (it == null) throw new NullPointerException("La ressource la plus lourde est introuvable !");
		int maxp = getPerso().getPerso().getMaxPods();
		int pod = getPerso().getPerso().getPods();
		int over = pod + 1 - maxp;
		int poditem = it.getPods();
		int fullw = poditem * it.getAmount();
		LOGGER.debug("Pod en trop = " + over);
		LOGGER.debug("Poid total de l'item = " + fullw);
		if (fullw <= over) {
			LOGGER.debug("Destruction de x" + it.getAmount() + " " + it.getName());
			getPerso().sendPacketToServer(new ItemDestroyPacket(it.getUUID(), it.getAmount()));
		} else {
			int todestroy = over / poditem + (over % poditem == 0 ? 0 : 1);
			LOGGER.debug("Destruction de x" + todestroy + " " + it.getName());
			getPerso().sendPacketToServer(new ItemDestroyPacket(it.getUUID(), todestroy));
		}
	}

	public Bank getNearestBank() {
		Bank near = Bank.ASTRUB;
		int dist = getPerso().getPerso().getMap().distance(near.getCoords());
		for (Bank k : Bank.values()) {
			int di = getPerso().getPerso().getMap().distance(k.getCoords());
			if (di < dist) {
				dist = di;
				near = k;
			}
		}
		return near;
	}

	public void depositBank() {
		final Set<Item> inv = getPerso().getPerso().getInventory().getContents().values().stream().map(i -> (ManchouItem) i).filter(UtilFunc.needToDeposit(getPerso())).collect(Collectors.toSet());
		final MovedItem[] array = inv.stream().map(i -> (ManchouItem) i).map(UtilFunc.deposit(getPerso())).filter(Objects::nonNull).toArray(MovedItem[]::new);
		Arrays.stream(array).forEach(i -> {
			getPerso().getPerso().moveItem(i);
			Threads.uSleep(250, TimeUnit.MILLISECONDS);
		});
		Threads.uSleep(1, TimeUnit.SECONDS);
		LOGGER.info(AnsiColor.GREEN + "à déposé : " + inv.stream().map(Item::showInfos).collect(Collectors.joining(",", "[", "]")) + " en banque !");
		MovedItem[] toRetrieve = getPerso().getPerso().getAccount().getBank().getContents().values().stream().map(i -> (ManchouItem) i).map(UtilFunc.retrieve(getPerso())).filter(Objects::nonNull)
				.toArray(MovedItem[]::new);
		Arrays.stream(toRetrieve).forEach(i -> {
			getPerso().getPerso().moveItem(i);
			Threads.uSleep(250, TimeUnit.MILLISECONDS);
		});
		Threads.uSleep(1, TimeUnit.SECONDS);
		final int kamas = getPerso().getPerso().getInventory().getKamas();
		final int bankK = getPerso().getPerso().getAccount().getBank().getKamas();
		int tomove;
		if (kamas < 10_000) {
			tomove = 10_000 - kamas;
			if (tomove > bankK) tomove = bankK;
			tomove = -tomove;
		} else tomove = kamas - 10_000;
		getPerso().getPerso().moveKama(tomove);
		getPerso().getPerso().exchangeLeave();
	}

	public boolean hasPopoRappel() {
		return !getPerso().getPerso().getInventory().getItems(DofusItems.POTION_DE_RAPPEL).isEmpty();
	}

	public boolean hasPopoBonta() {
		return !getPerso().getPerso().getInventory().getItems(DofusItems.POTION_DE_CITE_BONTA).isEmpty();
	}

	public boolean hasPopoBrakmar() {
		return !getPerso().getPerso().getInventory().getItems(DofusItems.POTION_DE_CITE_BRAKMAR).isEmpty();
	}

	private int quantityOf(int id, Set<Item> set) { // pr item non stackable
		return (int) set.stream().filter(i -> i.getTypeId() == id).count();
	}

	public boolean inventoryContainsItem(long itemUid) {
		return getPerso().getPerso().getInventory().getContents().containsKey(itemUid);
	}

	public int getQuantityInInventoryOf(long itemUid) {
		Item i = getPerso().getPerso().getInventory().getItem(itemUid);
		return i == null ? 0 : i.getAmount();
	}

	public int getQuantityInInventoryOf(int itemTypeId) {
		Set<Item> i = getPerso().getPerso().getInventory().getItems(itemTypeId);
		return (int) (i.isEmpty() ? 0 : i.stream().mapToLong(Item::getAmount).sum());
	}

	public int getQuantityInBankOf(long itemUid) {
		Item i = getPerso().getPerso().getAccount().getBank().getItem(itemUid);
		return i == null ? 0 : i.getAmount();
	}

	public int getQuantityInBankOf(int itemTypeId) {
		Set<Item> i = getPerso().getPerso().getAccount().getBank().getItems(itemTypeId);
		return (int) (i.isEmpty() ? 0 : i.stream().mapToLong(Item::getAmount).sum());
	}

}