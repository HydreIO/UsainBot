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
import fr.aresrpg.eratz.domain.dofus.item.Recipe;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.provider.type.WaterProvider;

/**
 * 
 * @since
 */
public class AmaknaBreadCraftBehavior extends CraftBehavior {

	private WaterProvider waterProvider;

	/**
	 * @param perso
	 */
	public AmaknaBreadCraftBehavior(Perso perso, int craft) {
		super(perso, craft);
		this.waterProvider = new WaterProvider(perso, craft);

	}

	@Override
	public Items getItemToCraft() {
		return Items.PAIN_AMAKNA;
	}

	@Override
	public void start() {
		Recipe recipe = getItemToCraft().getRecipe();

	}
}
