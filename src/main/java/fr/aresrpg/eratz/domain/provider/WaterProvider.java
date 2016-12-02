package fr.aresrpg.eratz.domain.provider;

import fr.aresrpg.eratz.domain.dofus.item.Items;
import fr.aresrpg.eratz.domain.player.Perso;

public class WaterProvider implements ItemProvider {

	private Perso providing, provided;

	public WaterProvider(Perso providing, Perso provided) {
		this.providing = providing;
		this.provided = provided;
	}

	public Perso getProvided() {
		return provided;
	}

	public Perso getProviding() {
		return providing;
	}

	@Override
	public void provide() {

	}

	@Override
	public Items getItemType() {
		return Items.EAU;
	}

}
