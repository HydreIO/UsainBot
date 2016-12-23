package fr.aresrpg.eratz.domain.ia.mind;

import fr.aresrpg.commons.domain.concurrent.Threads;
import fr.aresrpg.commons.domain.functional.suplier.Supplier;
import fr.aresrpg.commons.domain.util.exception.NotImplementedException;
import fr.aresrpg.dofus.structures.Chat;
import fr.aresrpg.eratz.domain.data.dofus.map.Path;
import fr.aresrpg.eratz.domain.data.player.Perso;
import fr.aresrpg.eratz.domain.ia.behavior.*;
import fr.aresrpg.eratz.domain.ia.behavior.move.BankDepositPath;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Predicate;

/**
 * 
 * @since
 */
public class BaseMind implements Mind {

	private final Perso perso;
	private Queue<Supplier<BehaviorStopReason>> actions = new LinkedList<>();
	private boolean infinite;
	private Set<Integer> itemsToKeep = new HashSet<>();
	private ConcurrentLinkedQueue<Runnable> forceds = new ConcurrentLinkedQueue<>();
	private boolean running;

	public BaseMind(Perso perso) {
		this.perso = perso;
	}

	public void shutdown() {
		running = false;
		actions.clear();
		forceds.clear();
	}

	@Override
	public void process() throws InterruptedException, ExecutionException {
		if (running) return;
		running = true;
		do {
			if (!forceds.isEmpty()) { // forced en prio
				forceds.poll().run();
				continue;
			}
			if (getActions().isEmpty()) continue;
			Supplier<BehaviorStopReason> next = getActions().poll();
			switch (next.get()) { // possibilité d'effectuer des actions selon le type de retour
				case QUANTITY_REACHED:
					getPerso().getAbilities().getBaseAbility().speak(Chat.ADMIN, "Récolte terminée.");
					break;
			}
			if (infinite) getActions().add(next); // si infinite loop ajout a la queue
		} while (getPerso().getAccount().isActive());
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void forceBehavior(Behavior b) {
		forceds.add(b::start);
	}

	@Override
	public Queue<Runnable> getForcedActions() {
		return forceds;
	}

	/**
	 * @return the actions
	 */
	public Queue<Supplier<BehaviorStopReason>> getActions() {
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
	public Mind thenDisconnectIf(String reason, Predicate<Perso> condition) {
		getActions().add(() -> {
			if (condition.test(getPerso())) getPerso().disconnect(reason);
			while (!getPerso().getAccount().isOffline())
				;
			return BehaviorStopReason.FINISHED;
		});
		return this;
	}

	@Override
	public Set<Integer> getItemToKeep() {
		return this.itemsToKeep;
	}

	@Override
	public Mind thenConnect(Perso perso) {
		getActions().add(() -> {
			getPerso().connect();
			return BehaviorStopReason.FINISHED;
		});
		return this;
	}

	@Override
	public Mind thenReconnect() {
		getActions().add(() -> {
			getPerso().connect();
			return BehaviorStopReason.FINISHED;
		});
		return this;
	}

	@Override
	public Mind thenWait(long time, TimeUnit unit) {
		getActions().add(() -> {
			Threads.uSleep(time, unit);
			return BehaviorStopReason.FINISHED;
		});
		return this;
	}

	@Override
	public Mind thenIdle() {
		getActions().add(new AntiAfkBehavior(getPerso(), false)); // pour cancel juste changer la state du perso
		return this;
	}

}
