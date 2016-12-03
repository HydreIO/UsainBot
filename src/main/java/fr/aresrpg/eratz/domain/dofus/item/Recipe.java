package fr.aresrpg.eratz.domain.dofus.item;

import java.util.*;

/**
 * 
 * @since
 */
public class Recipe {

	private Map<Items, Integer> recipe = new HashMap<>();

	public Recipe() {
	}

	public Recipe addItem(Items item, int quantity) {
		recipe.put(item, quantity);
		return this;
	}

	public int getQuantity(Items item) {
		if (recipe.containsKey(item)) return recipe.get(item);
		return 0;
	}

	/**
	 * @return the recipe
	 */
	public Map<Items, Integer> getRecipe() {
		return recipe;
	}

	public Set<Items> getIngredients() {
		return getRecipe().keySet();
	}

}
