package fr.aresrpg.eratz.domain.ia.mind;

import fr.aresrpg.commons.domain.util.exception.NotImplementedException;
import fr.aresrpg.eratz.domain.data.dofus.map.Path;
import fr.aresrpg.eratz.domain.data.dofus.player.Channel;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.behavior.BehaviorStopReason;
import fr.aresrpg.eratz.domain.ia.behavior.move.BankDepositPath;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Predicate;

/**
 * 
 * @since
 */
public class BaseMind implements Mind {

	private final Perso perso;
	private Queue<Future<BehaviorStopReason>> actions = new LinkedList<>();
	private boolean infinite;
	private Set<Integer> itemsToKeep = new HashSet<>();

	public BaseMind(Perso perso) {
		this.perso = perso;
	}

	@Override
	public void process() throws InterruptedException, ExecutionException {
		while (getPerso().getAccount().isActive()) {
			Future<BehaviorStopReason> next = getActions().poll();
			if (next == null) break;
			switch (next.get()) { // possibilité d'effectuer des actions selon le type de retour
				case QUANTITY_REACHED:
					getPerso().getAbilities().getBaseAbility().speak(Channel.ADMIN, "Récolte terminée.");
					break;
			}
			if (infinite) getActions().add(next); // si infinite loop ajout a la queue
		}
	}

	/**
	 * @return the actions
	 */
	public Queue<Future<BehaviorStopReason>> getActions() {
		return actions;
	}

	/**
	 * @return the perso
	 */
	public Perso getPerso() {
		return perso;
	}

	@Override
	public Mind thenHarvest(Path path, int quantity) {
		getActions().add(path.getHarvestBehavior(getPerso(), quantity));
		return this;
	}

	@Override
	public Mind thenDrop(int item, int quantity) {
		throw new NotImplementedException();
	}

	@Override
	public Mind thenCraft(Path path, int quantity) {
		getActions().add(path.getCraftBehavior(getPerso(), quantity));
		return this;
	}

	@Override
	public Mind thenDepositToBank() {
		getActions().add(new BankDepositPath(getPerso(), getItemToKeep().stream().mapToInt(Integer::intValue).toArray()));
		return this;
	}

	@Override
	public Mind thenFight(Path path) {
		throw new NotImplementedException();
	}

	@Override
	public Mind thenFeedPets() {
		throw new NotImplementedException();
	}

	@Override
	public Mind thenSell(Path path) {
		throw new NotImplementedException();
	}

	@Override
	public void thenRestart() {
		this.infinite = true;
	}

	@Override
	public Mind thenDisconnectIf(String reason, int timeToStayOffline, Predicate<Perso> condition) {
		if (condition.test(getPerso())) getPerso().disconnect(reason, timeToStayOffline);
		return this;
	}

	@Override
	public Set<Integer> getItemToKeep() {
		return this.itemsToKeep;
	}

}
