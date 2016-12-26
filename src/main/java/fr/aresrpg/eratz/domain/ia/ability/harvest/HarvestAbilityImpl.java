package fr.aresrpg.eratz.domain.ia.ability.harvest;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.dofus.structures.Skills;
import fr.aresrpg.eratz.domain.TheBotFather;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.data.player.object.Ressource;
import fr.aresrpg.eratz.domain.data.player.state.PlayerState;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class HarvestAbilityImpl implements HarvestAbility {

	private Perso perso;

	public HarvestAbilityImpl(Perso perso) {
		this.perso = perso;
	}

	@Override
	public void shutdown() {

	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	@Override
	public void harvest(Ressource r, Skills skill) {
		while (getPerso().getState() == PlayerState.HARVESTING)
			Threads.uSleep(50, TimeUnit.MILLISECONDS);
		int cl = r.getNeighborCell(getPerso().getMapInfos().getMap());
		if (cl == -1) {
			TheBotFather.LOGGER.severe("Impossible de trouver une cellule pour la ressource " + r);
			return;
		}
		if (getPerso().getMapInfos().getCellId() != cl) {
			getPerso().getNavigation().moveToCell(cl);
			if (getPerso().getMapInfos().getCellId() != cl) return; // path non trouvé (par exemple a cause de mob agressifs)
		}
		getPerso().setState(PlayerState.HARVESTING);
		getPerso().getAbilities().getBaseAbility().interract(skill, r.getCell().getId());
	}

}
