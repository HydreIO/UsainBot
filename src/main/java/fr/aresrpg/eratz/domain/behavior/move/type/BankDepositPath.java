/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.behavior.move.type;

import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.dofus.structures.Exchange;
import fr.aresrpg.dofus.structures.item.Item;
import fr.aresrpg.eratz.domain.ability.BaseAbility;
import fr.aresrpg.eratz.domain.behavior.move.PathBehavior;
import fr.aresrpg.eratz.domain.dofus.player.Channel;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.player.Player;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * @since
 */
public class BankDepositPath extends PathBehavior {

	private Random humanRandomizer = new Random();
	private int[] items;

	public BankDepositPath(Perso perso, int... itemsToKeep) {
		super(perso);
		this.items = itemsToKeep;
	}

	public void startPath() {
		BaseAbility ability = getPerso().getBaseAbility();
		ability.closeGui();
		if (!ability.goAndOpenBank()) return;
		Set<Item> inv = getPerso().getInventory().getContents();
		inv.stream().filter(i -> !ArrayUtils.contains(i.getId(), items)).forEach(i -> ability.moveItem(i.getId(), i.getQuantity(), Exchange.PLAYER_INVENTORY, Exchange.BANK));
		waitLitle();
		ability.speak(Channel.ADMIN, "à déposé : " + inv.stream().map(this::nameObject).collect(Collectors.joining(",", "[", "]")) + " en banque !");
	}

	private String nameObject(Item o) {
		return "x" + o.getQuantity() + " " + o.getTemplate().getName();
	}

	private void waitLitle() { // zone peuplé mieux vaut ne pas se déplacer trop vite
		waitSec(humanRandomizer.nextInt(6));
	}

	@Override
	public PathBehavior getPathToReachCurrentPath() {
		return null;
	}

	@Override
	public boolean acceptDefi(Player p) {
		return false;
	}

	@Override
	public boolean acceptEchange(Player p) {
		return false;
	}

	@Override
	public boolean acceptGuilde(String pname) {
		return false;
	}

	@Override
	public boolean acceptGroup(String pname) {
		for (Player p : getPerso().getGroup())
			if (p.getPseudo().equalsIgnoreCase(pname)) return true;
		return false;
	}

}
