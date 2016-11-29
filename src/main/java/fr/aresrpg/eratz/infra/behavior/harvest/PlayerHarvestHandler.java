package fr.aresrpg.eratz.infra.behavior.harvest;

import fr.aresrpg.commons.domain.util.schedule.Schedule;
import fr.aresrpg.commons.domain.util.schedule.Scheduled;
import fr.aresrpg.eratz.domain.behavior.Behavior;
import fr.aresrpg.eratz.domain.behavior.harvest.HarvestBehavior;
import fr.aresrpg.eratz.domain.behavior.harvest.HarvestHandler;
import fr.aresrpg.eratz.domain.dofus.ressource.Ressource;
import fr.aresrpg.eratz.domain.player.Perso;
import fr.aresrpg.eratz.domain.player.Player;
import fr.aresrpg.eratz.domain.util.concurrent.Executors;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @since
 */
public class PlayerHarvestHandler implements HarvestHandler, Scheduled {

	private Perso perso;
	private List<ScheduledFuture> tasks;

	public PlayerHarvestHandler(Perso perso) {
		this.perso = perso;
		tasks = Executors.SCHEDULER.register(this);
	}

	public void close() {
		tasks.forEach(c -> c.cancel(false));
	}

	@Override
	public void onRessourceSpawn(Ressource r) {
		r.setSpawned();
		if (canHarvest(r)) perso.getHarvestAbility().harvest(r);
	}

	@Override
	public void onRessourceRecolted(Player p, Ressource r) {
		r.setSpawned(false);
	}

	@Schedule(rate = 30, unit = TimeUnit.SECONDS)
	public void update() {
		perso.getBehaviors().stream().filter(Behavior::isActive).filter(b -> b instanceof HarvestBehavior).sorted(Behavior::compareTo).filter(this::needToDepositBank).forEach(this::depositBank);
	}

	private boolean canHarvest(Ressource r) {

	}

	private boolean needToDepositBank(Behavior b) {
		return ((HarvestBehavior) b).needToDepositAtBank();
	}

	private void depositBank(Behavior b) {

	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

}
