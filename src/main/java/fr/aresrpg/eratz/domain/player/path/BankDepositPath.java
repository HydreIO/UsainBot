/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.player.path;

import fr.aresrpg.commons.domain.util.ArrayUtils;
import fr.aresrpg.dofus.structures.character.Item;
import fr.aresrpg.eratz.domain.behavior.BaseAbility;
import fr.aresrpg.eratz.domain.behavior.move.PathBehavior;
import fr.aresrpg.eratz.domain.dofus.map.Zaap;
import fr.aresrpg.eratz.domain.dofus.player.Channel;
import fr.aresrpg.eratz.domain.dofus.player.InventoryType;
import fr.aresrpg.eratz.domain.player.Perso;

import java.util.Arrays;
import java.util.Random;

/**
 * 
 * @since
 */
public class BankDepositPath implements PathBehavior {

	private Perso perso;
	private Random humanRandomizer = new Random();

	public BankDepositPath(Perso perso) {
		this.perso = perso;
	}

	@Override
	public void startPath() {
		BaseAbility ability = perso.getBaseAbility();
		ability.goToZaap(Zaap.ASTRUB);
		waitLitle();
		perso.getNavigation().moveDown(3).moveToCell(142);
		waitLitle();
		ability.speakToNpc("Al Etsop").npcTalkChoice(0);
		Item[] wep = ability.depositInventoryInChest(InventoryType.EQUIPEMENT);
		Item[] div = ability.depositInventoryInChest(InventoryType.DIVERS);
		Item[] res = ability.depositInventoryInChest(InventoryType.RESSOURCES);
		waitLitle();
		ability.speak(Channel.ADMIN, "à déposé : " + Arrays.toString(ArrayUtils.concat(wep, div, res)) + " en banque !");
	}

	private void waitLitle() { // zone peuplé mieux vaut ne pas se déplacer trop vite
		waitSec(humanRandomizer.nextInt(6));
	}

	@Override
	public PathBehavior getPathToReachCurrentPath() {
		return null;
	}

	@Override
	public Perso getPerso() {
		return this.perso;
	}
}
