package fr.aresrpg.eratz.domain.data.player.info;

import static fr.aresrpg.tofumanchou.domain.Manchou.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.log.AnsiColors.AnsiColor;
import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.dofus.protocol.exchange.client.ExchangeMoveItemsPacket.MovedItem;
import fr.aresrpg.dofus.protocol.item.client.ItemDestroyPacket;
import fr.aresrpg.dofus.structures.Skills;
import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.dofus.structures.item.ItemCategory;
import fr.aresrpg.dofus.structures.map.Cell;
import fr.aresrpg.dofus.util.Maps;
import fr.aresrpg.dofus.util.Pathfinding;
import fr.aresrpg.dofus.util.Pathfinding.Node;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.util.UtilFunc;
import fr.aresrpg.eratz.domain.util.Validators;
import fr.aresrpg.tofumanchou.domain.data.enums.*;
import fr.aresrpg.tofumanchou.domain.data.item.Item;
import fr.aresrpg.tofumanchou.infra.data.ManchouItem;
import fr.aresrpg.tofumanchou.infra.data.ManchouPerso;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public class Utilities extends Info {

	private int kamasToKeep = 10_000;
	private Map<Integer, Integer> itemsToKeep = new HashMap<>();
	private Set<Zaap> zaaps;
	private int nextMapId = -1; // used in navigator to inform of the next map
	private int currentHarvest = -1; // used in harvesting to inform of the current harvested ressource

	public Utilities(BotPerso perso) {
		super(perso);
		itemsToKeep.put(DofusItems.POTION_DE_CITE_BONTA, 20);
		itemsToKeep.put(DofusItems.POTION_DE_RAPPEL, 20);
		itemsToKeep.put(DofusItems.POTION_DE_CITE_BRAKMAR, 20);
	}

	@Override
	public void shutdown() {

	}

	public boolean hasZaap(Zaap zaap) {
		if (zaaps == null) return true; // si zaap non init on return true pour qu'il aille init
		return zaaps.contains(zaap);
	}

	/**
	 * @return the zaaps
	 */
	public Set<Zaap> getZaaps() {
		return zaaps;
	}

	/**
	 * @return the currentHarvest
	 */
	public int getCurrentHarvest() {
		return currentHarvest;
	}

	/**
	 * @param currentHarvest
	 *            the currentHarvest to set
	 */
	public void setCurrentHarvest(int currentHarvest) {
		this.currentHarvest = currentHarvest;
	}

	public Skills getSkillFor(Interractable i) {
		if (!ArrayUtils.contains(getPerso().getPerso().getJob().getType(), i.getRequiredJob())) return null;
		for (Skills s : Skills.values())
			if (s.getType() == i) return s;
		return null;
	}

	/**
	 * @param zaaps
	 *            the zaaps to set
	 */
	public void setZaaps(Set<Zaap> zaaps) {
		this.zaaps = zaaps;
	}

	public int getKamasToKeep() {
		return kamasToKeep;
	}

	public boolean isOnPath() { // si -1 alors aucun path
		return nextMapId == -1 || getPerso().getPerso().getMap().getMapId() == nextMapId;
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
		if (max == 0) return 0;
		return 100 * curr / max;
	}

	public void useRessourceBags() {
		getPerso().getPerso().getInventory().getItemsByCategory(ItemCategory.RESOURCEBAG).forEach(i -> {
			getPerso().getPerso().useRessourceBags();
		});
	}

	public int getRandomAccessibleCellNextTo(int cellid, Function<Node, Node[]> neigbors) {
		Cell[] cells = getPerso().getPerso().getMap().getProtocolCells();
		Cell cell = cells[cellid];
		int width = getPerso().getPerso().getMap().getWidth();
		int height = getPerso().getPerso().getMap().getHeight();
		Node[] nodes = neigbors.apply(new Node(cell.getXRot(), cell.getYRot()));
		int perso = getPerso().getPerso().getCellId();
		List<Node> path = null;
		for (Node n : nodes) {
			if (!Maps.isInMapRotated(n.getX(), n.getY(), width, height)) continue;
			int id = Maps.getIdRotated(n.getX(), n.getY(), width, height);
			if (id < 0 || id >= cells.length) continue;
			path = Pathfinding.getCellPath(perso, id, cells, width, height, Pathfinding::getNeighbors, Validators.avoidingMobs(getPerso().getPerso().getMap()));
			if (path != null) return id;
		}
		return -1;
	}

	public void destroyHeaviestRessource() {
		useRessourceBags();
		Item it = getPerso().getPerso().getInventory().getHeaviestItem();
		if (it == null) throw new NullPointerException("La ressource la plus lourde est introuvable !");
		int maxp = getPerso().getPerso().getMaxPods();
		int pod = getPerso().getPerso().getPods();
		int over = pod + 1 - maxp;
		if (over < 1) return;
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

	public void openBank() {
		Threads.uSleep(2, TimeUnit.SECONDS);
		ManchouPerso p = getPerso().getPerso();
		p.speakToNpc(-2);
		p.npcTalkChoice(318, 259);
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
		Threads.uSleep(2, TimeUnit.SECONDS);
		final Set<Item> inv = getPerso().getPerso().getInventory().getContents().values().stream().map(i -> (ManchouItem) i).filter(UtilFunc.needToDeposit(getPerso())).collect(Collectors.toSet());
		final MovedItem[] array = inv.stream().map(i -> (ManchouItem) i).map(UtilFunc.deposit(getPerso())).filter(Objects::nonNull).toArray(MovedItem[]::new);
		LOGGER.info(AnsiColor.GREEN + "à déposé : "
				+ Arrays.stream(array).map(m -> getPerso().getPerso().getInventory().getItem(m.getItemUid())).map(Item::showInfos).collect(Collectors.joining(",", "[", "]")) + " en banque !");
		Arrays.stream(array).forEach(i -> {
			getPerso().getPerso().moveItem(i);
			Threads.uSleep(250, TimeUnit.MILLISECONDS);
		});
		Threads.uSleep(2, TimeUnit.SECONDS);
		MovedItem[] toRetrieve = getPerso().getPerso().getAccount().getBank().getContents().values().stream().map(i -> (ManchouItem) i).map(UtilFunc.retrieve(getPerso())).filter(Objects::nonNull)
				.toArray(MovedItem[]::new);
		LOGGER.info(AnsiColor.GREEN + "à récupéré : "
				+ Arrays.stream(toRetrieve).map(m -> getPerso().getPerso().getAccount().getBank().getItem(m.getItemUid())).map(Item::showInfos).collect(Collectors.joining(",", "[", "]"))
				+ " en banque !");
		Arrays.stream(toRetrieve).forEach(i -> {
			getPerso().getPerso().moveItem(i);
			Threads.uSleep(250, TimeUnit.MILLISECONDS);
		});
		Threads.uSleep(2, TimeUnit.SECONDS);
		final int kamas = getPerso().getPerso().getInventory().getKamas();
		final int bankK = getPerso().getPerso().getAccount().getBank().getKamas();
		int tomove;
		if (kamas < 10_000) {
			tomove = 10_000 - kamas;
			if (tomove > bankK) tomove = bankK;
			tomove = -tomove;
		} else tomove = kamas - 10_000;
		getPerso().getPerso().moveKama(tomove);
		if (tomove > 0) LOGGER.info(AnsiColor.GREEN + " à déposé " + tomove + " kamas !");
		else LOGGER.info(AnsiColor.GREEN + " à récupéré " + (-tomove) + " kamas !");
		Threads.uSleep(1, TimeUnit.SECONDS);
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
