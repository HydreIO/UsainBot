/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.ia.behavior.move;

import static fr.aresrpg.eratz.domain.TheBotFather.LOGGER;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.log.AnsiColors.AnsiColor;
import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.commons.domain.util.Randoms;
import fr.aresrpg.dofus.protocol.exchange.client.ExchangeMoveItemsPacket.MovedItem;
import fr.aresrpg.dofus.structures.ExchangeMove;
import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.dofus.structures.item.ItemCategory;
import fr.aresrpg.eratz.domain.data.ItemsData;
import fr.aresrpg.eratz.domain.data.ItemsData.LangItem;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public class BankAmaknaDepositPath extends Behavior {

	private int[] items;

	public BankAmaknaDepositPath(Perso perso, int... itemsToKeep) {
		super(perso);
		this.items = itemsToKeep;
	}

	public void startPath() {

	}

	private String nameObject(Item o) {
		LangItem langItem = ItemsData.get(o.getItemTypeId());
		return "x" + o.getQuantity() + " " + langItem.getName();
	}

	private void waitLitle() { // zone peuplé mieux vaut ne pas se déplacer trop vite
		Threads.uSleep(Randoms.nextBetween(1, 3), TimeUnit.SECONDS);
	}

	@Override
	public BehaviorStopReason start() {
		BaseAbility ability = getPerso().getAbilities().getBaseAbility();
		ability.goAndOpenAmaknaBank();
		waitLitle();
		Set<Item> inv = getPerso().getInventory().getContents().values().stream().filter(e -> {
			if (ArrayUtils.contains(e.getItemTypeId(), items)) return false;
			LangItem l = ItemsData.get(e.getItemTypeId());
			if (l.getCategory() == ItemCategory.QUESTOBJECT || l.getCategory() == ItemCategory.QUEST) return false;
			return true;
		}).collect(Collectors.toSet());
		MovedItem[] array = inv.stream().map(it -> new MovedItem(ExchangeMove.ADD, it.getUid(), it.getQuantity())).toArray(MovedItem[]::new);
		Arrays.stream(array).forEach(i -> {
			ability.moveItem(i);
			Threads.uSleep(250, TimeUnit.MILLISECONDS);
		});
		waitLitle();
		LOGGER.info(AnsiColor.GREEN + "à déposé : " + inv.stream().map(this::nameObject).collect(Collectors.joining(",", "[", "]")) + " en banque !");
		Threads.uSleep(1, TimeUnit.SECONDS);
		int kamas = getPerso().getInventory().getKamas() - 5000;
		if (kamas > 0) ability.moveKama(kamas);
		ability.exchangeLeave();
		Threads.uSleep(1, TimeUnit.SECONDS);
		getPerso().getNavigation().moveToCell(293, true);
		return BehaviorStopReason.FINISHED;
	}

}
