/*******************************************************************************
 * BotFather (C) - Dofus 1.29
 * This class is part of an AresRPG Project.
 *
 * @author Sceat {@literal <sceat@aresrpg.fr>}
 * 
 *         Created 2016
 *******************************************************************************/
package fr.aresrpg.eratz.domain.behavior.craft.type;

import fr.aresrpg.eratz.domain.behavior.craft.CraftBehavior;
import fr.aresrpg.eratz.domain.dofus.item.Items;
import fr.aresrpg.eratz.domain.player.Perso;

/**
 * 
 * @since
 */
public class AmaknaBreadCraftBehavior extends CraftBehavior {

	private int craft;

	/**
	 * @param perso
	 */
	public AmaknaBreadCraftBehavior(Perso perso, int craft) {
		super(perso);
		this.craft = craft;
	}

	/**
	 * @return the craft
	 */
	public int getNumberToCraft() {
		return craft;
	}

	@Override
	public Items getItemToCraft() {
		return Items.PAIN_AMAKNA;
	}

	@Override
	public void start() {

	}
}
