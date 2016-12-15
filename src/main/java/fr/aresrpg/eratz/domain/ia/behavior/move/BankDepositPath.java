/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.ia.behavior.move;

import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.eratz.domain.data.ItemsData;
import fr.aresrpg.eratz.domain.data.dofus.player.Channel;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.ability.BaseAbility;
import fr.aresrpg.eratz.domain.ia.behavior.Behavior;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public class BankDepositPath extends Behavior {

	private Random humanRandomizer = new Random();
	private int[] items;

	public BankDepositPath(Perso perso, int... itemsToKeep) {
		super(perso);
		this.items = itemsToKeep;
	}

	public void startPath() {

	}

	private String nameObject(Item o) {
		return "x" + o.getQuantity() + " " + ItemsData.getName(o.getItemTypeId());
	}

	private void waitLitle() { // zone peuplé mieux vaut ne pas se déplacer trop vite
		waitSec(humanRandomizer.nextInt(6));
	}

	@Override
	public BehaviorStopReason start() {
		BaseAbility ability = getPerso().getAbilities().getBaseAbility();
		ability.closeGui();
		if (!ability.goAndOpenBank()) return BehaviorStopReason.PATH_ERROR;
		Set<Item> inv = getPerso().getInventory().getContents().entrySet().stream().filter(e -> ArrayUtils.contains(e, items)).map(e -> e.getValue()).collect(Collectors.toSet());
		inv.stream().forEach(i -> ability.moveItem(i.getUid(), i.getQuantity()));
		waitLitle();
		ability.speak(Channel.ADMIN, "à déposé : " + inv.stream().map(this::nameObject).collect(Collectors.joining(",", "[", "]")) + " en banque !");
		return BehaviorStopReason.FINISHED;
	}

}
