package fr.aresrpg.eratz.domain.ia.path.zone.bucheron;

import fr.aresrpg.dofus.structures.item.Interractable;
import fr.aresrpg.dofus.structures.job.Jobs;
import fr.aresrpg.eratz.domain.data.map.BotMap;
import fr.aresrpg.eratz.domain.data.player.BotPerso;
import fr.aresrpg.eratz.domain.ia.path.Paths;
import fr.aresrpg.eratz.domain.ia.path.zone.HarvestZone;

/**
 * 
 * @since
 */
public class BontaZone extends HarvestZone {

	public BontaZone(BotPerso perso) {
		super(() -> perso, true, Interractable.getAllForJob(Jobs.JOB_BUCHERON).stream().filter(i -> i != Interractable.FRENE && i != Interractable.MACHINE_BUCHERON).toArray(Interractable[]::new));
	}

	@Override
	protected boolean isValid(BotMap map) {
		return hasArea(map, 7, 8) && hasSubArea(map, 43, 68, 69, 38) && map.hasOne(getRessources());
	}

	@Override
	protected Paths getType() {
		return Paths.BUCHERON_BONTA;
	}

}
