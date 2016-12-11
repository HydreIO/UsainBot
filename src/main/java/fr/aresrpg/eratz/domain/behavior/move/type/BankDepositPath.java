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
import fr.aresrpg.dofus.structures.item.Object;
import fr.aresrpg.eratz.domain.ability.BaseAbility;
import fr.aresrpg.eratz.domain.behavior.move.PathBehavior;
import fr.aresrpg.eratz.domain.dofus.player.Channel;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.player.Player;

import java.util.Arrays;
import java.util.Random;

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
		Object[] inv = ability.getItemInInventory();
		ability.depositItemInChest(Arrays.stream(inv).mapToInt(o -> o.getTemplate().getID()).filter(i -> !ArrayUtils.contains(i, items)).toArray());
		waitLitle();
		ability.speak(Channel.ADMIN, "à déposé : " + Arrays.toString(inv) + " en banque !");
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
